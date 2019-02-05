package utility;

import javax.enterprise.deploy.spi.Target;

import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;

public class DeploymentVO {
	private String earLocation;
	private String applicationName;
	private WebLogicDeploymentManager deploymentManager;
	private DeploymentOptions options;
	private Target[] deployTargets;

	public String getEarLocation() {
		return earLocation;
	}

	public void setEarLocation(String earLocation) {
		this.earLocation = earLocation;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public WebLogicDeploymentManager getDeploymentManager() {
		return deploymentManager;
	}

	public void setDeploymentManager(WebLogicDeploymentManager deploymentManager) {
		this.deploymentManager = deploymentManager;
	}

	public DeploymentOptions getOptions() {
		return options;
	}

	public void setOptions(DeploymentOptions options) {
		this.options = options;
	}

	public Target[] getDeployTargets() {
		return deployTargets;
	}

	public void setDeployTargets(Target[] deployTargets) {
		this.deployTargets = deployTargets;
	}
}
