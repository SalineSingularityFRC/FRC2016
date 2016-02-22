package org.usfirst.frc.team5066.library;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class for reading and writing to properties files.
 * 
 * @author Saline Singularity 5066
 *
 */
public class SingularityProperties {
	private Properties props;
	private String propFileURL;

	/**
	 * Constructor for SingularityPropsReader
	 * 
	 * @param propFileURL
	 *            Which file to use as the properties file
	 * @throws IOException
	 *             If the file is invalid or lacks read access
	 */
	public SingularityProperties(String propFileURL) throws IOException {
		this.propFileURL = propFileURL;
		props = readProperties(propFileURL);
	}

	/**
	 * Used to get the file URL
	 * 
	 * @return Which file is being read
	 */
	public String getFileURL() {
		return propFileURL;
	}

	/**
	 * Rereads file properties
	 * 
	 * @throws IOException
	 *             If the file is invalid or lacks read access
	 */
	public void reloadProperties() throws IOException {
		props = readProperties(propFileURL);
	}

	/**
	 * Actually reads the property file
	 * 
	 * @param propFileURL
	 *            Which file to read
	 * @return An object which encapsulates the properties found in the given
	 *         file
	 * @throws IOException
	 *             If the file lacks read access
	 */
	private Properties readProperties(String propFileURL) throws IOException {
		Properties prop = new Properties();
		String propURL = propFileURL;
		FileInputStream fileInputStream;

		fileInputStream = new FileInputStream(propURL);
		prop.load(fileInputStream);
		fileInputStream.close();

		return prop;
	}

	/**
	 * Sets a certain property in the file to a certain object
	 * 
	 * @param propName
	 *            Which property to change
	 * @param o
	 *            What to change it to (uses the {@code .toString()} method)
	 * @throws IOException
	 *             If file is not valid or does not allow write access
	 */
	public void setProperty(String propName, Object o) throws IOException {
		FileOutputStream out = new FileOutputStream(propFileURL);
		
		props.setProperty(propName, o.toString());
		props.store(out, null);
		out.close();
		reloadProperties();
	}

	/**
	 * Method to find access a certain string in the properties file
	 * 
	 * @param name
	 *            Which string to get
	 * @return The value of the string
	 */
	public String getString(String name) {
		return props.getProperty("name");
	}

	/**
	 * Method to find access a certain integer in the properties file
	 * 
	 * @param name
	 *            Which integer to get
	 * @return The value of the integer
	 */
	public int getInt(String name) {
		return Integer.parseInt(props.getProperty("name"));
	}

	/**
	 * Method to find access a certain float in the properties file
	 * 
	 * @param name
	 *            Which float to get
	 * @return The value of the float
	 */
	public float getFloat(String name) {
		return Float.parseFloat(props.getProperty("name"));
	}

	/**
	 * Method to find access a certain double in the properties file
	 * 
	 * @param name
	 *            Which double to get
	 * @return The value of the double
	 */
	public double getDouble(String name) {
		return Double.parseDouble(props.getProperty("name"));
	}

	/**
	 * Method to find access a certain boolean in the properties file
	 * 
	 * @param name
	 *            Which boolean to get
	 * @return The value of the boolean
	 */
	public boolean getBoolean(String name) {
		return Boolean.parseBoolean(props.getProperty("name"));
	}
}
