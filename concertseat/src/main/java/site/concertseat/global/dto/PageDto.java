package site.concertseat.global.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageDto<T> {
    private List<T> content;

    private int pageNum;

    private int pageSize;

    private int totalPage;

    private long totalElements;

    public PageDto(Page<T> page) {
        this.content = page.getContent();
        this.pageNum = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPage = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }
}
