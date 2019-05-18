package io.vinson.notebook.datastruct.fsm;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆栈式状态机，可依次返回之前状态
 * @author: jiangweixin
 * @date: 2019/5/17
 */
public class StackStateMachine<E, S extends State<E>> extends DefaultStateMachine<E, S> {

	private List<S> stateStack;

	public StackStateMachine() {
		this(null, null, null);
	}

	public StackStateMachine(E owner) {
		this(owner, null, null);
	}

	public StackStateMachine(E owner, S initialState) {
		this(owner, initialState, null);
	}

	public StackStateMachine(E owner, S initialState, S globalState) {
		super(owner, initialState, globalState);
	}

	@Override
	public void setInitialState(S state) {
		if (stateStack == null) {
			stateStack = new ArrayList<S>();
		}

		this.stateStack.clear();
		this.currentState = state;
	}

	@Override
	public S getCurrentState() {
		return currentState;
	}

	@Override
	public S getPreviousState() {
		if (stateStack.size() == 0) {
			return null;
		} else {
			return stateStack.get(stateStack.size() - 1);
		}
	}

	@Override
	public void changeState(S newState) {
		changeState(newState, true);
	}

	@Override
	public boolean revertToPreviousState() {
		if (stateStack.size() == 0) {
			return false;
		}

		S previousState = stateStack.remove(stateStack.size() - 1);
		changeState(previousState, false);
		return true;
	}

	private void changeState(S newState, boolean pushCurrentStateToStack) {
		if (pushCurrentStateToStack && currentState != null) {
			stateStack.add(currentState);
		}
		if (currentState != null)
			currentState.exit(owner);

		currentState = newState;
		currentState.enter(owner);
	}

}
