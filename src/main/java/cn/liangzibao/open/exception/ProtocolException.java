package cn.liangzibao.open.exception;

/**
 * Created by wangbai on 2017/2/20.
 */
public class ProtocolException extends Exception {

    public ProtocolException() {
        super();
    }

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

}
