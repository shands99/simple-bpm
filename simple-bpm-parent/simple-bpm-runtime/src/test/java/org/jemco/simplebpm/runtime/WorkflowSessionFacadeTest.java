package org.jemco.simplebpm.runtime;

import org.jemco.simplebpm.action.DefaultActionExecutor;
import org.jemco.simplebpm.event.Event;
import org.jemco.simplebpm.event.EventHandler;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.execution.DefaultRamExecutionState;
import org.jemco.simplebpm.execution.ExecutionState;
import org.jemco.simplebpm.process.Process;
import org.jemco.simplebpm.process.ProcessImpl;
import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.process.SubProcessRole;
import org.jemco.simplebpm.process.SubProcessRoleImpl;
import org.jemco.simplebpm.runtime.WorkflowSession;
import org.jemco.simplebpm.runtime.WorkflowSessionFacade;
import org.junit.Assert;
import org.junit.Test;

public class WorkflowSessionFacadeTest {

	@Test
	public void test_simpleNonBlockingFlow() throws Exception {
		
		Process process = new ProcessImpl("test");
		State state = process.addStartState("start-state");
		State state2 = process.addTransition(state, "startToSecond", "second-state");
		State state3 = process.addTransition(state2, "secondToFinal", "third-state");
		state3.setEnd(true);
		
		WorkflowSession session = createTestSession(state);
		
		// should be at final state
		session.execute("startToSecond");
		Assert.assertEquals(state3, session.getExecutionState().getCurrentState());
		
	}
	
	@Test
	public void test_subProcess1() throws Exception {
		
		Process process = new ProcessImpl("test");
		State state = process.addStartState("start-state");
		State state2 = process.addTransition(state, "startToSecond", "second-state");
		State state3 = process.addTransition(state2, "secondToFinal", "third-state");
		state3.setEnd(true);
		
		Process subProcess = process.addSubProcess(state2, "subprocess");
		State subStart = subProcess.addStartState("sub-start");
		State subState1 = subProcess.addTransition(subStart, "subStartToEnd", "final");
		subState1.setEnd(true);
		
		WorkflowSession session = createTestSession(state, "1");
		session.getExecutionState().getChildren().add(new DefaultRamExecutionState("subprocess:1", subStart));
		
		// executes sub process
		session.execute("startToSecond");
		// post sub - process complete
		session.execute("secondToFinal");
		Assert.assertEquals(state3, session.getExecutionState().getCurrentState());
		
	}
	
	@Test
	public void test_subProcess2() throws Exception {
		
		//test blocking sub process
		Process process = new ProcessImpl("test");
		State state = process.addStartState("start-state");
		State state2 = process.addTransition(state, "startToSecond", "second-state");
		State state3 = process.addTransition(state2, "secondToFinal", "third-state");
		state3.setEnd(true);
		
		Process subProcess = process.addSubProcess(state2, "subprocess");
		State subStart = subProcess.addStartState("sub-start");
		State sub1 = subProcess.addTransition(subStart, "subStartTo1", "sub-second-state");
		sub1.setBlocking(true);
		State subStateEnd = subProcess.addTransition(sub1, "sub1ToEnd", "final");
		subStateEnd.setEnd(true);
		
		WorkflowSession session = createTestSession(state, "1");
		session.getExecutionState().getChildren().add(new DefaultRamExecutionState("subprocess:1", subStart));
		
		// executes sub process
		session.execute("startToSecond");
		// post sub - process complete
		session.execute("sub1ToEnd");
		//Assert.assertEquals(state2, session.getExecutionState().getCurrentState());
		//session.execute("secondToFinal");
		Assert.assertEquals(state3, session.getExecutionState().getCurrentState());
		
	}
	
	private WorkflowSession createTestSession(State start) {
		return this.createTestSession(start, null);
	}
	
	private WorkflowSession createTestSession(State start, String id) {
		ExecutionState context = new DefaultRamExecutionState(start);
		WorkflowSession session = new WorkflowSessionFacade(context, new DefaultActionExecutor(), null, new MockEventService(), id);
		return session;
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
