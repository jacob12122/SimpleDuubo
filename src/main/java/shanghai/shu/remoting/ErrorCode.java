package shanghai.shu.remoting;

public enum ErrorCode {

    SUCCESS(1), ERROR(2);

    public int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
