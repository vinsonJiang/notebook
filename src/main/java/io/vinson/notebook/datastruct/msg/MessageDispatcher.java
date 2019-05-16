
package io.vinson.notebook.datastruct.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息分发管理器
 * @author: jiangweixin
 * @date: 2019/5/15
 */
public class MessageDispatcher implements Telegraph {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageDispatcher.class);

	@Override
	public boolean handleMessage(Telegram msg) {
		return false;
	}
}
