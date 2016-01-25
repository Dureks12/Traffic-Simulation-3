package excerise1;

import java.util.*;
import java.awt.Color;

import greenfoot.GreenfootImage;
import greenfoot.World;

public class TrafficWorld extends World {

	private static final Random rn = new Random();
	private static final int WORLD_HEIGHT = 750;
	private static final int WORLD_WIDTH = 1000;
	private static final int CELL_SIZE = 1;	
	private static final int N_WE_ROADS = 5;
	private static final int N_NS_ROADS = 7;
	private static final int ROAD_SIZE = 50;
	private static final int RANGE = 100;
	private static final int N_INTER = N_NS_ROADS * N_WE_ROADS;
	private static final int NS_SPACE = ROAD_SIZE + ((WORLD_WIDTH - (ROAD_SIZE * N_NS_ROADS))/N_NS_ROADS+17);
	private static final int WE_SPACE = ROAD_SIZE+ ((WORLD_HEIGHT - (ROAD_SIZE * N_WE_ROADS))/N_WE_ROADS+25);

	private GreenfootImage background = getBackground();
	private Roads[] eastWestBound = new Roads[N_WE_ROADS];
	private Roads[] northSouthBound = new Roads[N_NS_ROADS];
	private Intersection[] inter = new Intersection[N_INTER];
	private ArrayList<Car> cars = new ArrayList<Car>();
	

	public TrafficWorld(){

		super(WORLD_WIDTH, WORLD_HEIGHT, CELL_SIZE);	

		background.setColor(Color.GREEN);
		background.fill();

		fillRoads();
		createEastWestRoads();
		createNorthSouthRoads();
		createInterSections();
		drawACar();

	}

	private void fillRoads(){
		for(int ns = 0; ns < northSouthBound.length; ns++){
			northSouthBound[ns] = new Roads(DIRECTION.NORTH);
		}

		for(int we = 0; we < eastWestBound.length; we++){
			eastWestBound[we] = new Roads(DIRECTION.WEST);
		}

	}

	private void createInterSections(){
		int n = 0;
		for(int ns = 0; ns < northSouthBound.length; ns++){
			for(int we = 0; we < eastWestBound.length; we++, n++ ){
				int x = ((NS_SPACE*ns)+(ROAD_SIZE/2));
				int y = (ROAD_SIZE/2)+(WE_SPACE*we);
				inter[n] = new Intersection(x, y);
				addLights(inter[n]);
				addObject(inter[n],x,y);
			}
		}
	}

	private void addLights(Intersection inter){
		for(DIRECTION dir: DIRECTION.values()){
			int x = inter.getLight(dir).getXPos();
			int y = inter.getLight(dir).getYPos();
			addObject(inter.getLight(dir),x,y);
		}
	}

	private void createEastWestRoads(){
		for(int we = 0; we < eastWestBound.length; we++){
			int y = (ROAD_SIZE/2)+(WE_SPACE*we);
			addObject(eastWestBound[we],WORLD_WIDTH/2, y);

		}
	}

	private void createNorthSouthRoads(){
		for(int ns = 0; ns < northSouthBound.length; ns++){
			int x = ((NS_SPACE*ns)+(ROAD_SIZE/2));
			addObject(northSouthBound[ns], x, WORLD_HEIGHT/2);
		}
	}
	
	private void drawRandomCars(){
		if(rn.nextInt(RANGE) == 0){
			drawACar();
		}
		
	}
	
	private void drawACar(){
		DIRECTION dir = DIRECTION.values()[rn.nextInt(DIRECTION.values().length)];
		Car car = new Car(dir);
		cars.add(car);
		addObject(car, x(dir,car), y(dir, car));
	}
	
	private int x(DIRECTION dir, Car car){
		int x;
		int cWidth = car.getImage().getWidth();
		if(dir == DIRECTION.NORTH || dir == DIRECTION.SOUTH){
			int space = ((NS_SPACE*rn.nextInt(N_NS_ROADS))+(ROAD_SIZE/2));
			x = (dir == DIRECTION.NORTH)? space+(cWidth/4):space-(cWidth/4);
		}else{
			x = (dir == DIRECTION.WEST)? 2:WORLD_WIDTH-2;
		}
		
		return x;
	}
	
	private int y(DIRECTION dir, Car car){
		int y;
		int cWidth = car.getImage().getWidth();
		if(dir == DIRECTION.NORTH || dir == DIRECTION.SOUTH){
			y = (dir == DIRECTION.NORTH)? WORLD_HEIGHT-3:3;
		}else{
			int space =  (ROAD_SIZE/2)+(WE_SPACE*rn.nextInt(N_WE_ROADS));
			y = (dir == DIRECTION.WEST)? space+(cWidth/4):space-(cWidth/4);
		}
		
		return y;
	}
	

	public void act(){
		drawRandomCars();	
	}
}
