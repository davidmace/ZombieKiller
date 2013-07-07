import javax.swing.*;
import java.awt.*;

/**
 * Applet that starts by adding the title screen
 */
public class ZombieKiller extends JApplet{
	
	static Container pane;
	
	public void init() {
		resize(700,500);
		
		Title.japplet=this;
		Game.japplet=this;
		Directions.japplet=this;
		DeathScreen.japplet=this;
		
		pane=getContentPane();
		pane.add(new Title());
	}

    /**
     * This is what we call if we are starting a new game.
     *
     * @param p
     */
	public static void change(JPanel p) {
		ZombieKiller.pane.removeAll();
		ZombieKiller.pane.add(p);
		ZombieKiller.pane.validate();
		ZombieKiller.pane.repaint();
	}
	
	
}