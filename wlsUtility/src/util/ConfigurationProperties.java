package util;

public enum ConfigurationProperties {

	CONSOLE_MANAGER_PATH("consoleManagerPath"), 
	PROJECT_NAME("projectName"), 
	HOSTNAME("hostName"), 
	PORT("port"),
	ADMIN_USER("adminUser"),
	ADMIN_PASSWORD("adminPassword"),
	DEPLOYMENT_WAIT_TIME("deploymentWaitTime"),
	TIMEOUT_TIME("timeoutTime");
	
	private String property;

	ConfigurationProperties(String property) {
		this.property = property;
	}

	public String getName() {
		return property;
	}
}
