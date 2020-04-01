package pro.antonshu.server.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pro.antonshu.server.entities.Document;
import pro.antonshu.server.entities.dto.DocumentDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentMapper MAPPER = Mappers.getMapper(DocumentMapper.class);

    DocumentDto fromDocument(Document document);

    Document toDocument(DocumentDto documentDto);

    List<Document> toDocumentList(List<DocumentDto> orderDtoList);

    List<DocumentDto> fromDocumentList(List<Document> orderList);
}
