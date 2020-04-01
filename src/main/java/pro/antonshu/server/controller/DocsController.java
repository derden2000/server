package pro.antonshu.server.controller;

import com.google.gson.Gson;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.antonshu.server.entities.Document;
import pro.antonshu.server.entities.User;
import pro.antonshu.server.entities.dto.DocumentDto;
import pro.antonshu.server.mappers.DocumentMapper;
import pro.antonshu.server.services.DocumentService;
import pro.antonshu.server.services.UserService;
import pro.antonshu.server.utils.Packet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

@RestController
@RequestMapping(value = "/docs")
public class DocsController {

    private DocumentService documentService;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/get")
    @ResponseBody
    public String getAllDocs(Principal principal,
                                     @RequestParam("id") Long id) {
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findOneByLogin(currentUser.getUsername());
//        System.out.println("User find :" + user);
        Gson gson = new Gson();
        if (id > 0) {
            List<Document> answer = Collections.singletonList(documentService.findOneById(id));
            List<DocumentDto> res = DocumentMapper.MAPPER.fromDocumentList(answer);
//            System.out.println("All docs: " + answer);
//            System.out.println("JSON String: " + gson.toJson(new Packet(answer)));
//            System.out.println("JSON String: " + gson.toJson(new Packet(res)));
            return gson.toJson(new Packet(res));
        }
        List<DocumentDto> res = DocumentMapper.MAPPER.fromDocumentList((List<Document>) user.getDocuments());
//        System.out.println("All docs: " + res);
        String resStr = gson.toJson(new Packet(res));
//        System.out.println("JSON String: " + resStr);
        return resStr;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/get/ent")
    @ResponseBody
    public Document getDoc(Principal principal,
                                     @RequestParam("id") Long id) {
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findOneByLogin(currentUser.getUsername());

        Document doc = documentService.findOneById(id);
        return doc;
    }

    @PostMapping("/rcv")
    @ResponseBody
    public String submit(Principal principal,
                         @RequestBody Document document) {
        System.out.println("Request with body was received");
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findOneByLogin(currentUser.getUsername());
        List<Document> list = (List<Document>) user.getDocuments();
        Document toSave = documentService.save(document);
        list.add(toSave);
        user.setDocuments(list);
        userService.save(user);
        System.out.println("New Users list after saving: " + userService.getUser(user).getDocuments());

        return String.format("New Users list after saving: %s", userService.getUser(user).getDocuments());
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/pkcs")
    @ResponseBody
    public String receivePkcs7(Principal principal,
                         @RequestBody Document document) {
        UserDetails currentUser
                = (UserDetails) ((Authentication) principal).getPrincipal();
        User receivedFrom = userService.findOneByLogin(currentUser.getUsername());
        System.out.println("Received info from: " + receivedFrom.getLogin());
        System.out.println("Received info from: " + receivedFrom);

        byte[] buffer = Base64.getDecoder().decode(document.getData());

        //Corresponding class of signed_data is CMSSignedData
        CMSSignedData signature = null;
        try {
            signature = new CMSSignedData(buffer);
        } catch (CMSException e) {
            e.printStackTrace();
        }
        Store cs = signature.getCertificates();
        SignerInformationStore signers = signature.getSignerInfos();
        Collection c = signers.getSigners();
        Iterator it = c.iterator();

        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            Collection certCollection = cs.getMatches(signer.getSID());
            Iterator certIt = certCollection.iterator();
            X509CertificateHolder cert = (X509CertificateHolder) certIt.next();

            //get CA public key
            // Create a X509 certificate
            CertificateFactory certificatefactory = null;
            try {
                certificatefactory = CertificateFactory.getInstance("X.509");
            } catch (CertificateException e) {
                e.printStackTrace();
            }

            // Open the certificate file
            FileInputStream fileinputstream = null;
            try {
                fileinputstream = new FileInputStream("src/main/resources/serverCA.crt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //get CA public key
            PublicKey pk = null;
            try {
                pk = certificatefactory.generateCertificate(fileinputstream).getPublicKey();
            } catch (CertificateException e) {
                e.printStackTrace();
            }

            X509Certificate myCA = null;
            try {
                Security.addProvider(new BouncyCastleProvider());
                myCA = new JcaX509CertificateConverter().setProvider("BC").getCertificate(cert);
                myCA.verify(pk);
                System.out.println("Verification done successfully!");
            } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException e) {
                e.printStackTrace();
                return "Verification of your certificate was failed";
            }

            CMSProcessable sc = signature.getSignedContent();
            byte[] data = (byte[]) sc.getContent();

            List<Document> list = new ArrayList<>(receivedFrom.getDocuments());
            Document toSave = documentService.save(new Document(document.getTitle(), data));
            list.add(toSave);
            receivedFrom.setDocuments(list);
            userService.save(receivedFrom);

            File encodedFile = new File(document.getTitle());
            try {
                Files.write(Paths.get(String.valueOf(encodedFile)), data);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            System.out.println("Start sending to MCS");
//            RestTemplate restTemplate = new RestTemplate();
//            restTemplate.put("https://antonshu.hb.bizmrg.com/", encodedFile);
//            System.out.println("Sending done");
        }
        return String.format("New Users list after saving: %s", userService.getUser(receivedFrom).getDocuments());
    }
}
