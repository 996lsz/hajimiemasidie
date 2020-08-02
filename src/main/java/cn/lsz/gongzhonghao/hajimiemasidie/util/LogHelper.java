package cn.lsz.gongzhonghao.hajimiemasidie.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 必须配合LogAnnotation使用
 * 
 * @author LSZ 2020/07/21 15:58
 * @contact 648748030@qq.com
 */
public class LogHelper {

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private static ThreadLocal<List<Item>> threadLocal = new ThreadLocal<>();

	/*public static void debug(String var1){
		log(LogLevel.DEBUG, var1, null, null);
	}

	public static void debug(String var1, Object... var2){
		log(LogLevel.DEBUG, var1, var2, null);
	}

	public static void debug(String var1, Throwable var2){
		log(LogLevel.DEBUG, var1, null, var2);
	}*/

	public static void info(String var1){
		log(LogLevel.INFO, var1, null, null);
	}

	public static void info(String var1, Object... var2){
		log(LogLevel.INFO, var1, var2, null);
	}

	/*public static void info(String var1, Throwable var2){
		log(LogLevel.INFO, var1, null, var2);
	}

	public static void warn(String var1){
		log(LogLevel.WARN, var1, null, null);
	}

	public static void warn(String var1, Object... var2){
		log(LogLevel.WARN, var1, var2, null);
	}

	public static void warn(String var1, Throwable var2){
		log(LogLevel.WARN, var1, null, var2);
	}

	public static void error(String var1){
		log(LogLevel.ERROR, var1, null, null);
	}

	public static void error(String var1, Object... var2){
		log(LogLevel.ERROR, var1, var2, null);
	}

	public static void error(String var1, Throwable var2){
		log(LogLevel.ERROR, var1, null, var2);
	}*/


	private static void log(LogLevel logLevel, String logStr, Object[] args, Throwable throwable){
		Item item = new Item(logLevel, logStr, args, throwable);
		List<Item> items = threadLocal.get();
		if(items == null){
			items = new ArrayList<>();
			threadLocal.set(items);
		}
		items.add(item);
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class Item{

		private LogLevel level;

		private String logStr;

		private Object[] args;

		private Throwable throwable;
	}

	public enum LogLevel {
		DEBUG,
		INFO,
		WARN,
		ERROR;
	}

	public static List<Item> getLog(){
		return threadLocal.get();
	}

	public static void remove(){
		threadLocal.remove();
	}
}
