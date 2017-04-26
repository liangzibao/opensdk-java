package cn.liangzibao.open.exception;

/**
 * Created by wangbai on 2017/4/26.
 */
public class DecryptCommonParamsError extends Exception {
    public DecryptCommonParamsError() {
        super();
    }

    public DecryptCommonParamsError(String message) {
        super(message);
    }

    public DecryptCommonParamsError(String message, Throwable cause) {
        super(message, cause);
    }
}
