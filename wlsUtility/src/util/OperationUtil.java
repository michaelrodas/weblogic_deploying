package util;

import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.TargetException;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;

public class OperationUtil {
	
	public static void validateInputData(String[] args) {
		if (args.length == 0) {
			System.err.print("Param 1: desired operation is missing. Available options are deploy and undeploy.");
			System.exit(-1);
		}
		if (!args[0].equalsIgnoreCase(ExecutionCommand.DEPLOY.name())
				&& !args[0].equalsIgnoreCase(ExecutionCommand.UNDEPLOY.name())) {
			System.err.print("Invalid operation. Available options are:");
			for (ExecutionCommand execCommand : ExecutionCommand.values()) {
				System.err.print(execCommand.name());
			}
			System.exit(-1);
		}
	}
	
	public static TargetModuleID getTargetModule(String applicationName, WebLogicDeploymentManager deployManager,
			Target[] deployTargets) throws TargetException {
		TargetModuleID[] targetModuleIDs = deployManager.getAvailableModules(ModuleType.EAR, deployTargets);
		TargetModuleID targetModule = null;
		for (TargetModuleID targetModuleID : targetModuleIDs) {
			if (targetModuleID.getModuleID().equals(applicationName)) {
				targetModule = targetModuleID;
				break;
			}
		}
		return targetModule;
	}
	
	public static void validateElapsedTime(long startTime) {
		long elapseTime = System.currentTimeMillis() - startTime;
		if(elapseTime >= Long.parseLong(ConfigPropertiesUtil.getInstance()
				.getValue(ConfigurationProperties.TIMEOUT_TIME.getName()))){
			throw new RuntimeException("Waiting time exceeded: " + (int) ((elapseTime / (1000*60)) % 60) + " minutes");
		}
	}
}
