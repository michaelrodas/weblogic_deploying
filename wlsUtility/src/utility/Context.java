package utility;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.deploy.spi.exceptions.TargetException;

import util.ExecutionCommand;

public class Context {
	private static Map<ExecutionCommand, OperationStrategy> strategies = new HashMap<ExecutionCommand, OperationStrategy>();

	public Context() {
		strategies.put(ExecutionCommand.DEPLOY, new DeployOperation());
		strategies.put(ExecutionCommand.UNDEPLOY, new UndeployOperation());
	}

	public void execute(ExecutionCommand command, DeploymentVO deploymentVO)
			throws InterruptedException, TargetException {
		strategies.get(command).execute(deploymentVO);
	}
}
