/**
 * Controls the big enemies that jump and have more lives
 */
public class Boss extends Enemy{
	
	private int vy;
	private int hops;
	
	public Boss(int x,int y,int speed,int hops) {
		super("img/boss/",8,new Vector3(x,y,Game.maxDepth-10),speed);
		addAnimation("run", 0, 7, 100);
		setAnimation("run");
		lives=3;
		this.hops=hops;
	}
	
	public void updateMovement() {
		
		//z movement
		if(pos.z>0) pos.z-=speed;
		
		//y-kinematics
		if(pos.y<=0) {
			vy=hops;
			pos.y=1;
		}
		else {
			pos.y+=vy;
			vy-=2;
		}
		
	}
	
	public void updateHealth() {
		lives--;
	}

}