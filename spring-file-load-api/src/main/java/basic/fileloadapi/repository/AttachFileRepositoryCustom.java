package basic.fileloadapi.repository;

import basic.fileloadapi.dto.AttachFileDto;

import java.util.Optional;

public interface AttachFileRepositoryCustom {
    Optional<AttachFileDto> findByFileIdToDto(Long attachFileId);
}
