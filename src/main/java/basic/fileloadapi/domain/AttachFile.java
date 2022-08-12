package basic.fileloadapi.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachFile extends AbstractDataTraceEntity{
    @Id
    @GeneratedValue
    @Column(name = "attach_file_id")
    private Long id;

    private String uploadFileName;
    private String storeFileName;

    public AttachFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
