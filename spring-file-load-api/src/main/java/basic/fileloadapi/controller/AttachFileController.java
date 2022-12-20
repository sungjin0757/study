package basic.fileloadapi.controller;

import basic.fileloadapi.dto.AttachFileDto;
import basic.fileloadapi.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attach")
public class AttachFileController {
    private final AttachFileService attachFileService;

    @PostMapping("/upload")
    public ResponseEntity<AttachFileDto> uploadFile(@RequestParam MultipartFile file)
            throws IOException{
        logUploadFile(file);
        AttachFileDto attachFileDto = attachFileService.uploadFile(file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attachFileDto);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable("fileId")Long fileId)
            throws MalformedURLException{
        AttachFileDto attachFileDto = attachFileService.findAttachFile(fileId);
        String uploadFileName = attachFileDto.getUploadFileName();
        String storeFileName = attachFileDto.getStoreFileName();

        logDownloadFile(uploadFileName, storeFileName);

        UrlResource downloadUrlResource = genDownloadUrlResource(storeFileName);
        String responseHeaderForFileDownload = genResponseHeaderForFileDownload(uploadFileName);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, responseHeaderForFileDownload)
                .body(downloadUrlResource);
    }

    private void logUploadFile(MultipartFile file) throws IOException {
        log.info("MultipartFile = {}", file);
        log.info("Content Type = {]", file.getContentType());
        log.info("File Name = {}", file.getOriginalFilename());
    }

    private void logDownloadFile(String uploadFileName, String storeFileName) {
        log.info("uploadFileName = {}", uploadFileName);
        log.info("downloadFileName = {}", storeFileName);
    }

    private UrlResource genDownloadUrlResource(String fileName) throws MalformedURLException {
        return new UrlResource("file:" + attachFileService.getFullPath(fileName));
    }

    private String genResponseHeaderForFileDownload(String uploadFileName) {
        String encodedFileName = encodeFileName(uploadFileName);
        return genContentDispositionHeader(encodedFileName);
    }

    private String encodeFileName(String uploadFileName) {
        return UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
    }

    private String genContentDispositionHeader(String encodedFileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("attachment; filename=\"");
        sb.append(encodedFileName);
        sb.append("\"");

        return sb.toString();
    }
}
