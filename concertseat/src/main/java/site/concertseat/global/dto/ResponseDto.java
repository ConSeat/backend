package site.concertseat.global.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import site.concertseat.global.statuscode.ErrorCode;
import site.concertseat.global.statuscode.SuccessCode;

@Getter
public class ResponseDto<T> extends ResponseEntity<ResponseData<T>> {

    public ResponseDto(HttpStatus status, String message) {
        super(new ResponseData<>(new ResponseHeader(message), null), status);
    }

    public ResponseDto(T body, HttpStatus status, String message) {
        super(new ResponseData<>(new ResponseHeader(message), body), status);
    }

    public static <T> ResponseDto<T> success(SuccessCode s, T body) {
        return new ResponseDto<>(body, HttpStatus.valueOf(s.getHttpStatusCode()), s.getMessage());
    }

    public static ResponseDto<Void> success(SuccessCode s) {
        return new ResponseDto<>(HttpStatus.valueOf(s.getHttpStatusCode()), s.getMessage());
    }

    public static <T> ResponseDto<T> fail(ErrorCode e) {
        return new ResponseDto<>(HttpStatus.valueOf(e.getHttpStatusCode()), e.getMessage());
    }

    public static <T> ResponseDto<T> fail(String errorMessage) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, errorMessage);
    }
}