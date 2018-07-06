package code;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Filestream {
	
	public static Image getImageFromPath(String s)
	{
		try {
			return ImageIO.read(Filestream.class.getResource("res/"+s));
		} catch (IOException e) {
			System.out.println("Image file not found @ "+s);
		}
		return null;
	}
}
