package pro.antonshu.server.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "documents")
@NoArgsConstructor
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "path")
    private String path;

    @Column(name = "data")
    private byte[] data;

    public Document(String title, byte[] data) {
        this.title = title;
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(id, document.id) &&
                Objects.equals(title, document.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
