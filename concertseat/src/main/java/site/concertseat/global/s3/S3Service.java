package site.concertseat.global.s3;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import org.springframework.web.multipart.MultipartFile;
import site.concertseat.global.exception.CustomException;

import java.io.IOException;
import java.util.List;

public interface S3Service {
    String upload(MultipartFile multipartFile, String dirName, int order);

    List<String> uploadMultipleFiles(List<MultipartFile> multipartFiles, String dirName) throws IOException;

    String uploadCompressedImage(String fileUrl) throws IOException, ImageProcessingException, MetadataException;

    void deleteFolder(String folderPath) throws CustomException;

    void deleteFile(String fileUrl) throws CustomException;
}
