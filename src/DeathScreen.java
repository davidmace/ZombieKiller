import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Screen that pops up after user dies and asks if want to restart
 */
public class DeathScreen extends JPanel{

	static BufferedImage background;
	static JApplet japplet;
	boolean replay;
	javax.swing.Timer timer;
	
	public DeathScreen() {
		timer=new javax.swing.Timer(100, new PaintListener());
		timer.start();

        //custom color scheme
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
		add(Box.createRigidArea(new Dimension(0,345)));
		
		JPanel instruct=new JPanel();
		instruct.setLayout(new BoxLayout(instruct,BoxLayout.X_AXIS));
			instruct.add(Box.createRigidArea(new Dimension(300,0)));
			instruct.add(new CustomButton("Restart", new Dimension(100,25), normalColors,focusColors,activeColors) {
				public void buttonClicked(MouseEvent e) {
					replay=true;
				}
			});
			instruct.add(Box.createRigidArea(new Dimension(300,0)));
		add(instruct);
		
		add(Box.createRigidArea(new Dimension(0,5)));
	}
	
	public void paint(Graphics g) {
		g.drawImage(background,0,0,japplet);
		System.out.println("DEATHPAINT");
		g.setColor(Color.black);
		g.fillRect(150,100,japplet.getWidth()-300,japplet.getHeight()-200);
		((JPanel)getComponents()[1]).getComponents()[1].repaint();
	}

    /**
     * Wait for the restart button to be clicked
     */
	private class PaintListener implements ActionListener{		
		public void actionPerformed(ActionEvent e) {
			repaint();
			if(replay) {
				ZombieKiller.change(new Game());
				timer.stop();
			}
		}	
	}
	
}
