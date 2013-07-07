import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JApplet;

public abstract class Enemy extends Sprite {

	public AnimationController anim;
	protected int speed;
	public int lives;
	public static double maxSpeed;
	
	public Enemy(String dir,int animationLength, Vector3 pos, int speed) {
		super(pos);
		anim=new AnimationController(dir,animationLength);
		super.defaultSize=anim.getFrameSize();
		this.speed=speed;
	}
	
	public Ghost makeGhost() {
		return new Ghost(pos,anim.getCurrentScaledFrame());
	}
	
	public void setAnimation(String name) {
		anim.setAnimation(name);
	}
	public void addAnimation(String name, int startFrame, int endFrame, int speed) {
		anim.addAnimation(name, startFrame, endFrame, speed);
	}
	
	public abstract void updateMovement();
	public abstract void updateHealth();
	
	public boolean checkAttacking() {
		if(pos.z<=0) {
			return true;
		}
		return false;
	}
	
	public boolean checkAttacked(Point mousePos) {
		Point top=new Point(getScreenX(),getScreenY());
		if(mousePos.x>top.x && mousePos.x<top.x+getWidth() && mousePos.y>top.y && mousePos.y<top.y+getHeight()) {
			if( new Color(anim.getCurrentScaledFrame().getRGB(mousePos.x-top.x,mousePos.y-top.y),true).getAlpha()>128 ) {
				updateHealth();
				return true;
			}
			
		}
		return false;
		
		
		/*Point top=new Point(getScreenX(),getScreenY());
		double factor=1-pos.z/ZombieKiller.maxDepth;
		Point colliderStartResize=new Point((int)(colliderStartPoint.x*factor),(int)(colliderStartPoint.y*factor));
		Point colliderEndResize=new Point((int)(colliderEndPoint.x*factor),(int)(colliderEndPoint.y*factor));
		if(mousePos.x>top.x+colliderStartResize.x && mousePos.x<top.x+colliderEndResize.x && mousePos.y>top.y+colliderStartResize.y && mousePos.y<top .y+colliderEndResize.y) {
			updateHealth();
			return true;
		}
		return false;*/
	}
	
	public void paint(Graphics g) {
		//scale image from animation
		/*int width=getWidth();
		int height=getHeight();
		BufferedImage resizedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D scaled= resizedImage.createGraphics();
		scaled.drawImage(anim.getCurrentFrame(), 0, 0, width, height, null);
		scaled.dispose();
		//drawImage
		g.drawImage(resizedImage,getScreenX(),getScreenY(),null);*/
		g.drawImage( anim.getCurrentFrame().getScaledInstance(getWidth(),getHeight(),Image.SCALE_FAST) , getScreenX() , getScreenY()  , japplet);
	}
	
	//animation controller
	private class AnimationController {
		
		private BufferedImage[] frames;
		private int currentFrame;
		private int length;
		private ArrayList <Animation> animations;
		private Animation currentAnimation;
		private javax.swing.Timer timer;	
		private RefreshListener refreshListener;
		
		public AnimationController(String dir, int length) {
			this.length=length;
			currentFrame=0;
			frames=new BufferedImage[length];
			for(int i=0; i<frames.length; i++) {
				frames[i]=loadImage(dir+"000"+i+".png");
			}
			animations=new ArrayList();
			refreshListener= new RefreshListener();
		}
		
		//add new animation
		public void addAnimation(String name, int startFrame, int endFrame, int speed) {
			animations.add(new Animation(name,startFrame,endFrame,speed));
		}
		
		//add new animation
		public void setAnimation(String name) {
			for(int i=0; i<animations.size(); i++) {
				if(animations.get(i).name==name) {
					if(currentAnimation==animations.get(i)) {
						return;
					}
					currentAnimation=animations.get(i);
					currentFrame=currentAnimation.startFrame;
				}
			}
			timer= new javax.swing.Timer( currentAnimation.speed, refreshListener );
			timer.start();
		}
		
		//get frame size
		public Dimension getFrameSize() {
			return new Dimension(frames[0].getWidth(),frames[0].getHeight());
		}
		//refresh frame
		public void refreshFrame() {
			currentFrame++;
			if(currentFrame>currentAnimation.endFrame) {
				currentFrame=currentAnimation.startFrame;
			}
		}
		
		//every few milliseconds reset screen
		private class RefreshListener implements ActionListener{		
			public void actionPerformed(ActionEvent e) {
				refreshFrame();
			}	
		}
			
		//return current frame
		private BufferedImage getCurrentFrame() {
			return frames[currentFrame];
		}
		public BufferedImage getCurrentScaledFrame() {
			int width=getWidth();
			int height=getHeight();
			BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics2D = scaledImage.createGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics2D.drawImage(getCurrentFrame(), 0, 0, width, height, japplet);
			graphics2D.dispose();
			return scaledImage;
		}
		
		/*//split image into frames
		private void segmentImage(BufferedImage image, int framesWide, int framesHigh) {
			//setup frame stuff
			int frameLimit=framesWide*framesHigh;
			int frameWidth=image.getWidth(null)/framesWide;
			int frameHeight=image.getHeight(null)/framesHigh;
			frames=new BufferedImage[frameLimit];
			//go through all frames
			for(int fy=0; fy<framesHigh; fy++) {
				for(int fx=0; fx<framesWide; fx++) {
					//initialize new frames
					frames[fy*framesWide+fx] = new BufferedImage(frameWidth, frameHeight, image.getType());
					//put data in frames
					Graphics2D gr = frames[fy*framesWide+fx].createGraphics();  
					gr.drawImage(image, 0, 0, frameWidth, frameHeight, frameWidth*fx, frameHeight*fy, frameWidth*fx+frameWidth, frameHeight*fy+frameHeight, null);
					gr.dispose();
				}
			}
		}*/
		
		public BufferedImage loadImage(String name) {
			Image i=japplet.getImage(japplet.getDocumentBase(), name);
			MediaTracker mediaTracker=new MediaTracker(japplet);
			mediaTracker.addImage(i,0);
			try {
				mediaTracker.waitForAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BufferedImage b=new BufferedImage(400,400,BufferedImage.TYPE_INT_ARGB);
			Graphics g = b.createGraphics();
		    g.drawImage(i, 0, 0, japplet);
			return b;
		}
		
		//animation class
		private class Animation {
			
			public String name;
			public int startFrame;
			public int endFrame;
			public int speed;
			
			public Animation(String name, int startFrame, int endFrame, int speed) {
				this.name=name;
				this.startFrame=startFrame;
				this.endFrame=endFrame;
				this.speed=speed;
			}
			
		}
	}

	
}
