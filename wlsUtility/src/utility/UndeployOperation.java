package utility;

import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.TargetException;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressObject;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import util.ConfigPropertiesUtil;
import util.ConfigurationProperties;
import util.OperationUtil;

public class UndeployOperation implements OperationStrategy {
	private static final Logger logger = Logger.getLogger(UndeployOperation.class);

	@Override
	public void execute(DeploymentVO deploymentVO) throws InterruptedException, TargetException {
		undeployApp(deploymentVO);
	}

	private void undeployApp(DeploymentVO deploymentVO) throws TargetException, InterruptedException {
		TargetModuleID targetModule = OperationUtil.getTargetModule(deploymentVO.getApplicationName(),
				deploymentVO.getDeploymentManager(), deploymentVO.getDeployTargets());
		ProgressObject processStatus = null;
		long startTime = System.currentTimeMillis();
		try {
			processStatus = deploymentVO.getDeploymentManager().undeploy(new TargetModuleID[] { targetModule });
		} catch (NullPointerException ex) {
			throw new NullPointerException("The application is not deployed on server.");
		}

		DeploymentStatus deploymentStatus = processStatus.getDeploymentStatus();
		while (!deploymentStatus.isCompleted()) {
			logger.log(Level.INFO, "\n\t Waiting for the undeployment process to get completed successfully.");
			Thread.sleep(Integer.parseInt(ConfigPropertiesUtil.getInstance()
					.getValue(ConfigurationProperties.DEPLOYMENT_WAIT_TIME.getName())));
			deploymentStatus = processStatus.getDeploymentStatus();
			OperationUtil.validateElapsedTime(startTime);
		}
		
		logger.log(Level.INFO, "\n\t The application was successfully removed.");
	}
}
