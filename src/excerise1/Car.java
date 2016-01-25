package excerise1;

import java.util.*; 
import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class Car extends Actor implements IntersectionListener {

	private static final int NORMAL_SPEED = 3;
	private static final int FAST_SPEED = 5;
	private static final int SLOW_SPEED = 1;
	private static final int PERCENT = 40;
	private static final int RANGE = 100;
	private static final int STOP = 0;
	private static final int TURN = 12;


	private int speed;
	private DIRECTION dir;
	private DIRECTION turning;
	private boolean right;
	private boolean turned = false;
	private boolean crashed = false;
	private Random rn = new Random();
	private GreenfootImage[] cars = { new GreenfootImage("images\\topCarBlue.png"), new GreenfootImage("images\\topCarRed.png"),
			new GreenfootImage("images\\topCarPurple.png"),	new GreenfootImage("images\\topCarYellow.png")};


	public Car(DIRECTION dir){
		this.dir = dir;
		setImage(cars[rn.nextInt(cars.length)]);
		rotate(dir);
		right = rn.nextBoolean();
	}

	private void rotate(DIRECTION dir){
		setRotation(dir.setRotation(dir));
	}

	private void atTheEdge(){
		try{
			if(!crashed && this.isAtEdge()){
				throw new Error("at Edge");
			}
		}catch(Error e){
			getWorld().removeObject(this);
		}
	}

	public void collision(){

		try{
			if(this.isTouching(this.getClass())){
				throw new Error("collision");
			}
		}catch(Error e){
			crashed = true;
			crash();
		}

	}

	private void crash(){
		if(crashed){
			Explosion x = new Explosion(true);
			getWorld().addObject(x, this.getX(), this.getY());
			System.out.println("crash");
			this.removeTouching(this.getClass());
			getWorld().removeObject(this);
		}
	}


	@Override
	public void approaching(Intersection i) {
		for(DIRECTION d: DIRECTION.values()){
			approachingInter(d, i);
		}

	}

	@Override
	public void inInterection(Intersection i) {

		for(DIRECTION d: DIRECTION.values()){	
			inInter(d,i);
		}
	}

	private void approachingInter( DIRECTION dir, Intersection i){
		if(this.dir == dir  &&  i.getLight(dir).getState() == Light.STATE.GREEN){
			speed = NORMAL_SPEED;
		}

		if(this.dir == dir && i.getLight(dir).getState() == Light.STATE.YELLOW){
			speed = SLOW_SPEED;
		}

		if(this.dir == dir && i.getLight(dir).getState() == Light.STATE.RED){
			speed = SLOW_SPEED;
		}
		
		assert(this.isTouching(i.getClass()));
		
		if(!this.isTouching(i.getClass())){
			turned = false;
		}
	}

	private void inInter(DIRECTION dir, Intersection i){ 
		Light.STATE state = i.getLight(dir).getState();

		if(this.dir == dir && isTouching(i.getClass()) && state == Light.STATE.RED){
			speed = (turned)? NORMAL_SPEED:STOP;
		}

		if(this.dir == dir && isTouching(i.getClass()) && state == Light.STATE.GREEN){
			speed = NORMAL_SPEED;
			turn(i);
		}

		if(this.dir == dir && isTouching(i.getClass()) && state == Light.STATE.YELLOW){
			speed = FAST_SPEED;
			turn(i);
		}
	}

	private void turn(Intersection i){

		if(rn.nextInt(RANGE) < PERCENT){
			speed = SLOW_SPEED;
			turnWestEast(i);
			turnNorthSouth(i);
			
		}

	}

	private void turnNorthSouth(Intersection i){
		if(this.dir == DIRECTION.WEST ||this.dir == DIRECTION.EAST){
			if(!turned){
				turning = (!right) ? DIRECTION.NORTH:DIRECTION.SOUTH;
				assert(turning != DIRECTION.NORTH || turning != DIRECTION.SOUTH);
				if(turning == DIRECTION.NORTH){
					turnNorth(i);
				}else if(turning == DIRECTION.SOUTH){
					turnSouth(i);
				}
			}
		}
	}
	
	private void turnNorth(Intersection i){
		if(this.getX() == i.getX()+TURN){
			this.dir = DIRECTION.NORTH;
			this.setRotation(dir.setRotation(dir));
			turned = true;
		}
	}
	
	private void turnSouth(Intersection i){
		if(this.getX() == i.getX() - TURN){
			this.dir = DIRECTION.SOUTH;
			this.setRotation(dir.setRotation(dir));
			turned = true;
		}
	}

	private void turnWestEast(Intersection i){
		if(dir == DIRECTION.NORTH || dir == DIRECTION.SOUTH){
			if(!turned){
				turning = (!right)? DIRECTION.EAST:DIRECTION.WEST;
				assert(turning != DIRECTION.EAST  || turning != DIRECTION.WEST);
				if(turning == DIRECTION.EAST){
					turnEast(i);
				}else if(turning == DIRECTION.WEST){
					turnWest(i);
				}
			}
		}
	}
	
	private void turnEast(Intersection i){
		if(this.getY() == i.getY() - TURN){
			this.dir = DIRECTION.EAST;
			this.setRotation(dir.setRotation(dir));
			turned = true;
		}
	}
	
	private void turnWest(Intersection i){
		if(this.getY() == i.getY() + (TURN)){
			this.dir = DIRECTION.WEST;
			this.setRotation(dir.setRotation(dir));
			turned = true;
		}
	}

	@Override
	public void leaving(Intersection i) {
		speed = STOP;
	}

	public void act(){
		collision();
		atTheEdge();
		move(speed);
	}
}
