package site.concertseat.global.s3;

import org.springframework.web.multipart.MultipartFile;
import site.concertseat.global.exception.CustomException;

import java.io.IOException;
import java.util.List;

public interface S3Service {
    String upload(MultipartFile multipartFile, String dirName) throws IOException;

    List<String> uploadMultipleFiles(List<MultipartFile> multipartFiles, String dirName) throws IOException;

    List<String> uploadCompressedMultipartFiles(List<MultipartFile> multipartFiles, String dirName) throws IOException;

    void deleteFolder(String folderPath) throws CustomException;

    void deleteFile(String fileUrl) throws CustomException;
}
