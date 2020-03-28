package pro.antonshu.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pro.antonshu.server.entities.Document;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Document findOneById(Long id);

    @Query("select d.id from Document d")
    List<Long> findAllIDocumentsId();
}
