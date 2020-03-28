package pro.antonshu.server.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.antonshu.server.services.DocumentService;

import java.util.List;

@RestController
@RequestMapping(value = "/docs")
public class DocsController {

    private DocumentService documentService;

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/all")
    public List<Long> getAllDocs() {
        List<Long> res = documentService.getAllDocumentsId();
        System.out.println("res: " + res);
        return res;
    }
}
