
package io.vinson.notebook.datastruct.msg;

/** 
 * 消息管理
 *
 * @author: jiangweixin
 * @date: 2019/5/15
 */
public final class MessageManager extends MessageDispatcher {

	private static final MessageManager INSTANCE = new MessageManager();

	private MessageManager() {
	}

	public static MessageManager getInstance() {
		return INSTANCE;
	}

}
