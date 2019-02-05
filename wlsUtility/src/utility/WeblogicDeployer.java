package utility;

import java.util.Arrays;

import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import util.ConfigPropertiesUtil;
import util.ConfigurationProperties;
import util.ExecutionCommand;
import util.OperationUtil;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.tools.SessionHelper;

public class WeblogicDeployer {

	private static final Logger logger = Logger.getLogger(WeblogicDeployer.class);

	public void executeApplication(String args[]){
		OperationUtil.validateInputData(args);

		ExecutionCommand executionCommand = ExecutionCommand.getCommand(args[0]);

		String consoleManagerPath = ConfigPropertiesUtil.getInstance()
				.getValue(ConfigurationProperties.CONSOLE_MANAGER_PATH.getName());
		String projectName = ConfigPropertiesUtil.getInstance()
				.getValue(ConfigurationProperties.PROJECT_NAME.getName());
		String hostName = ConfigPropertiesUtil.getInstance().getValue(ConfigurationProperties.HOSTNAME.getName());
		String port = ConfigPropertiesUtil.getInstance().getValue(ConfigurationProperties.PORT.getName());
		String adminUser = ConfigPropertiesUtil.getInstance().getValue(ConfigurationProperties.ADMIN_USER.getName());
		String adminPassword = ConfigPropertiesUtil.getInstance()
				.getValue(ConfigurationProperties.ADMIN_PASSWORD.getName());
		String earLocation = consoleManagerPath + "/projects/" + projectName + "/JEE/dist/BizAgi-ear-Weblogic.ear";
		String applicationName = "BizAgi-ear-Weblogic";		
		
		DeploymentVO deploymentVO = new DeploymentVO();
		
		logger.log(Level.INFO, "\n\t About to connect to WLS...");
		try {
			prepareExecutionData(hostName, port, adminUser, adminPassword, earLocation, applicationName, deploymentVO);

			Context context = new Context();
			context.execute(executionCommand, deploymentVO);
		} catch (Throwable ex) {
			logger.log(Level.ERROR, "There was an error during the execution of the operation \n\t", ex);
			ex.printStackTrace();
		}

		System.exit(0);
	}

	private static void prepareExecutionData(String hostName, String port, String adminUser, String adminPassword,
			String earLocation, String applicationName, DeploymentVO deploymentVO)
			throws DeploymentManagerCreationException {
		boolean isCluster = false;
		
		WebLogicDeploymentManager deploymentManager = SessionHelper.getRemoteDeploymentManager("t3", hostName, port,
				adminUser, adminPassword);
		logger.log(Level.INFO, "\n\t Domain name: " + deploymentManager.getDomain());

		DeploymentOptions options = new DeploymentOptions();
		options.setName(applicationName);

		Target targets[] = deploymentManager.getTargets();
		Target deployTargets[] = new Target[1];
		logger.log(Level.INFO, "\n\t The application can be deployed in the following targets: " + Arrays.toString(targets));
		for (int i = 0; i < targets.length; i++) {
			if(targets[i].toString().contains("cluster")){
				isCluster = true;
				deployTargets[0] = targets[i];
			}
		}
		
		if (!isCluster) {
			deployTargets[0] = targets[0];
			logger.log(Level.INFO, "\n\t WLS has a StandAlone configuration: " + deployTargets[0]);
		} else {
			logger.log(Level.INFO, "\n\t WLS has a Cluster configuration: \n\t" + deployTargets[0]);
		}

		deploymentVO.setApplicationName(applicationName);
		deploymentVO.setEarLocation(earLocation);
		deploymentVO.setDeploymentManager(deploymentManager);
		deploymentVO.setOptions(options);
		deploymentVO.setDeployTargets(deployTargets);
	}
}