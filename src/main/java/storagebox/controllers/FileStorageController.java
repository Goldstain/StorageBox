package storagebox.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import storagebox.services.impl.FileStoreServiceImpl;


@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStoreServiceImpl fsServiceImpl;

    @PostMapping("/multipart")
    public ResponseEntity<?> uploadMultipartFile(@RequestParam("file") MultipartFile data) {
        String response = fsServiceImpl.uploadMultipartFile(data);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/multipart/s3")
    public ResponseEntity<?> uploadMultipartFileToS3(@RequestParam("file") MultipartFile data) {
        String response = fsServiceImpl.uploadFileToS3(data);

        return ResponseEntity.ok(response);
    }

    @PostMapping("base64")
    public ResponseEntity<?> uploadBase64File(@RequestBody FileStorageDTO data) {
        String response = fsServiceImpl.uploadBase64File(data.getFilename(), data.getFiletype
                , data.getFilebase64());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> upload(@RequestParam(value = "file", required = false) MultipartFile multipartData
            , @RequestBody(required = false) FileStorageDTO base64Data) {

        if (base64Data == null) {
            String response = fsServiceImpl.uploadMultipartFile(multipartData);
            return ResponseEntity.ok(response);
        } else {
            String response = fsServiceImpl.uploadBase64File(base64Data.getFilename()
                    , base64Data.getFiletype(), base64Data.getFilebase64());
            return ResponseEntity.ok(response);
        }
    }


    @GetMapping("/multipart/{filename}")
    public ResponseEntity<?> downloadMultipartfile(@PathVariable String filename) {
        FileStorage fileDetails = fsServiceImpl.retreiveFile(filename);
        byte[] file = fsServiceImpl.downloadMultipartFile(filename);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(fileDetails.getFiletype()))
                .body(file);
    }

    @GetMapping("/multipart/s3/{filename}")
    public ResponseEntity<?> downloadMultipartS3File(@PathVariable String filename) {
        FileStorage fileDetails = fsServiceImpl.retreiveFile(filename);
        byte[] file = fsServiceImpl.downloadS3File(filename);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(fileDetails.getFiletype()))
                .body(file);
    }

    @GetMapping("/base64/{filename}")
    public ResponseEntity<?> downloadBase64File(@PathVariable String filename) {
        FileStorage fileDetails = fsServiceImpl.retreiveFile(filename);
        byte[] file = fsServiceImpl.downloadBase64File(filename);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(fileDetails.getFiletype()))
                .body(file);
    }

}
