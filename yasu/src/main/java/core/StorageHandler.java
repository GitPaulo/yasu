package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths; 
import java.nio.file.StandardOpenOption;
import org.json.JSONException;
import org.json.JSONObject;

public class StorageHandler {
	private String path;
	private File file;
	private String extension;

	public StorageHandler(String p) {
		this.path = p;
		this.file = new File(this.path);
		try {
			if (this.file.createNewFile()) {
				System.out.println("Created new file:" + this.path);
			} else {
				System.out.println("Loaded:" + this.path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.extension = "";

		int i = this.path.lastIndexOf('.');
		if (i > 0) {
			this.extension = this.path.substring(i + 1);
		}
	}

	public void appendToTextFile(String str) {
		if (!this.extension.equals("txt")) {
			throw new Error("Incorrect file type for this instance! Must be .txt");
		}
		str = str + System.getProperty("line.separator");
		try {
			Files.write(Paths.get(this.path, new String[0]), str.getBytes(),
					new OpenOption[] { StandardOpenOption.APPEND });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveToTextFile(String data) {
		if (!this.extension.equals("txt")) {
			throw new Error("Incorrect file type for this instance! Must be .txt");
		}
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream(this.file, false);

			byte[] contentInBytes = data.getBytes();

			oFile.write(contentInBytes);
			oFile.flush();
			oFile.close();
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oFile != null) {
					oFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getAllFromFile() {
		Path fileLocation = Paths.get(this.path, new String[0]);
		byte[] data = null;
		try {
			data = Files.readAllBytes(fileLocation);
		} catch (IOException e) {
			System.out.println("Could not read file: " + fileLocation);
			throw new RuntimeException(e);
		}
		return new String(data);
	}

	public void saveJSONData(JSONObject o) {
		if (!this.extension.equals("json")) {
			throw new Error("Incorrect file type for this instance! Must be JSON");
		}
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream(this.file, false);
			String data = o.toString();
			byte[] contentInBytes = data.getBytes();

			oFile.write(contentInBytes);
			oFile.flush();
			oFile.close();
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oFile != null) {
					oFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONObject getJSONData() {
		if (!this.extension.equals("json")) {
			throw new Error("Incorrect file type for this instance! Must be JSON.");
		}
		String text = getAllFromFile();
		JSONObject json = null;
		try {
			json = new JSONObject(text);
		} catch (JSONException e) {
			Logger.info(new Object[] { "JSON Data was empty on load. Should be first time!" });
		}
		return json;
	}

	public String getExtension() {
		return this.extension;
	}
}
