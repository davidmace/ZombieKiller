/**
 * Controls the custom behavior of the zombie enemy type.
 */
public class Zombie extends Enemy{
	
	public Zombie(int x,int y,int speed) {
		super("img/zombie/",8,new Vector3(x,y,Game.maxDepth-10),speed);
		addAnimation("run", 0, 7, 100);
		setAnimation("run");
		lives=1;
	}
	
	public void updateMovement() {
		if(pos.z>0) pos.z-=speed;
	}
	
	public void updateHealth() {
		lives--;
	}

}