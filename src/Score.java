import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JApplet;

public class Score {

	public static JApplet japplet;
	public int score;
	public BufferedImage[] frames;
	public Image[] numbers;
	public int length;
	
	public Score() {
		numbers=new Image[10];
		for(int i=0; i<10; i++) {
			numbers[i]=japplet.getImage(japplet.getDocumentBase(),"img/score/"+i+".png");
		}	
		frames=new BufferedImage[5];
		for(int i=0; i<frames.length; i++) {
			frames[i]=new BufferedImage(43,57,BufferedImage.TYPE_INT_ARGB);
		}
		score=0;
	}
	
	public void inc(int amount) {
		score+=amount;
	}
	
	public int getDigit(int digit) {
		if(digit==0)
			return score%(int)Math.pow(10,digit);
		return (score%(int)Math.pow(10,digit)-score%(int)Math.pow(10,digit-1) )/(int)Math.pow(10,digit-1);
	}
	
	public void paint(Graphics g) {
		for(int i=0; i<5; i++) {
			g.drawImage( numbers[getDigit(5-i)] , i*30 , 0 , japplet );
		}
	}
}
