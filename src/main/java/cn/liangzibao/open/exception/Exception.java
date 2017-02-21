package cn.liangzibao.open.exception;

/**
 * Created by wangbai on 2017/2/20.
 */
public class Exception extends java.lang.Exception {

    public Exception() {
        super();
    }

    public Exception(String message) {
        super(message);
    }

    public Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
