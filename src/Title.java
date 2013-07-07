import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Controls the start screen
 */
public class Title extends JPanel{
	static JApplet japplet;
	public boolean start,directions;
	Image title,titleBackground;
	javax.swing.Timer timer;
	
	public Title() {
		super();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		timer=new javax.swing.Timer( 500, new StartListener() );
		timer.start();
		titleBackground=japplet.getImage(japplet.getDocumentBase(), "img/titleBackground.png");
		title=japplet.getImage(japplet.getDocumentBase(), "img/title.png");
		start=false;
		directions=false;

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

        //instuctions button
		JPanel instruct=new JPanel();
		instruct.setLayout(new BoxLayout(instruct,BoxLayout.X_AXIS));
			instruct.add(Box.createRigidArea(new Dimension(300,0)));
			instruct.add(new CustomButton("Instructions", new Dimension(100,25), normalColors,focusColors,activeColors) {
				public void buttonClicked(MouseEvent e) {
					directions=true;
				}
			});
			instruct.add(Box.createRigidArea(new Dimension(300,0)));
		add(instruct);
		
		add(Box.createRigidArea(new Dimension(0,5)));

        //play button
		JPanel play=new JPanel();
		play.setLayout(new BoxLayout(play,BoxLayout.X_AXIS));
			play.add(Box.createRigidArea(new Dimension(300,0)));
			play.add(new CustomButton("Start", new Dimension(100,25), normalColors,focusColors,activeColors) {
				public void buttonClicked(MouseEvent e) {
					start=true;
				}
			});
			play.add(Box.createRigidArea(new Dimension(300,0)));
		add(play);
		
		add(Box.createRigidArea(new Dimension(0,100)));
	}
	
	public void paint(Graphics g) {
		g.drawImage(titleBackground,0,0,japplet);
		g.drawImage(title,(japplet.getWidth()-title.getWidth(japplet))/2,100,japplet);
		((JPanel)getComponents()[1]).getComponents()[1].repaint();
		((JPanel)getComponents()[3]).getComponents()[1].repaint();
	}

    /**
     * Start game if user clicks the start button
     */
	private class StartListener implements ActionListener{		
		public void actionPerformed(ActionEvent e) {
			if(start==true) {
				ZombieKiller.change(new Game());
				timer.stop();
			}
			else if(directions==true) {
				ZombieKiller.change(new Directions());
				timer.stop();
			}
		}	
	}
}
