package basic.fileloadapi.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttachFileDto {
    private Long attachFileId;
    private String uploadFileName;
    private String storeFileName;

    @QueryProjection
    public AttachFileDto(Long attachFileId, String uploadFileName, String storeFileName) {
        this.attachFileId = attachFileId;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
