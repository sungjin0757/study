package basic.fileloadapi.service;

import basic.fileloadapi.dto.AttachFileDto;
import org.springframework.web.multipart.MultipartFile;

public interface AttachFileService {
    String getFullPath(String fileName);
    AttachFileDto findAttachFile(Long attachFileId);
    AttachFileDto uploadFile(MultipartFile multipartFile);
}
