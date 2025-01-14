package storagebox.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileStoreService {

    public String uploadMultipartFile(MultipartFile data);

    public String uploadFileToS3(MultipartFile data);

    public String uploadBase64File(String filename, String filetype, String data);

    public FileStorage retreiveFile(String filename);

    public byte[] downloadMultipartFile(String filename);

    public byte[] downloadS3File(String filename);

    public byte[] downloadBase64File(String filename);
}
