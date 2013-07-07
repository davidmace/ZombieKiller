import javax.swing.*;
import java.awt.*;

/**
 * Base implementation for our enemies that incorporates their base x,y,z movement behavior.
 */
public abstract class Sprite {

	public static JApplet japplet;
	protected Vector3 pos;
	protected Dimension defaultSize;
	
	public Sprite(Vector3 pos) {
		this.pos=pos;
	}

    /**
     * Small bit of math to use similar triangles to find image coordinates and sizes. We want further away
     * enemies to be smaller so they're harder to shoot.
     */
	public int getScreenX() {
		return (int)(-(Game.screenDim.width/2-pos.x)*(1-pos.z/Game.maxDepth)+Game.screenDim.width/2);
	}
	public int getScreenY() {
		return (int)(Game.screenDim.height-Math.sin(Game.tiltAngle)*pos.z-(1-pos.z/Game.maxDepth)*pos.y-getHeight());
	}
    public int getWidth() {
        return (int)(defaultSize.width*(1-pos.z/Game.maxDepth));
    }
    public int getHeight() {
        return (int)(defaultSize.height*(1-pos.z/Game.maxDepth));
    }

	public double getWorldX() { return pos.x; }
	public double getWorldY() { return pos.y; }
	public double getWorldZ() { return pos.z; }
	
	public abstract void paint(Graphics g);
	
}
