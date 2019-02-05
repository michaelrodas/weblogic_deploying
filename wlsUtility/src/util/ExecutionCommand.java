package util;

public enum ExecutionCommand {
	DEPLOY, 
	UNDEPLOY;

	public static ExecutionCommand getCommand(String command) {
		ExecutionCommand resultingCommand = null;
		for (ExecutionCommand execCommand : ExecutionCommand.values()) {
			if (execCommand.name().equalsIgnoreCase(command)) {
				resultingCommand = execCommand;
			}
		}
		return resultingCommand;
	}
}
