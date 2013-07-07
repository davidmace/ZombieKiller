import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Controls the game logic
 */
public class Game extends JPanel implements MouseListener, MouseMotionListener{
	
	static JApplet japplet;
	int creationListenerDelay;
	LList sprites;
	LList ghosts;
	Image background;
	Random random;
	boolean alreadyClicked;
	static int maxDepth;
	static double tiltAngle;
	static Dimension screenDim;
	BufferedImage screen;
	Graphics screenGraphics;
	Scope scope;
	LifeMeter lifeMeter;
	Score score;
	Point mousePos;
	javax.swing.Timer attackListener,creationListener,paintListener,rehealthListener;
	//double maxSpeed,creationDelay,reloadHealth;
	
	public Game() {

        //reload the health if not attacked
		rehealthListener=new javax.swing.Timer( 15000, new RehealthListener() );
		rehealthListener.start();

        //create new enemy
        creationListener=new javax.swing.Timer( 1500, new CreationListener() );
		creationListener.start();
		creationListenerDelay=1500;

        //paint the screen
        paintListener=new javax.swing.Timer( 100, new PaintListener() );
		paintListener.start();

        //enemy attacks if next to the screen
        attackListener=new javax.swing.Timer( 500, new AttackListener() );
		attackListener.start();

        addMouseListener(this);
		addMouseMotionListener(this);
		maxDepth=800;
		Enemy.maxSpeed=5;
		alreadyClicked=false; 
		Sprite.japplet=japplet;
		tiltAngle=Math.PI/13;
		screenDim=japplet.getSize();
		random=new Random();
		sprites=new LList();
		ghosts=new LList();

        background=japplet.getImage(japplet.getDocumentBase(), "img/background.png");
		screen=new BufferedImage(japplet.getWidth(),japplet.getHeight(),BufferedImage.TYPE_INT_ARGB);
		screenGraphics = screen.createGraphics();

        MediaTracker mediaTracker=new MediaTracker(japplet);
		mediaTracker.addImage(background,0);
		try {
			mediaTracker.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        //play the background music
		new AePlayWave("sounds/music.wav").start();

		Scope.japplet=japplet;
		scope=new Scope();
		LifeMeter.japplet=japplet;
		lifeMeter=new LifeMeter();
		Score.japplet=japplet;
		score=new Score();
		mousePos=new Point(0,0);
	}
	
	public void paint(Graphics g) {
		System.out.println("UPDATE SCREEN");
		//background
		screenGraphics.drawImage(background,0,0,japplet);
		
		//update sprites
		LListNode current=sprites.getLastNode();
		while(current!=null) {
			Enemy s=(Enemy)current.data;
			//check dead
			if(s.lives==0) {
				ghosts.add(((Enemy)current.data).makeGhost());
				sprites.remove(current);
				if(current.data instanceof Zombie)
					score.inc(50);
				else 
					score.inc(200);
			}
			else {
		        //move
				System.out.print("a");
		        s.updateMovement();
		        //paint
		        s.paint(screenGraphics);
			}
		       
		    current=current.prev;
		}
		
		//paint ghosts
		LListNode currentG=ghosts.getFirstNode();
		while(currentG!=null) {
			Ghost s=(Ghost)currentG.data;
			if(s.getScreenY()<0)
				ghosts.remove(currentG);
			s.paint(screenGraphics);
		    currentG=currentG.next;
		}
		
		//red tint
		double transFactor=lifeMeter.getTransFactor();
		int[] pixels=new int[screen.getWidth()*screen.getHeight()];
		screen.getRGB(0,0,screen.getWidth(),screen.getHeight(),pixels,0,screen.getWidth());
		for(int i=0; i<pixels.length; i++) {
			Color c=new Color(pixels[i]);
			Color cNew=new Color( c.getRed() , (int)(c.getGreen()*transFactor) , (int)(c.getBlue()*transFactor) );
			pixels[i]=cNew.getRGB();
		}
		screen.setRGB(0,0,screen.getWidth(),screen.getHeight(),pixels,0,screen.getWidth());
		
		//scope
		scope.update(screen,mousePos);
		scope.paint(screenGraphics);
		
		//score
		score.paint(screenGraphics);
				
		//health
		lifeMeter.paint(screenGraphics);
		
		g.drawImage(screen,0,0,japplet);
				
	}

    /**
     * Kill the nearest enemy that is under our shot
     *
     * @param p
     */
	public void checkShot(Point p) {
		//loop sprites
		LListNode current=sprites.getFirstNode();
		while(current!=null) {
			Enemy s=(Enemy)current.data;
	
			if(s.checkAttacked(p)==true) break;
		
		    current=current.next;
		}
	}

    /**
     * Shoot at the zombie and play a noise when we click
     *
     * @param e
     */
	public void mouseClicked(MouseEvent e) {
		new AePlayWave("sounds/shot.wav").start();
		checkShot(e.getPoint());
	}
	public void mouseDragged(MouseEvent e) {
		if(alreadyClicked==false) {
			checkShot(e.getPoint());
			alreadyClicked=true;
		}
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {
		mousePos=e.getPoint();
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {
		alreadyClicked=false;
	}

    /**
     * Create new enemies
     */
	private class CreationListener implements ActionListener{		
		public void actionPerformed(ActionEvent e) {

            //make the game harder by making the zombies spawn faster and run faster
            creationListenerDelay*=.99;
			creationListener.setDelay(creationListenerDelay);
			Enemy.maxSpeed*=1.01;

            //create an enemy
			int select=random.nextInt(100);
			if(select<10)
				sprites.add(new Boss(random.nextInt(japplet.getWidth()-150),0,random.nextInt((int)Enemy.maxSpeed)+2,random.nextInt(25)));
			else if(select<20)
				sprites.add(new Zombie(random.nextInt(japplet.getWidth()-150),0,random.nextInt((int)Enemy.maxSpeed*2)+2));
			else
				sprites.add(new Zombie(random.nextInt(japplet.getWidth()-150),0,random.nextInt((int)Enemy.maxSpeed)+2));
			repaint();
		}	
	}

    /**
     * Refresh screen and add health back at constant rate if we haven't been attacked for a while
     */
	private class PaintListener implements ActionListener{		
		public void actionPerformed(ActionEvent e) {
			sortSprites();
			if(lifeMeter.rehealth) {
				lifeMeter.addLife(2);
				System.out.println("add");
			}
			repaint();
		}	
	}

    /**
     * Do the intermittent attack from the enemies that are touching the screen
     */
	private class AttackListener implements ActionListener{		
		public void actionPerformed(ActionEvent e) {
			checkSpritesAttacking();
			checkPlayerDeath();
		}	
	}

    /**
     * Check if we haven't been hit for a while so we can start gaining back health
     */
	private class RehealthListener implements ActionListener{		
		public void actionPerformed(ActionEvent e) {
			lifeMeter.rehealth=true;
			rehealthListener.stop();
		}	
	}

    /**
     * Check if player died
     */
	public void checkPlayerDeath() {
		if(lifeMeter.getLife()<=0) {
			//ZombieKiller.gameRememberer.add(this);
			repaint();
			ZombieKiller.change(new DeathScreen());
			attackListener.stop();
			creationListener.stop();
			paintListener.stop();
			rehealthListener.stop();
			DeathScreen.background=screen;
			/*removeMouseListener(this);
			removeMouseMotionListener(this);
			attackListener.stop();
			new javax.swing.Timer(100, new DeathListener()).start();*/
			System.out.println("AT DEATH");
		}
	}

    /**
     * All of the logic for decreasing health if enemy is touching screen
     */
	public void checkSpritesAttacking() {
		LListNode current=sprites.getFirstNode();
		while(current!=null) {
			Enemy s=(Enemy)current.data;
			if( s.checkAttacking() ) {
				lifeMeter.damage(3);
				lifeMeter.rehealth=false;
				rehealthListener.start();
			}
			current=current.next;
		}
	}

    /**
     * Put sprites in correct order so faster sprites can get in front of slower sprites for drawing
     */
	public void sortSprites() {
		if(sprites.size()>1) {
			LListNode low,high,current;
			current=sprites.getFirstNode().next;
			for(int i=1; i<sprites.size(); i++) {
				low=current;
				while(low.prev!=null) {
					high=low;
					low=low.prev;
					if(((Sprite)low.data).getWorldZ()<((Sprite)high.data).getWorldZ()) {
						break;
					}
					Object sv=low.data;
					low.data=high.data;
					high.data=sv;
				}
				current=current.next;
			}
			
		}
	}
	
}
