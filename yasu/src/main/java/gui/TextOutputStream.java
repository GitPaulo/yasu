package gui;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

public class TextOutputStream extends OutputStream {
	private JTextArea textControl;

	public TextOutputStream(JTextArea control) {
		this.textControl = control;
	}

	public void write(int b) throws IOException {
		this.textControl.append(String.valueOf((char) b));
	}
}
