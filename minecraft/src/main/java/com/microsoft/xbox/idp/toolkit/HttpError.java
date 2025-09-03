package com.microsoft.xbox.idp.toolkit;

import java.io.InputStream;
import java.util.Scanner;

public class HttpError {
    private static final String INPUT_START_TOKEN = "\\A";
    private final int errorCode;
    private final String errorMessage;
    private final int httpStatus;

    public HttpError(int i, int i2, String str) {
        this.errorCode = i;
        this.httpStatus = i2;
        this.errorMessage = str;
    }

    public HttpError(int i, int i2, InputStream inputStream) {
        this.errorCode = i;
        this.httpStatus = i2;
        Scanner scannerUseDelimiter = new Scanner(inputStream).useDelimiter(INPUT_START_TOKEN);
        this.errorMessage = scannerUseDelimiter.hasNext() ? scannerUseDelimiter.next() : "";
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public int getHttpStatus() {
        return this.httpStatus;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("errorCode: ");
        stringBuffer.append(this.errorCode).append(", httpStatus: ").append(this.httpStatus).append(", errorMessage: ").append(this.errorMessage);
        return stringBuffer.toString();
    }
}
