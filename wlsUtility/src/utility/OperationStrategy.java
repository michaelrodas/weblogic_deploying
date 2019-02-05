package utility;

import javax.enterprise.deploy.spi.exceptions.TargetException;

public interface OperationStrategy {

	void execute(DeploymentVO deploymentVO) throws InterruptedException, TargetException;
}
