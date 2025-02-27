package site.concertseat.global.statuscode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(400,"Bad Request"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    ALREADY_RESERVED(400, "Already Reserved"),
    INVALID_TOKEN(403, "Invalid Token"),
    ;

    private final int httpStatusCode;

    private final String message;

}

