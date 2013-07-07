import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class CustomButton extends JPanel implements MouseListener{
	
	JApplet japplet;
	Image buttonImg,normalImg, activeImg, focusImg;
	String text;
	
	public CustomButton(String text, Dimension dim, Color[] normalColors, Color[] focusColors, Color[] activeColors) {
		super();
		setPreferredSize(dim);
		int border=Math.max( Math.max( normalColors.length , activeColors.length ) , focusColors.length );
		this.text=text;
		normalImg=constructButton(normalColors);
		activeImg=constructButton(activeColors);
		focusImg=constructButton(focusColors);
		buttonImg=normalImg;
		addMouseListener(this);
	}
	
	private BufferedImage constructButton( Color[] color) {
		Dimension d=getPreferredSize();
		BufferedImage button=new BufferedImage(d.width,d.height,BufferedImage.TYPE_INT_ARGB);
		Graphics g=button.createGraphics();
		
		//make corners
		BufferedImage cornerTL=new BufferedImage( color.length*2 , color.length*2 , BufferedImage.TYPE_INT_ARGB );
		Graphics2D cornerTLG=cornerTL.createGraphics();
		cornerTLG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for(int y=color.length-1; y>=0; y--) {
			cornerTLG.setColor( color[y] );
			cornerTLG.fillOval(color.length-y-1,color.length-y-1,(y+1)*4+1,(y+1)*4+1);
		}
		
		BufferedImage cornerTR=new BufferedImage( color.length*2 , color.length*2 , BufferedImage.TYPE_INT_ARGB );
		BufferedImage cornerBR=new BufferedImage( color.length*2 , color.length*2 , BufferedImage.TYPE_INT_ARGB );
		BufferedImage cornerBL=new BufferedImage( color.length*2 , color.length*2 , BufferedImage.TYPE_INT_ARGB );
		
		for(int y=0; y<color.length*2; y++) {
			for(int x=0; x<color.length*2; x++) {
				int rgb=cornerTL.getRGB(x,y);
				cornerTR.setRGB(color.length*2-x-1,y,rgb);
				cornerBR.setRGB(color.length*2-x-1,color.length*2-y-1,rgb);
				cornerBL.setRGB(x,color.length*2-y-1,rgb);
			}
		}
		
		//make edges
		BufferedImage edgeR=new BufferedImage( color.length , 1 , BufferedImage.TYPE_INT_ARGB );
		BufferedImage edgeL=new BufferedImage( color.length , 1 , BufferedImage.TYPE_INT_ARGB );
		BufferedImage edgeT=new BufferedImage( 1 , color.length , BufferedImage.TYPE_INT_ARGB );
		BufferedImage edgeB=new BufferedImage( 1 , color.length , BufferedImage.TYPE_INT_ARGB );
		
		for(int i=0; i<color.length; i++) {
			int rgb=color[i].getRGB();
			edgeR.setRGB(i, 0, rgb);
			edgeL.setRGB(color.length-i-1, 0, rgb);
			edgeB.setRGB(0, i, rgb);
			edgeT.setRGB(0, color.length-i-1, rgb);
		}
		
		//draw corners
		g.drawImage(cornerTL,0,0,japplet);
		g.drawImage(cornerTR,d.width-color.length*2,0,japplet);
		g.drawImage(cornerBL,0,d.height-color.length*2,japplet);
		g.drawImage(cornerBR,d.width-color.length*2,d.height-color.length*2,japplet);
		
		//draw background
		g.setColor( color[0] );
		g.fillRect(color.length , color.length , d.width-color.length*2 , d.height-color.length*2);
				
		//draw left and right
		int rightOffset=d.width-color.length;
		for(int y=color.length*2-1; y<d.height-color.length*2+1; y++) {
			g.drawImage(edgeL,0,y,japplet);
			g.drawImage(edgeR,rightOffset,y,japplet);
		}
		
		//draw top and bottom
		int bottomOffset=d.height-color.length;
		for(int x=color.length*2-1; x<d.width-color.length*2+1; x++) {
			g.drawImage(edgeT,x,0,japplet);
			g.drawImage(edgeB,x,bottomOffset,japplet);
		}
		
		//draw string
		Font font = UIManager.getDefaults().getFont("Button.font");
		FontMetrics metrics =g.getFontMetrics(font);
		int hgt = metrics.getHeight();
		int adv = metrics.stringWidth(text);
		Dimension size = new Dimension(adv, hgt);
		
		AttributedString attributedString = new AttributedString(text);
		attributedString.addAttribute(TextAttribute.FONT, font);
		g.setColor(Color.black);
		g.drawString(text, (d.width-adv)/2, (d.height-hgt)/2+hgt);
		
		return button;
	}

	public void paint(Graphics g) {
		g.drawImage(buttonImg,0,0,japplet);
		//g.drawImage(content,(getPreferredSize().width-content.getWidth(japplet))/2,(getPreferredSize().height-content.getHeight(japplet))/2,japplet);
	}
	
	public void actionPerformed(ActionEvent e) {
		buttonImg=focusImg;
		repaint();
	}

	public void mouseClicked(MouseEvent arg0) {}

	public void mouseEntered(MouseEvent arg0) {
		buttonImg=activeImg;
		repaint();
	}

	public void mouseExited(MouseEvent arg0) {
		buttonImg=normalImg;
		repaint();
	}

	public void mousePressed(MouseEvent arg0) {
		buttonImg=focusImg;
		repaint();
		buttonClicked(arg0);
	}

	public void mouseReleased(MouseEvent arg0) {}
	
	//override this when using button
	public void buttonClicked(MouseEvent arg0) {}
	
}


