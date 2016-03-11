package deadlybanquet;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import deadlybanquet.model.Position;


/**
 * Created by Hampus on 2016-03-04.
 */
public class RenderObject {
	
	private Position pos;
	private Image image;
	private Animation animation;
	private int distX, distY;
	private boolean moving;
	
	public RenderObject(int xPos, int yPos, Animation a, boolean m, int distX, int distY){
		this.pos = new Position(xPos, yPos);
		this.animation = a;
		this.distX = distX;
		this.distY = distY;
		this.moving = m;
		
	}
	
	public RenderObject(int xPos, int yPos, Image im, boolean m){
		this.pos = new Position(xPos, yPos);
		this.image = im;
		this.moving = m;
	}
	
	public int getXPos(){
		return pos.getX();
	}
	
	public int getYPos(){
		return pos.getY();
	}
	
	public Position getPos(){
		return pos;
	}
	
	public Image getImage(){
		return image;
	}
	public Animation getAnimation(){
		return animation;
	}
	public int getDistX(){
		return distX;
	}
	public int getDistY(){
		return distY;
	}
	public boolean isMoving(){
		return moving;
	}
}
