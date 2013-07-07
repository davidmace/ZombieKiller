import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Health meter at top of screen. Reloads if we haven't been attacked for a while.
 */
public class LifeMeter {

	static JApplet japplet;
	private int life,maxLife;
	boolean rehealth;
	
	public LifeMeter() {
		maxLife=100;
		life=100;
		rehealth=false;
	}
	
	public void damage(int damage) {
		life-=damage;
		if(life<0) life=0;
	}
	
	public int getLife() {
		return life;
	}
	
	public void addLife(int life) {
		this.life+=life;
		if(this.life>maxLife) this.life=100;
	}

    /**
     * Returns the factor by which we rehealth
     * @return
     */
	public double getTransFactor() {
		if(life/(float)maxLife>0.35) return 1;
		return ((float)life/maxLife)/0.35;
	}
	
	
	public void paint(Graphics g) {
		BufferedImage meter=new BufferedImage(50,50,BufferedImage.TYPE_INT_ARGB);
		int red=new Color(255,0,0).getRGB();
		double rad=6.28*life/maxLife-3.14;
		for(int y=0; y<50; y++) {
			for(int x=0; x<50; x++) {
				int dist=(25-x)*(25-x)+(25-y)*(25-y);
				if(dist<25*25 && dist>15*15 && Math.atan2(25-y, 25-x)<=rad) {
					meter.setRGB(x,y,red);
				}
			}
		}
		Graphics2D meterGraphics=meter.createGraphics();
		meterGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		meterGraphics.setColor(Color.black);
		meterGraphics.drawOval(0,0,50,50);
		meterGraphics.drawOval(10,10,30,30);
		g.drawImage(meter,japplet.getWidth()-50-10,10,japplet);
	}
}
