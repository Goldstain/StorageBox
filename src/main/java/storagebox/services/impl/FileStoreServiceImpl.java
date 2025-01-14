package storagebox.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import storagebox.config.AWSCloudUtil;
import storagebox.services.FileStoreService;

import java.io.IOException;
import java.util.Base64;

@Service
public class FileStoreServiceImpl {

    @Autowired
    FileStoreService fsService;

    @Value("${aws.access-key}")
    private String AWS_ACCESS_KEY;
    @Value("${aws.secret-key}")
    private String AWS_SECRET_KEY;
    @Value("${aws.s3.bucket}")
    private String AWS_BUCKET;

    public String uploadMultipartFile(MultipartFile data) {
        FileStorage file = new FileStorage();
        file.setFilename(data.getOriginalFilename());
        file.setFiletype(data.getContentType());
        try {
            file.setFilebyte(FileStorageUtil.compressFile(data.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileStorage newFile = fsService.persistFile(file);
        if (newFile != null) {
            return String.format("File %s uploaded successfully.", data.getOriginalFilename());
        }

        return String.format("File %s upload failed.", data.getOriginalFilename());
    }

    public String uploadFileToS3(MultipartFile data) {
        try {
            uploadMultipartFile(data);

            AWSCloudUtil AWSCloudUtil = new AWSCloudUtil();
            AWSCloudUtil.uploadFileToS3(data.getOriginalFilename(), data.getBytes()
                    , AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_BUCKET);
            return String.format("File %s uploaded successfully.", data.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.format("File %s upload failed.", data.getOriginalFilename());
    }

    public String uploadBase64File(String filename, String filetype, String data) {
        FileStorage file = new FileStorage();
        file.setFilename(filename);
        file.setFiletype(filetype);
        file.setFilebase64(data);
        FileStorage newFile = fsService.persistFile(file);

        if (newFile != null) {
            return String.format("File %s uploaded successfully.", filename);
        }

        return String.format("File %s upload failed.", filename);
    }

    public FileStorage retreiveFile(String filename) {
        return fsService.retrieveFileByName(filename);
    }

    public byte[] downloadMultipartFile(String filename) {
        return FileStorageUtil.deCompressFile(fsService.retrieveFileByName(filename).getFilebyte());
    }

    public byte[] downloadS3File(String filename) {
        try {
            AWSCloudUtil AWSCloudUtil = new AWSCloudUtil();
            return AWSCloudUtil.downloadFileFromS3(filename, AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_BUCKET)
                    .readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] downloadBase64File(String filename) {
        String data = fsService.retrieveFileByName(filename).getFilebase64();
        String content =data.split(",")[1];
        if (content==null) return null;

        return Base64.decodeBase64(content);
    }
}