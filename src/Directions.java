import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

/**
 * Instructions screen
 */
public class Directions extends JPanel{
	static JApplet japplet;
	public boolean goNext;
	Image background,zombie,boss;
	AttributedString t1,t2,t3,t4;
	javax.swing.Timer timer;
	
	public Directions() {
		super();
		background=japplet.getImage(japplet.getDocumentBase(), "img/titleBackground.png");
		zombie=japplet.getImage(japplet.getDocumentBase(), "img/zombie/0000.png");
		boss=japplet.getImage(japplet.getDocumentBase(), "img/boss/0000.png");
		Color[] activeColors={
				new Color(130,190,245),
				new Color(145,210,248),
				new Color(130,180,245),
				new Color(200,225,250),
		};
		Color[] normalColors={
				new Color(224,224,224),
				new Color(246,246,246),
				new Color(188,188,188),
				new Color(216,216,216),
		};
		Color[] focusColors={
				new Color(10,138,237),
				new Color(43,163,241),
				new Color(9,107,232),
				new Color(159,196,246),
		};
		add(Box.createRigidArea(new Dimension(0,850)));

        //build the play button
		JPanel play=new JPanel();
		play.setLayout(new BoxLayout(play,BoxLayout.X_AXIS));
			play.add(Box.createRigidArea(new Dimension(300,0)));
			play.add(new CustomButton("Start", new Dimension(100,25), normalColors,focusColors,activeColors) {
				public void buttonClicked(MouseEvent e) {
					goNext=true;
				}
			});
			play.add(Box.createRigidArea(new Dimension(300,0)));
		add(play);
		
		add(Box.createRigidArea(new Dimension(0,10)));

        //construct the instructions text
		Font font = new Font("Times New Roman", Font.BOLD, 18);
		
		t1 = new AttributedString("Your Mission: Shoot the UNDEAD before they kill YOU!");
		t1.addAttribute(TextAttribute.FONT, font);
		
		t2 = new AttributedString("This is a ZOMBIE..Shoot him once to kill him.");
		t2.addAttribute(TextAttribute.FONT, font);
		
		t3 = new AttributedString("This is a JUGGERNAUT... He's a little bit harder to kill.");
		t3.addAttribute(TextAttribute.FONT, font);
		
		t4 = new AttributedString("Now go out and kick some ZOMBIE BUTT!!!");
		t4.addAttribute(TextAttribute.FONT, font);
		
		timer=new javax.swing.Timer( 500, new StartListener() );
		timer.start();
		
	}
	
	public void paint(Graphics g) {
		g.drawImage(background,0,0,japplet);
		g.setColor(Color.white);
		g.drawImage(zombie, 360, 80, japplet);
		g.drawImage(boss, 500, 200, japplet);
		g.drawString(t1.getIterator(),70,80);
		g.drawString(t2.getIterator(),50,200);
		g.drawString(t3.getIterator(),50,300);
		g.drawString(t4.getIterator(),130,380);
		((JPanel)getComponents()[1]).getComponents()[1].repaint();
	}

    /**
     * Start the zombie game
     */
	private class StartListener implements ActionListener{		
		public void actionPerformed(ActionEvent e) {
			if(goNext==true) {
				ZombieKiller.change(new Game());
				timer.stop();
			}
		}	
	}
}
