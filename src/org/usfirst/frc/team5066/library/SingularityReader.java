package org.usfirst.frc.team5066.library;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SingularityReader { 
	
	private Properties props;
	private String propFileURL;
	
	public SingularityReader(String propFileURL) throws IOException {
		propFileURL = this.propFileURL;
		props = readProperties(propFileURL);
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
	
	public void setProperty(String propName, Object o) throws IOException{
		props.setProperty(propName, o.toString());
		//TODO save the file
		reloadProperties();
	}
	
}