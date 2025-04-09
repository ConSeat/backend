package site.concertseat.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookmarkStadiumDto {
    private Integer stadiumId;

    private String stadiumName;
}
