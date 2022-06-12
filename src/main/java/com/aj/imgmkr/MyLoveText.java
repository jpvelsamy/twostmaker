package com.aj.imgmkr;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import ij.ImagePlus;

/**
 * https://www.baeldung.com/java-add-text-to-image
 * https://imagej.nih.gov/ij/links.html
 * 
 * @author jpvel
 *
 */
public class MyLoveText {

	@Test
	public void createImage() throws IOException {
		
		String imagePath = this.getClass().getResource("512px-Hinweiszeichen_17a_empty.jpg").getPath();
		String text1 = "Sweetheart - nothing that I do ";
		String text2 = "is possible without you my luv...";
		String text3 = "YOU is often all that’s needed";
		String text4 = "to keep my hope & dreams alive.";
		String text5 = "Love you so much my dear";
		
		List<String> textList = new ArrayList<>();
		textList.add(text1);
		textList.add(text2);
		textList.add(text3);
		textList.add(text4);
		textList.add(text5);
		
		File sourceFile = new File(imagePath);		
		BufferedImage image = ImageIO.read(sourceFile);
		Graphics g = image.getGraphics();
		Font font = createFontToFit(new Font("Ubuntu", Font.BOLD, 80), text1, image);	
		FontMetrics metrics = g.getFontMetrics(font);
				
		int x = getX(image, text1, metrics);
		int y = getY(image, metrics)-60;
		for (Iterator<String> iterator = textList.iterator(); iterator.hasNext();) {
			String text = (String) iterator.next();			
			AttributedString attributedText1 = createAttributedText(text, font);
			g.drawString(attributedText1.getIterator(), x, y);
			y=y+35;
		}
		
		ImagePlus resultGraphicsCentered = new ImagePlus("", image);
		resultGraphicsCentered.show();
		String nextFile = "/home/jpvel/"+"output.jpg";
		File nextFilePath = new File(nextFile);
		ImageIO.write(image, "jpg", nextFilePath);
		
		System.out.println("Hold on");
	}

	private int getY(BufferedImage image, FontMetrics metrics) {
		int positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
		return positionY;
	}

	private int getX(BufferedImage image, String text1, FontMetrics metrics) {
		int positionX = (image.getWidth() - metrics.stringWidth(text1)) / 2;
		return positionX;
	}

	private AttributedString createAttributedText(String text1, Font font) {
		AttributedString attributedText1 = new AttributedString(text1);
		attributedText1.addAttribute(TextAttribute.FONT, font);
		attributedText1.addAttribute(TextAttribute.FOREGROUND, Color.BLACK);
		return attributedText1;
	}

	public static Font createFontToFit(Font baseFont, String text, BufferedImage image) throws IOException {
		Font newFont = baseFont;

		FontMetrics ruler = image.getGraphics().getFontMetrics(baseFont);
		GlyphVector vector = baseFont.createGlyphVector(ruler.getFontRenderContext(), text);

		Shape outline = vector.getOutline(0, 0);

		double expectedWidth = outline.getBounds().getWidth();
		double expectedHeight = outline.getBounds().getHeight();

		boolean textFits = image.getWidth() >= expectedWidth && image.getHeight() >= expectedHeight;

		if (!textFits) {
			double widthBasedFontSize = (baseFont.getSize2D() * image.getWidth()) / expectedWidth;
			double heightBasedFontSize = (baseFont.getSize2D() * image.getHeight()) / expectedHeight;

			double newFontSize = widthBasedFontSize < heightBasedFontSize ? widthBasedFontSize : heightBasedFontSize;
			newFontSize = newFontSize - 10;
			newFont = baseFont.deriveFont(baseFont.getStyle(), (float) newFontSize);
		}
		return newFont;
	}
}
