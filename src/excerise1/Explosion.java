package excerise1;

import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class Explosion extends Actor {
	private enum EXPLOSION_PHASE{
		START,MIDDLE,FINISHED, REMOVE;
	}
	
	private GreenfootImage explosion;
	private int count = 0;
	private EXPLOSION_PHASE xPhase;
	
	public Explosion(boolean timeToExplode){
		xPhase = EXPLOSION_PHASE.START;
	}
	
	public void explode(){
		
		switch(xPhase){
		case START:
			firstExplosion();
			break;
		case MIDDLE:
			secondExplosion();
			break;
		case FINISHED:
			thirdExplosion();
			break;
		case REMOVE:
			remove();
			break;
		}
		
		count++;
		setImage(explosion);
	}
	

	private void firstExplosion(){
		explosion = new GreenfootImage("images//explosion1.png");
		if(count == 50){
			xPhase = EXPLOSION_PHASE.MIDDLE;
		}
	}
	
	private void secondExplosion(){
		explosion = new GreenfootImage("images//explosion2.png");
		if(count == 100){
			xPhase = EXPLOSION_PHASE.FINISHED;
		}
	}
	
	private void thirdExplosion(){
		explosion = new GreenfootImage("images//explosion3.png");
		if(count == 175){
			xPhase = EXPLOSION_PHASE.REMOVE;
		}
	}
	
	private void remove(){
		getWorld().removeObject(this);
	}
	
	public void act(){
		explode();
	}
	
}
