package site.concertseat.global.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.concertseat.global.exception.CustomException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import static site.concertseat.global.statuscode.ErrorCode.*;
import static site.concertseat.global.util.DateFormatter.convertToTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${BUCKET_URL}")
    private String bucketUrl;

    @Override
    public String upload(MultipartFile multipartFile, String dirName, int order) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        validateImageFileExtension(extension);

        String convertExtension = "jpeg";

        String s3FileName = convertS3Name(multipartFile, dirName, convertExtension, order);

        byte[] content = convertToByte(multipartFile);

        return uploadFile(content, s3FileName, "image/" + convertExtension);
    }

    private byte[] convertToByte(MultipartFile multipartFile) throws IOException {
        return multipartFile.getBytes();
    }

    private String convertS3Name(MultipartFile multipartFile, String dirName, String extension, int order) {
        if (multipartFile.isEmpty() || Objects.isNull(multipartFile.getOriginalFilename())) {
            throw new CustomException(FILE_UPLOAD_FAIL);
        }

        return String.format("%s/upload_%s-%02d.%s",
                dirName, convertToTime(LocalDateTime.now()), order, extension);
    }

    private String uploadFile(byte[] content, String s3FileName, String contentType) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(content.length);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, s3FileName, byteArrayInputStream, objectMetadata);
            amazonS3Client.putObject(putObjectRequest);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new CustomException(FILE_UPLOAD_FAIL);
        } finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        return generateS3(s3FileName);
    }

    private String generateS3(String s3FileName) {
        if (!bucketUrl.endsWith("/")) {
            bucketUrl += "/";
        }
        return bucketUrl + s3FileName;
    }

    @Override
    public List<String> uploadMultipleFiles(List<MultipartFile> multipartFiles, String dirName) throws IOException {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            throw new CustomException(FILE_UPLOAD_FAIL);
        }

        List<String> uploadedUrls = new ArrayList<>();

        for(int i = 0; i < multipartFiles.size(); i++) {
            MultipartFile file = multipartFiles.get(i);

            String uploadedUrl = upload(file, dirName, i+1);

            uploadedUrls.add(uploadedUrl);
        }

        return uploadedUrls;
    }

    @Override
    public List<String> convertCompressedMultipartFiles(List<String> fileUrls) throws IOException {
        List<String> uploadedUrls = new ArrayList<>();

        for (String url : fileUrls) {
            String uploadedUrl = uploadCompressedImage(url);
            uploadedUrls.add(uploadedUrl);
        }

        return uploadedUrls;
    }


    private String uploadCompressedImage(String fileUrl) throws IOException {
        String fileKey = fileUrl.replace(bucketUrl, "");

        S3Object s3Object = amazonS3Client.getObject(bucket, fileKey);
        S3ObjectInputStream s3InputStream = s3Object.getObjectContent();
        BufferedImage image = ImageIO.read(s3InputStream);

        String extension = "jpeg";
        String s3FileName = convertToCompressedUrl(fileKey);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            throw new IllegalArgumentException("No PNG writers available");
        }
        ImageWriter writer = writers.next();

        try (ImageOutputStream ios = new MemoryCacheImageOutputStream(baos)) {
            writer.setOutput(ios);

            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(0.5f);

            writer.write(null, new IIOImage(image, null, null), writeParam);

            return uploadFile(baos.toByteArray(), s3FileName, "image/" + extension);
        } finally {
            writer.dispose();
        }
    }

    private String convertToCompressedUrl(String url) {
        int lastDotIndex = url.lastIndexOf(".");

        return url.substring(0, lastDotIndex) + "_compress" + url.substring(lastDotIndex);
    }

    private void validateImageFileExtension(String extension) {
        if (!Arrays.asList("jpg", "jpeg", "png", "webp").contains(extension)) {
            throw new CustomException(FILE_EXTENSION_FAIL);
        }
    }

    @Override
    public void deleteFolder(String folderPath) throws CustomException {
        try {
            ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request().withBucketName(bucket).withPrefix(folderPath);
            ListObjectsV2Result result;

            do {
                result = amazonS3Client.listObjectsV2(listObjectsRequest);
                List<S3ObjectSummary> objects = result.getObjectSummaries();

                for (S3ObjectSummary objectSummary : objects) {
                    amazonS3Client.deleteObject(bucket, objectSummary.getKey());
                }

                listObjectsRequest.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated());

        } catch (AmazonServiceException e) {
            throw new CustomException(FILE_DELETE_FAIL);
        } catch (Exception e) {
            throw new CustomException(FILE_DELETE_FAIL);
        }
    }

    @Override
    public void deleteFile(String fileUrl) throws CustomException {
        try {
            try {
                String fileKey = fileUrl.replace(bucketUrl, "");
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileKey));
            } catch (AmazonServiceException e) {
                throw new CustomException(FILE_DELETE_FAIL);
            }
        } catch (Exception exception) {
            throw new CustomException(FILE_DELETE_FAIL);
        }
    }
}
