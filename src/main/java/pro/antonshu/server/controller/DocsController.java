package pro.antonshu.server.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.antonshu.server.entities.Document;
import pro.antonshu.server.entities.User;
import pro.antonshu.server.services.DocumentService;
import pro.antonshu.server.services.UserService;

import java.io.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @GetMapping(value = "/all")
    public List<Document> getAllDocs(Principal principal) {
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findOneByLogin(currentUser.getUsername());
        List<Document> res = new ArrayList<>(user.getDocuments());
        System.out.println("res: " + res);
        return res;
    }

//    @PostMapping("/rcv")
//    @ResponseBody
//    public String submit(Principal principal,
//                         @RequestParam("files") MultipartFile[] files,
//                         Model model) {
//
//        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
//        User user = userService.findOneByLogin(currentUser.getUsername());
//
//        for (MultipartFile file : files) {
////            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "windows-1251"));
////            File toWritr = new File(file.getName());
//
//            try (FileOutputStream fos = new FileOutputStream(file.getName())) {
//                fos.write(file.getBytes());
//                System.out.println("Receiving new file");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
////            List<String> strs = reader.lines().collect(Collectors.toList());
////            List<String> procList = new ArrayList<>();
////            strs.forEach(s -> procList.add(s.replace("sudo", "<span style=\"background-color: #eef00e;\"><b>sudo<a name=\"sudo\"></a></b></span>")));
////            procList.forEach(System.out::println);
//////            strs.forEach(s -> s.replace("sudo", "<b>sudo</b>"));
////            modelMap.addAttribute("strings", procList);
////            String searchText = "sudo";
////            modelMap.addAttribute("sw", searchText);
//        }
////        modelMap.addAttribute("files", files);
//        return "fileUploadView - OK";
//    }

    @PostMapping("/rcv")
    @ResponseBody
    public String submit(Principal principal,
                         @RequestBody Document document,
//                         @RequestParam("doc") Document document,
                         Model model) {
        System.out.println("Request with body was received");
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findOneByLogin(currentUser.getUsername());
        List<Document> list = new ArrayList<>(user.getDocuments());
        Document toSave = documentService.save(document);
        list.add(toSave);
        user.setDocuments(list);
        userService.save(user);
        System.out.println("New Users list after saving: " + userService.getUser(user).getDocuments());

//        modelMap.addAttribute("files", files);
        return String.format("New Users list after saving: %s", userService.getUser(user).getDocuments());
    }
}
