package com.dbbl.controller;


import com.dbbl.service.SmsUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/excel")
public class SmsUploadController {
    private final SmsUploadService smsUploadService;

    public SmsUploadController(SmsUploadService smsUploadService) {
        this.smsUploadService = smsUploadService;
    }

    @PostMapping(
            value = "/SMSupload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )

    public ResponseEntity<String> uploadSmsExcel(
            @RequestParam("file") MultipartFile file) {

        try {

            int totalRecords =
                    smsUploadService.uploadSmsExcel(file);

            return ResponseEntity.ok(
                    "SMS Excel uploaded successfully. Total records: "
                            + totalRecords
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("SMS upload failed: " + e.getMessage());
        }
    }

}
