package site.concertseat.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.concertseat.global.statuscode.ErrorCode;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    ErrorCode errorCode;
}
