package site.concertseat.domain.bookmark.dto.res;

import lombok.Data;
import site.concertseat.domain.bookmark.dto.BookmarkStadiumDto;

import java.util.List;

@Data
public class BookmarkStadiumListRes {
    private List<BookmarkStadiumDto> stadiums;

    public BookmarkStadiumListRes(List<BookmarkStadiumDto> stadiums) {
        this.stadiums = stadiums;
    }
}
