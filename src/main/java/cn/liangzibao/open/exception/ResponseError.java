package cn.liangzibao.open.exception;

/**
 * Created by wangbai on 2017/2/21.
 */
public class ResponseError extends AbstractGetwayError {

    protected long errorCode;

    public ResponseError() {
        super();
        this.errorCode = 0;
    }

    public ResponseError(String message) {
        super(message);
        this.errorCode = 0;
    }

    public ResponseError(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = 0;
    }

    public ResponseError(String message, long errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ResponseError(String message, long errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public long getErrorCode() {
        return this.errorCode;
    }

}
