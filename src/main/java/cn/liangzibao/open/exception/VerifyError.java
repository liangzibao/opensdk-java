package cn.liangzibao.open.exception;

/**
 * Created by wangbai on 2017/2/21.
 */
public class VerifyError extends AbstractGetwayError {

    public VerifyError() {
        super();
    }

    public VerifyError(String message) {
        super(message);
    }

    public VerifyError(String message, Throwable cause) {
        super(message, cause);
    }

}
