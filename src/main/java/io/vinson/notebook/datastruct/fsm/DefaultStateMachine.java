package io.vinson.notebook.datastruct.fsm;

import io.vinson.notebook.datastruct.msg.Telegram;

/**
 * 状态机调度
 * @author: jiangweixin
 * @date: 2019/5/17
 */
public class DefaultStateMachine<E, S extends State<E>> implements StateMachine<E, S> {

	protected E owner;

	protected S currentState;

	protected S previousState;

	protected S globalState;

	public DefaultStateMachine() {
		this(null, null, null);
	}

	public DefaultStateMachine(E owner) {
		this(owner, null, null);
	}

	public DefaultStateMachine(E owner, S initialState) {
		this(owner, initialState, null);
	}

	public DefaultStateMachine(E owner, S initialState, S globalState) {
		this.owner = owner;
		this.setInitialState(initialState);
		this.setGlobalState(globalState);
	}

	public E getOwner() {
		return owner;
	}

	public void setOwner(E owner) {
		this.owner = owner;
	}

	@Override
	public void setInitialState(S state) {
		this.previousState = null;
		this.currentState = state;
	}

	@Override
	public void setGlobalState(S state) {
		this.globalState = state;
	}

	@Override
	public S getCurrentState() {
		return currentState;
	}

	@Override
	public S getGlobalState() {
		return globalState;
	}

	@Override
	public S getPreviousState() {
		return previousState;
	}

	@Override
	public void update() {
		if (globalState != null)
			globalState.update(owner);

		if (currentState != null)
			currentState.update(owner);
	}

	@Override
	public void changeState(S newState) {
		previousState = currentState;

		if (currentState != null)
			currentState.exit(owner);

		currentState = newState;

		if (currentState != null)
			currentState.enter(owner);
	}

	@Override
	public boolean revertToPreviousState() {
		if (previousState == null) {
			return false;
		}

		changeState(previousState);
		return true;
	}

	@Override
	public boolean isInState(S state) {
		return currentState == state;
	}

    @Override
	public boolean handleMessage(Telegram telegram) {
        if (currentState != null && currentState.onMessage(owner, telegram)) {
            return true;
        }

        if (globalState != null && globalState.onMessage(owner, telegram)) {
            return true;
        }

        return false;
	}
}
