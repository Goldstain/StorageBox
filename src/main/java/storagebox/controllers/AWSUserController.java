package storagebox.controllers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import storagebox.services.impl.S3Service;

import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/upload")
public class AWSUserController {

    private final S3Service s3Service;

    @GetMapping
    public String fileUploadPage() {
        return "form";
    }

    @PostMapping
    public String saveFiles(@RequestParam("description") String description
            , @RequestParam("file") MultipartFile multipartFile, Model model) {

        String contentType = multipartFile.getContentType();
        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/gif");
        if (!allowedTypes.contains(contentType)) {
            model.addAttribute("error", "Unsupported file type. Only JPEG, PNG, and GIF are allowed");
            return "form";
        }

        try {
            s3Service.uploadToS3(multipartFile, contentType);
            model.addAttribute("message", "File Successfully Uploaded");
        } catch (AmazonServiceException e) {
            model.addAttribute("error", "AWS Service Exception");
            e.printStackTrace();
        } catch (SdkClientException e) {
            model.addAttribute("error", "SDK Client Exception");
            e.printStackTrace();
        } catch (IOException e) {
            model.addAttribute("error", "Error Uploading file");
            e.printStackTrace();
        }

        return "form";
    }
}
