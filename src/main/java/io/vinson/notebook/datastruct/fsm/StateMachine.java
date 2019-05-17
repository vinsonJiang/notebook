package io.vinson.notebook.datastruct.fsm;

import io.vinson.notebook.datastruct.msg.Telegram;
import io.vinson.notebook.datastruct.msg.Telegraph;

/**
 * 状态机接口
 * @author: jiangweixin
 * @date: 2019/5/16
 */
public interface StateMachine<E, S extends State<E>> extends Telegraph {

	public void update();

	public void changeState(S newState);

	public boolean revertToPreviousState();

	public void setInitialState(S state);

	public void setGlobalState(S state);

	public S getCurrentState();

	public S getGlobalState();

	public S getPreviousState();

	public boolean isInState(S state);

	public boolean handleMessage(Telegram telegram);
}
