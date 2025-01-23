package storagebox.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public String uploadToS3(MultipartFile multipartFile, String contentType) throws IOException
            , AmazonServiceException, SdkClientException {

        String filename;

        try (InputStream inputStream = multipartFile.getInputStream()) {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(multipartFile.getSize());

            filename = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

            s3Client.putObject(new PutObjectRequest(bucketName, filename
                    , inputStream, metadata));
        }
        return s3Client.getUrl(bucketName, filename).toString();
    }


    public void deleteFromS3(String url) {
        if (url.isEmpty()) return;
        try {
            String filename = url.substring(url.indexOf(".amazonaws.com/") + ".amazonaws.com/".length());
            filename = URLDecoder.decode(filename, StandardCharsets.UTF_8.name());
            s3Client.deleteObject(bucketName, filename);
        } catch (AmazonServiceException e) {
            System.err.println("Amazon Service Exception: " + e.getMessage());
        } catch (SdkClientException e) {
            System.err.println("SDK Client Exception: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Encoding Exception: " + e.getMessage());
        }
    }

    public String getPhotoUrlFromS3(String filename) {
        return s3Client.getUrl(bucketName, filename).toString();
    }


}