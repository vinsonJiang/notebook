package io.vinson.notebook.datastruct.fsm;


import io.vinson.notebook.datastruct.msg.Telegram;

/**
 * @author: jiangweixin
 * @date: 2019/5/16
 */
public interface State<E> {

	public void enter(E entity);

	public void update(E entity);

	public void exit(E entity);

	public boolean onMessage(E entity, Telegram telegram);
}
