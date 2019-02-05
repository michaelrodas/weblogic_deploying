package util;

import java.io.IOException;
import java.util.Properties;

public class ConfigPropertiesUtil {
	
	private static ConfigPropertiesUtil instance = null;
    private Properties properties;

    protected ConfigPropertiesUtil() throws IOException{
        properties = new Properties();
        properties.load(getClass().getResourceAsStream("/deployment.properties"));

    }
    
    public static ConfigPropertiesUtil getInstance() {
        if(instance == null) {
            try {
                instance = new ConfigPropertiesUtil();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return instance;
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }
}
