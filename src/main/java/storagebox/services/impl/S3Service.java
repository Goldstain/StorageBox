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

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public void uploadToS3(MultipartFile multipartFile, String contentType) throws IOException
            , AmazonServiceException, SdkClientException {

        try (InputStream inputStream = multipartFile.getInputStream()) {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(multipartFile.getSize());

            String filename = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

            s3Client.putObject(new PutObjectRequest(bucketName, filename
                    , inputStream, metadata));
        }
    }

}