package site.concertseat.global.statuscode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    OK(200, "성공했습니다."),
    NO_CONTENT(204,"성공했습니다."),
    CREATED(201, "생성에 성공했습니다"),
    DELETED(204, "삭제에 성공했습니다")
    ;

    private final int httpStatusCode;

    private final String message;
}

