package site.concertseat.global.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.concertseat.global.dto.ResponseDto;

import static site.concertseat.global.statuscode.ErrorCode.INVALID_ARGUMENT;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    protected ResponseDto<?> handleException(Exception e) {
        return ResponseDto.fail(e.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseDto<?> handleCustomException(CustomException e) {
        return ResponseDto.fail(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseDto<?> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseDto.fail(INVALID_ARGUMENT);
    }
}
