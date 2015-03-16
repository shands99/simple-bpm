package org.jemco.simplebpm.runtime.components;

import java.util.Arrays;
import java.util.List;

import org.jemco.simplebpm.DefaultWorkflowService;
import org.jemco.simplebpm.WorkflowService;
import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.action.DefaultActionExecutor;
import org.jemco.simplebpm.event.Event;
import org.jemco.simplebpm.event.EventHandler;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.execution.DefaultExecutionStateService;
import org.jemco.simplebpm.execution.DefaultRamExecutionState;
import org.jemco.simplebpm.execution.ExecutionState;
import org.jemco.simplebpm.execution.ExecutionStateService;
import org.jemco.simplebpm.process.Process;
import org.jemco.simplebpm.process.ProcessImpl;
import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.registry.SimpleHashMapRegistry;
import org.jemco.simplebpm.registry.Registry;
import org.jemco.simplebpm.runtime.DefaultContext;
import org.jemco.simplebpm.runtime.SessionUtils;
import org.jemco.simplebpm.runtime.WorkflowSession;
import org.jemco.simplebpm.runtime.WorkflowSessionImpl;
import org.junit.Assert;
import org.junit.Test;

public class SubProcessNodeComponentTest {

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
		
		WorkflowSession session = createTestSession(state, "1", process);
		//session.getExecutionState().getChildren().add(new DefaultRamExecutionState("subprocess:1", subStart));
		
		// executes sub process
		session.execute("startToSecond");		
		Assert.assertEquals(state3, SessionUtils.getCurrentNode(session));
		
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
		
		WorkflowSession session = createTestSession(state, "1", process);
		//session.getExecutionState().getChildren().add(new DefaultRamExecutionState("subprocess:1", subStart));
		
		// executes sub process
		session.execute("startToSecond");
		Assert.assertEquals(state2, SessionUtils.getCurrentNode(session));
		
		// post sub - process complete
		session.execute("sub1ToEnd");
		Assert.assertEquals(state3, SessionUtils.getCurrentNode(session));
		
	}
	
	private WorkflowSession createTestSession(State start, String id, Process process) {
		
		ExecutionState context = new DefaultRamExecutionState(id, start);
		
		SubProcessNodeComponent subComponent = new SubProcessNodeComponent();
		
		ExecutionStateService executionStateService = new DefaultExecutionStateService();
		DefaultWorkflowService workflowService = new DefaultWorkflowService();
		ActionExecutor executor = new DefaultActionExecutor();
		EventService eventService = new MockEventService();
		
		Registry registry = new SimpleHashMapRegistry();
		registry.add(executionStateService);
		registry.add(workflowService);
		registry.add(executor);
		registry.add(eventService);
		
		subComponent.setRegistry(registry);
		subComponent.loadService();
		workflowService.setRegistry(registry);
		workflowService.loadService();
		
		List<NodeComponent> components = Arrays.asList(new NodeComponent[]{subComponent});
		WorkflowSession session = workflowService.newSession(process, new DefaultContext(null), context);
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
