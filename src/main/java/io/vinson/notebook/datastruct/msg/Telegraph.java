package io.vinson.notebook.datastruct.msg;

/**
 * @author: jiangweixin
 * @date: 2019/5/15
 */
public interface Telegraph {

	public boolean handleMessage(Telegram msg);

}
