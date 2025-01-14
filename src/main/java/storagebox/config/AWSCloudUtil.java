package storagebox.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.*;

public class AWSCloudUtil {


    private AWSCredentials awsCredentials(String accessKey, String secretKey) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return credentials;
    }

    private AmazonS3 amazonS3ClientBuilder(String accessKey, String secretKey) {
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials(accessKey, secretKey)))
                .withRegion(Regions.EU_NORTH_1)
                .build();
        return s3client;
    }

    public void uploadFileToS3(String filename, byte[] fileBytes, String accessKey, String secretKey
            , String bucket) {
        AmazonS3 s3client = amazonS3ClientBuilder(accessKey, secretKey);

        File file = new File(filename);

        try (OutputStream os = new FileOutputStream(file)) {
            os.write(fileBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        s3client.putObject(bucket, filename, file);
    }

    public S3ObjectInputStream downloadFileFromS3(String filename, String accessKey, String secretKey
            , String bucket) {
        AmazonS3 s3client = amazonS3ClientBuilder(accessKey, secretKey);
        S3Object s3Object = s3client.getObject(bucket, filename);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        return inputStream;
    }

}
