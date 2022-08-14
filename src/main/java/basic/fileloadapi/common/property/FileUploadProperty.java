package basic.fileloadapi.common.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@ConstructorBinding
@ConfigurationProperties(prefix = "file")
@Getter
public class FileUploadProperty {
    private final String dir;

    public FileUploadProperty(String dir) {
        this.dir = dir;
    }
}
