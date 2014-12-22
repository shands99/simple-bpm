package org.jemco.simplebpm;

import org.junit.Assert;
import org.junit.Test;

import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.event.Event;
import org.jemco.simplebpm.event.EventHandler;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.runtime.ActionState;
import org.jemco.simplebpm.runtime.State;
import org.jemco.simplebpm.runtime.StateImpl;
import org.jemco.simplebpm.runtime.StateTransition;
import org.jemco.simplebpm.runtime.StateTransitionImpl;
import org.jemco.simplebpm.runtime.execution.ExecutionContext;
import org.jemco.simplebpm.runtime.execution.RamExecutionContext;

public class WorkflowSessionImplTest {

	@Test
	public void test_simpleBlockingFlow() throws Exception {
		
		State state = new StateImpl(false, false, "start-state");
		State state2 = new StateImpl(false, true, "second-state");
		State state3 = new StateImpl(true, false, "third-state");
		
		StateTransition transition1 = new StateTransitionImpl("startToSecond", state, state2);
		StateTransition transition2 = new StateTransitionImpl("secondToFinal", state2, state3);
		
		state.addTransition(transition1);
		state2.addTransition(transition2);
		
		ExecutionContext context = new RamExecutionContext(state);
		WorkflowSession session = new WorkflowSessionImpl(context, new MockActionExecution(), null, new MockEventService());
		
		session.execute("startToSecond");
		Assert.assertEquals(state2, session.getExecutionState().getCurrentState());
		session.execute("secondToFinal");
		Assert.assertEquals(state3, session.getExecutionState().getCurrentState());
	}
	
	@Test(expected = WorkflowException.class)
	public void test_invalidTransition() throws Exception {
		
		State state = new StateImpl(false, false, "start-state");
		State state2 = new StateImpl(false, true, "second-state");
		State state3 = new StateImpl(true, false, "third-state");
		
		StateTransition transition1 = new StateTransitionImpl("startToSecond", state, state2);
		StateTransition transition2 = new StateTransitionImpl("secondToFinal", state2, state3);
		
		state.addTransition(transition1);
		state2.addTransition(transition2);
		
		ExecutionContext context = new RamExecutionContext(state);
		WorkflowSession session = new WorkflowSessionImpl(context, new MockActionExecution(), null, new MockEventService());
		
		session.execute("secondToFinal");
		
	}
	
	@Test
	public void test_simpleNonBlockingFlow() throws Exception {
		
		State state = new StateImpl(false, false, "start-state");
		State state2 = new StateImpl(false, false, "second-state");
		State state3 = new StateImpl(true, false, "third-state");
		
		StateTransition transition1 = new StateTransitionImpl("startToSecond", state, state2);
		StateTransition transition2 = new StateTransitionImpl("secondToFinal", state2, state3);
		
		state.addTransition(transition1);
		state2.addTransition(transition2);
		
		ExecutionContext context = new RamExecutionContext(state);
		WorkflowSession session = new WorkflowSessionImpl(context, new MockActionExecution(), null, new MockEventService());
		
		// should be at final state
		session.execute("startToSecond");
		Assert.assertEquals(state3, session.getExecutionState().getCurrentState());
		
	}
	
	private class MockActionExecution implements ActionExecutor {

		@Override
		public void executeActions(ActionState state) {
			
			
			
		}
		
	}
	
	private class MockEventService implements EventService {

		@Override
		public void addHandler(EventHandler handler) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void raiseEvent(Event event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
