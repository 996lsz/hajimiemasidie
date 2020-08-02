package core.exception;

/**
 * description
 * 
 * @author LSZ 2019/11/03 13:27
 * @contact 648748030@qq.com
 */
public class MethodSynchronizedException extends Exception {

    public MethodSynchronizedException(){
        super("功能调用繁忙，请稍后再试");
    }

    public MethodSynchronizedException(String s) {
        super(s);
    }

}
