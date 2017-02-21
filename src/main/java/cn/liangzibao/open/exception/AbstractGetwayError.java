package cn.liangzibao.open.exception;

/**
 * Created by wangbai on 2017/2/21.
 */
public abstract class AbstractGetwayError extends Exception {

    public AbstractGetwayError() {
        super();
    }

    public AbstractGetwayError(String message) {
        super(message);
    }

    public AbstractGetwayError(String message, Throwable cause) {
        super(message, cause);
    }

}
