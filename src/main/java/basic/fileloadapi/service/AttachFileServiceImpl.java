package basic.fileloadapi.service;

import basic.fileloadapi.common.exception.FileException;
import basic.fileloadapi.common.property.FileUploadProperty;
import basic.fileloadapi.domain.AttachFile;
import basic.fileloadapi.dto.AttachFileDto;
import basic.fileloadapi.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AttachFileServiceImpl implements AttachFileService{
    private final AttachFileRepository attachFileRepository;
    private final FileUploadProperty fileUploadProperty;

    @Override
    public AttachFileDto findAttachFile(Long attachFileId) {
        return attachFileRepository.findByFileIdToDto(attachFileId)
                .orElseThrow(() -> {
                    throw new FileException("해당하는 file이 존재하진 않습니다.");
        });
    }

    @Transactional
    @Override
    public AttachFileDto uploadFile(MultipartFile multipartFile) {
        if(!isExistFile(multipartFile))
            return null;

        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = genStoreFileName(originalFileName);

        log.info("storeFileName = {}", storeFileName);

        saveAttachFileInDir(multipartFile, storeFileName);
        AttachFile attachFile = saveAttachFileInDb(originalFileName, storeFileName);

        return new AttachFileDto(attachFile.getId(), originalFileName, storeFileName);
    }

    @Override
    public String getFullPath(String fileName) {
        return fileUploadProperty.getDir() + fileName;
    }

    private void saveAttachFileInDir(MultipartFile multipartFile, String storeFileName) {
        try {
            multipartFile.transferTo(new File(getFullPath(storeFileName)));
        } catch (IOException e) {
            throw new FileException("Upload Exception Occurred!");
        }
    }

    private AttachFile saveAttachFileInDb(String originalFileName, String storeFileName) {
        AttachFile attachFile = new AttachFile(originalFileName, storeFileName);
        attachFileRepository.save(attachFile);
        return attachFile;
    }

    private boolean isExistFile(MultipartFile multipartFile) {
        if(multipartFile.isEmpty())
            return false;
        return true;
    }

    private String genStoreFileName(String originalFileName) {
        String format = genFileFormat(originalFileName);
        String uuid = UUID.randomUUID().toString();
        StringBuilder sb = new StringBuilder();

        sb.append(uuid);
        sb.append(".");
        sb.append(format);

        return sb.toString();
    }

    private String genFileFormat(String originalFileName) {
        int formatPos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(formatPos + 1);
    }

}
