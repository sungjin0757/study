package basic.fileloadapi.common.property;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {
    private String errorCode;
    private String message;
}
