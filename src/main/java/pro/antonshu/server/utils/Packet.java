package pro.antonshu.server.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import pro.antonshu.server.entities.dto.DocumentDto;

import java.util.List;

@NoArgsConstructor
@Data
public class Packet {
    private List<DocumentDto> documentDtos;

    public Packet(List<DocumentDto> documentDtos) {
        this.documentDtos = documentDtos;
    }
}
