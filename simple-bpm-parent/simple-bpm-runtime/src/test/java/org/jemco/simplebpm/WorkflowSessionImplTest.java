package org.jemco.simplebpm;

import org.junit.Assert;
import org.junit.Test;
import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.action.Phase;
import org.jemco.simplebpm.event.Event;
import org.jemco.simplebpm.event.EventHandler;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.runtime.ActionExecutorRole;
import org.jemco.simplebpm.runtime.Process;
import org.jemco.simplebpm.runtime.ProcessImpl;
import org.jemco.simplebpm.runtime.State;
import org.jemco.simplebpm.runtime.execution.ExecutionContext;
import org.jemco.simplebpm.runtime.execution.RamExecutionContext;

public class WorkflowSessionImplTest {

	@Test
	public void test_simpleBlockingFlow() throws Exception {
		
		Process process = new ProcessImpl("test");
		State state = process.addStartState("start-state");
		State state2 = process.addTransition(state, "startToSecond", "second-state");
		state2.setBlocking(true);
		State state3 = process.addTransition(state2, "secondToFinal", "third-state");
		state3.setEnd(true);
		
		ExecutionContext context = new RamExecutionContext(state);
		WorkflowSession session = new WorkflowSessionImpl(context, new MockActionExecution(), null, new MockEventService());
		
		session.execute("startToSecond");
		Assert.assertEquals(state2, session.getExecutionState().getCurrentState());
		session.execute("secondToFinal");
		Assert.assertEquals(state3, session.getExecutionState().getCurrentState());
	}
	
	@Test
	public void test_multiBlockingFlow() throws Exception {
		
		Process process = new ProcessImpl("test");
		State state = process.addStartState("start-state");
		State state2 = process.addTransition(state, "startToSecond", "second-state");
		state2.setBlocking(true);
		State state2a = process.addTransition(state, "startToA", "2a-state");
		state2a.setBlocking(true);
		State state3 = process.addTransition(state2, "secondToFinal", "third-state");
		state3.setEnd(true);
		
		state3 = process.addTransition(state2a, "2aToFinal", "third-state");
		
		ExecutionContext context = new RamExecutionContext(state);
		WorkflowSession session = new WorkflowSessionImpl(context, new MockActionExecution(), null, new MockEventService());
		
		session.execute("startToSecond");
		Assert.assertEquals(state2, session.getExecutionState().getCurrentState());
		session.execute("secondToFinal");
		Assert.assertEquals(state3, session.getExecutionState().getCurrentState());
	}
	
	@Test(expected = WorkflowException.class)
	public void test_invalidTransition() throws Exception {
		
		Process process = new ProcessImpl("test");
		State state = process.addStartState("start-state");
		State state2 = process.addTransition(state, "startToSecond", "second-state");
		state2.setBlocking(true);
		State state3 = process.addTransition(state2, "secondToFinal", "third-state");
		state3.setEnd(true);
		
		ExecutionContext context = new RamExecutionContext(state);
		WorkflowSession session = new WorkflowSessionImpl(context, new MockActionExecution(), null, new MockEventService());
		
		session.execute("secondToFinal");
		
	}
	
	@Test(expected = WorkflowException.class)
	public void test_atFinalState() throws Exception {
		
		Process process = new ProcessImpl("test");
		State state = process.addStartState("start-state");
		State state2 = process.addTransition(state, "startToSecond", "second-state");
		State state3 = process.addTransition(state2, "secondToFinal", "third-state");
		state3.setEnd(true);
		
		ExecutionContext context = new RamExecutionContext(state);
		WorkflowSession session = new WorkflowSessionImpl(context, new MockActionExecution(), null, new MockEventService());
		
		session.execute("startToSecond");
		session.execute("secondToFinal");
	}
	
	@Test
	public void test_simpleNonBlockingFlow() throws Exception {
		
		Process process = new ProcessImpl("test");
		State state = process.addStartState("start-state");
		State state2 = process.addTransition(state, "startToSecond", "second-state");
		State state3 = process.addTransition(state2, "secondToFinal", "third-state");
		state3.setEnd(true);
		
		ExecutionContext context = new RamExecutionContext(state);
		WorkflowSession session = new WorkflowSessionImpl(context, new MockActionExecution(), null, new MockEventService());
		
		// should be at final state
		session.execute("startToSecond");
		Assert.assertEquals(state3, session.getExecutionState().getCurrentState());
		
	}
	
	private class MockActionExecution implements ActionExecutor {

		@Override
		public void executeActions(ActionExecutorRole state,
				WorkflowSession session, Phase phase) {
			// TODO Auto-generated method stub
			
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
