package org.usfirst.frc.team5066.library;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SingularityPropsReader { 
	
	//each SingularityPropsReader object is associated with one properties file.
	
	private Properties props;
	private String propFileURL;
	
	public SingularityPropsReader(String propFileURL) throws IOException {
		propFileURL = this.propFileURL;
		props = readProperties(propFileURL);
	}
	
	public String getFileURL() {
		return propFileURL;
	}
	
	public void reloadProperties() throws IOException {
		props = readProperties(propFileURL);
	}
	
	//properties file creation
	private Properties readProperties(String propFileURL) throws IOException{
		
		Properties prop = new Properties();
		String propURL = propFileURL;
		FileInputStream fileInputStream;
		
		//loads properties file. Tutorial found on : http://stackoverflow.com/questions/8775303/read-properties-file-outside-jar-file
		fileInputStream = new FileInputStream(propURL);
		prop.load(fileInputStream);
		fileInputStream.close();
		
		return prop;
	}
	
	//setProperty method
	
	public void setProperty(String propName, Object o) throws IOException{
		
		//TODO test to see if this works
		
		FileOutputStream out = new FileOutputStream(propFileURL);
		props.setProperty(propName, o.toString());
		props.store(out, null);
		out.close();
		reloadProperties();
	}
	
	//getProperty methods
	
	public int getInt(String name) {
		return Integer.parseInt(props.getProperty("name"));
	}
	
	public float getFloat(String name) {
		return Float.parseFloat(props.getProperty("name"));
	}
	
	public double getDouble(String name) {
		return Double.parseDouble(props.getProperty("name"));
	}
	
	public String getString(String name) {
		return props.getProperty("name");
	}
	
	public boolean getBoolean(String name) {
		return Boolean.parseBoolean(props.getProperty("name"));
	}
	
	
}
