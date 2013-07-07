import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Ghost extends Sprite{
	
	BufferedImage[] frames;
	int currentFrame;
	
	public Ghost(Vector3 pos, BufferedImage img) {
		super(pos);
		super.defaultSize=new Dimension(img.getWidth(),img.getHeight());
		currentFrame=0;
		new javax.swing.Timer( 20, new UpdateListener() ).start();
		int frequency=2;
		int amplitude=img.getWidth()/10;
		frames=new BufferedImage[20];
		for(int i=0; i<frames.length; i++) {
			double sinOffset=Math.PI*2/frames.length*frequency*i;
			frames[i]=new BufferedImage(img.getWidth()+2*amplitude,img.getHeight(),BufferedImage.TYPE_INT_ARGB);
			for(int y=0; y<img.getHeight(); y++) {
				int offset=(int)(Math.sin(sinOffset+(float)y/img.getHeight()*2*Math.PI*frequency)*amplitude);
				for(int x=0; x<img.getWidth(); x++) {
					Color c=new Color(img.getRGB(x, y),true);
					int avg=( c.getRed()+c.getGreen()+c.getBlue() )/3;
					int newColor=new Color(avg,avg,avg,c.getAlpha()).getRGB();
					frames[i].setRGB(x+offset+amplitude, y, newColor);
				}
			}
		}
	}
	
	public void paint(Graphics g) {
		g.drawImage(frames[currentFrame],getScreenX(),getScreenY(),japplet);
	}
	
	private class UpdateListener implements ActionListener{		
		public void actionPerformed(ActionEvent e) {
			currentFrame++;
			if(currentFrame==frames.length) currentFrame=0;
			pos.y+=80;
		}	
	}
}
