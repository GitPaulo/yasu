package utilities;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public final class AsciiArt {
	public static String useAPI(String input, String font) {
		input = input.trim().replace(' ', '+');
		String link = "http://artii.herokuapp.com/make?text="+input+"&font="+font;
		System.out.printf("input: %s font: %s url: %s %n", input, font, link);
	
		Document doc = null;
		
		try {
			doc = Jsoup.connect(link).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		Element output = doc.body(); 
		System.out.println(output.html());
		OutputSettings settings = new OutputSettings();
		
		settings.prettyPrint(false);
		return Jsoup.clean(output.html(), "", Whitelist.none(), settings);
	}
	
	public static String artify( String input, Font font ) throws IOException {
		input = input.trim();
		BufferedImage image = new BufferedImage(144, 32, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setFont(font);
		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.drawString(input, 6, 24);
		ImageIO.write(image, "png", new File("artify.png"));
		
		StringBuilder sbout = new StringBuilder();
		for (int y = 0; y < 32; y++) {
		    StringBuilder sb = new StringBuilder();
		    for (int x = 0; x < 144; x++)
		        sb.append(image.getRGB(x, y) == -16777216 ? " " : image.getRGB(x, y) == -1 ? "#" : "*");
		    if (sb.toString().trim().isEmpty()) continue;
		    sbout.append(sb+"\n");
		}
		
		return "```" + sbout.toString() + "```";
	}
}
