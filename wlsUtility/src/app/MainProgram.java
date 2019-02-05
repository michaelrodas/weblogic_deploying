package app;

import utility.WeblogicDeployer;

public class MainProgram {

	public static void main(String[] args) {
		WeblogicDeployer deployer = new WeblogicDeployer();
		deployer.executeApplication(args);
	}
}
