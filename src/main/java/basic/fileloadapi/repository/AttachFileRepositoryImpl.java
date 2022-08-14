package basic.fileloadapi.repository;

import basic.fileloadapi.dto.AttachFileDto;
import basic.fileloadapi.dto.QAttachFileDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static basic.fileloadapi.domain.QAttachFile.*;

@RequiredArgsConstructor
public class AttachFileRepositoryImpl implements AttachFileRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<AttachFileDto> findByFileIdToDto(Long attachFileId) {
        AttachFileDto attachFileDto = jpaQueryFactory
                .select(new QAttachFileDto(attachFile.id,
                        attachFile.uploadFileName,
                        attachFile.storeFileName))
                .from(attachFile)
                .where(attachFile.id.eq(attachFileId))
                .fetchOne();
        return Optional.ofNullable(attachFileDto);
    }
}
