
package io.vinson.notebook.datastruct.msg;

/**
 * 提供額外信息接口
 * @author: jiangweixin
 * @date: 2019/5/15
 */
public interface TelegramProvider {
	Object provideMessageInfo(int msg, Telegraph receiver);
}
