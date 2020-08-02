package core.exception;

/**
 * description
 * 
 * @author LSZ 2019/11/03 13:27
 * @contact 648748030@qq.com
 */
public class ValidaException extends Exception {

    public ValidaException(){
        super("发生未知错误，请联系管理员");
    }

    public ValidaException(String s) {
        super(s);
    }

}
