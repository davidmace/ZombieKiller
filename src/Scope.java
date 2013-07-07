import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Place a close up view over what we are shooting for a cool visual effect.
 */
public class Scope {
	
	public static JApplet japplet;
	private int rad;
	private double zoom;
	private BufferedImage img;
	private Point pos;
	
	public Scope() {
		rad=50;
		zoom=1.5;
		img=new BufferedImage(rad*2,rad*2,BufferedImage.TYPE_INT_ARGB);
		pos=new Point(100,100);
	}

    /**
     * Zoom in on the location that our cursor is over with a simple scale over a circular region
     * @param screen
     * @param pos
     */
	public void update( BufferedImage screen , Point pos ) {
		if(screen==null) return;
		this.pos=pos;
		for(int y=-rad; y<rad; y++) {
			for(int x=-rad; x<rad; x++) {
				if(x*x+y*y<=rad*rad && pos.x+x/zoom>=0 && pos.x+x/zoom<screen.getWidth() && pos.y+y/zoom>=0 && pos.y+y/zoom<screen.getHeight()) {
					img.setRGB(x+rad, y+rad, screen.getRGB((int)(pos.x+x/zoom), (int)(pos.y+y/zoom)));
				}
			}
		}
	}

    /**
     * Overlay the scope image
     * @param g
     */
	public void paint(Graphics g) {
		g.drawImage(img,pos.x-rad,pos.y-rad,japplet);
		g.setColor(Color.black);
		//g.drawOval(pos.x-rad,pos.y-rad,rad*2,rad*2);
		g.drawLine(pos.x-10,pos.y,pos.x+10,pos.y);
		g.drawLine(pos.x,pos.y-10,pos.x,pos.y+10);
	}
	
}
