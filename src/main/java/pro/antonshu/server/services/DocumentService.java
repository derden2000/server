package pro.antonshu.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.antonshu.server.entities.Document;
import pro.antonshu.server.repositories.DocumentRepository;

import java.util.List;

@Service
public class DocumentService {

    private DocumentRepository documentRepository;

    @Autowired
    public void setDocumentRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document findOneById(Long id) {
        return documentRepository.findById(id).get();
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public List<Long> getAllDocumentsId() {
        return documentRepository.findAllIDocumentsId();
    }
}
