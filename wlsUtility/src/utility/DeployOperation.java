package utility;

import java.io.File;

import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.TargetException;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressObject;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import util.ConfigPropertiesUtil;
import util.ConfigurationProperties;
import util.OperationUtil;

public class DeployOperation implements OperationStrategy {
	private static final Logger logger = Logger.getLogger(DeployOperation.class);

	@Override
	public void execute(DeploymentVO deploymentVO) throws InterruptedException, TargetException {
		deployApp(deploymentVO);
	}

	private void deployApp(DeploymentVO deploymentVO) throws InterruptedException, TargetException {
		TargetModuleID targetModule = getTargetModule(deploymentVO);
		TargetModuleID[] runningModules = deploymentVO.getDeploymentManager().getRunningModules(ModuleType.EAR, deploymentVO.getDeployTargets());
		ProgressObject processStatus = null;
		boolean redeployApp = evaluateRedeployOperation(targetModule, runningModules);
		
		long startTime = System.currentTimeMillis();
		if(redeployApp){
			processStatus = deploymentVO.getDeploymentManager().redeploy(new TargetModuleID[] { targetModule },
					new File(deploymentVO.getEarLocation()), null);
		}else {
			processStatus = deploymentVO.getDeploymentManager().distribute(deploymentVO.getDeployTargets(),
					new File(deploymentVO.getEarLocation()), null, deploymentVO.getOptions());
		}
		
		DeploymentStatus deploymentStatus = processStatus.getDeploymentStatus();

		while (!deploymentStatus.isCompleted()) {
			logger.log(Level.INFO, "\n\t Waiting for the deployment activation to get completed successfully.");
			Thread.sleep(Integer.parseInt(ConfigPropertiesUtil.getInstance()
					.getValue(ConfigurationProperties.DEPLOYMENT_WAIT_TIME.getName())));
			deploymentStatus = processStatus.getDeploymentStatus();
			OperationUtil.validateElapsedTime(startTime);
		}		

		targetModule = getTargetModule(deploymentVO);
		
		processStatus = deploymentVO.getDeploymentManager().start(new TargetModuleID[] { targetModule });
		deploymentStatus = processStatus.getDeploymentStatus();
		
		while (!deploymentStatus.isCompleted()) {
			logger.log(Level.INFO, "\n\t Waiting for the application to get started.");
			Thread.sleep(Integer.parseInt(ConfigPropertiesUtil.getInstance()
					.getValue(ConfigurationProperties.DEPLOYMENT_WAIT_TIME.getName())));
			deploymentStatus = processStatus.getDeploymentStatus();
		}
		
		logger.log(Level.INFO, "\n\t The application is ready to be used.");
	}

	private TargetModuleID getTargetModule(DeploymentVO deploymentVO) throws TargetException {
		return OperationUtil.getTargetModule(deploymentVO.getApplicationName(),
				deploymentVO.getDeploymentManager(), deploymentVO.getDeployTargets());
	}

	private boolean evaluateRedeployOperation(TargetModuleID targetModule, TargetModuleID[] runningModules) {
		boolean redeployApp = false;
		for (TargetModuleID targetModuleID : runningModules) {
			if (targetModuleID.equals(targetModule)) {
				redeployApp = true; 				
			}
		}
		return redeployApp;
	}

	
}
