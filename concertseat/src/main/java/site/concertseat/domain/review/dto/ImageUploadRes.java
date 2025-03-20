package site.concertseat.domain.review.dto;

import lombok.Data;

import java.util.List;

@Data
public class ImageUploadRes {
    private List<String> originalImage;

    public ImageUploadRes(List<String> originalImage) {
        this.originalImage = originalImage;
    }
}
