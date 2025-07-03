package site.concertseat.global.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
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
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import static site.concertseat.global.statuscode.ErrorCode.*;
import static site.concertseat.global.util.DateFormatter.convertToTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "webp", "heic");

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${BUCKET_URL}")
    private String bucketUrl;

    @Override
    public String upload(MultipartFile multipartFile, String dirName, int order) {
        if (multipartFile.isEmpty() || Objects.isNull(multipartFile.getOriginalFilename())) {
            throw new CustomException(FILE_UPLOAD_FAIL);
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        validateImageFileExtension(extension);

        String s3FileName = convertS3Name(dirName, extension, order);

        byte[] content = convertToByte(multipartFile);

        return uploadFile(content, s3FileName, "image/" + extension);
    }

    private byte[] convertToByte(MultipartFile multipartFile) {
        try {
            return multipartFile.getBytes();
        } catch (IOException e) {
            throw new CustomException(FILE_UPLOAD_FAIL);
        }
    }

    private String convertS3Name(String dirName, String extension, int order) {
        return String.format("%s/upload_%s-%02d.%s",
                dirName, convertToTime(LocalDateTime.now()), order, extension);
    }

    private String uploadFile(byte[] content, String s3FileName, String contentType) {
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
    public List<String> uploadMultipleFiles(List<MultipartFile> multipartFiles, String dirName) {
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
    public String uploadCompressedImage(String fileUrl) throws IOException, ImageProcessingException, MetadataException {
        String fileKey = fileUrl.replace(bucketUrl, "");

        int orientation = 1;
        try (S3ObjectInputStream metaInputStream = amazonS3Client.getObject(bucket, fileKey).getObjectContent()) {
            Metadata metadata = ImageMetadataReader.readMetadata(metaInputStream);
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        }

        BufferedImage image;
        try (S3ObjectInputStream imageInputStream = amazonS3Client.getObject(bucket, fileKey).getObjectContent()) {
            image = ImageIO.read(imageInputStream);
        }

        image = transformImageByOrientation(image, orientation);

        if (image.getTransparency() == Transparency.TRANSLUCENT) {
            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = newImage.createGraphics();
            g2d.drawImage(image, 0, 0, Color.WHITE, null);
            g2d.dispose();
            image = newImage;
        }

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

        return url.substring(0, lastDotIndex) + "_compress.jpeg";
    }

    private void validateImageFileExtension(String extension) {
        if (!SUPPORTED_EXTENSIONS.contains(extension)) {
            throw new CustomException(FILE_EXTENSION_FAIL);
        }
    }

    private BufferedImage transformImageByOrientation(BufferedImage image, int orientation) {
        int w = image.getWidth();
        int h = image.getHeight();
        AffineTransform transform = new AffineTransform();

        switch (orientation) {
            case 6:
                transform.translate(h, 0);
                transform.rotate(Math.toRadians(90));
                break;
            case 3:
                transform.translate(w, h);
                transform.rotate(Math.toRadians(180));
                break;
            case 8:
                transform.translate(0, w);
                transform.rotate(Math.toRadians(270));
                break;
            default:
                return image;
        }

        BufferedImage rotatedImage = new BufferedImage(
                orientation == 6 || orientation == 8 ? h : w,
                orientation == 6 || orientation == 8 ? w : h,
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.setTransform(transform);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
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
