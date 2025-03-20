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
    FILE_UPLOAD_FAIL(400, "File Upload Fail"),
    FILE_EXTENSION_FAIL(400, "File Extension Fail"),
    FILE_DELETE_FAIL(404, "File Delete Fail"),
    ;

    private final int httpStatusCode;

    private final String message;

}

