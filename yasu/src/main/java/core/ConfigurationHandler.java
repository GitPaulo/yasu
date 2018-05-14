package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationHandler {
	private final String path;
	private final File configFile;
	private boolean isNew;

	public ConfigurationHandler() {
		this.path = "yasu_config.properties";
		this.configFile = new File(this.path);
		this.isNew = (!this.configFile.exists());
		try {
			if (this.configFile.createNewFile()) {
				System.out.println("Created new file:" + this.path);
			} else {
				System.out.println("Loaded:" + this.path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getValue(String key) {
		String data = null;
		FileReader reader = null;
		try {
			reader = new FileReader(this.configFile);
			Properties props = new Properties();
			props.load(reader);

			return props.getProperty(key);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public void setValue(String key, String value) {
		FileReader reader = null;
		FileWriter writer = null;
		try {
			reader = new FileReader(this.configFile);
			writer = new FileWriter(this.configFile);
			Properties props = new Properties();
			props.load(reader);
			props.setProperty(key, value);
			props.store(writer, "Value set at X date");
			return;
		} catch (FileNotFoundException ex) {
			ex.getStackTrace();
		} catch (IOException ex) {
			ex.getStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isNew() {
		return this.isNew;
	}
}
