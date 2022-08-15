package basic.fileloadapi.common.exception.advice;

import basic.fileloadapi.common.exception.FileException;
import basic.fileloadapi.common.property.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(FileException.class)
    public ResponseEntity<ErrorResult> genFileException(FileException ex) {
        ErrorResult errorResult = new ErrorResult("BAD", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResult);
    }

}
