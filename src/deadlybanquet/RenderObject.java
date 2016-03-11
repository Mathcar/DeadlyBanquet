package deadlybanquet;

import org.newdawn.slick.Image;

import deadlybanquet.model.Position;

/**
 * Created by Hampus on 2016-03-04.
 */
public class RenderObject {
	
	private Position pos;
	private Image image;
	
	public RenderObject(int xPos, int yPos, Image im){
		this.pos = new Position(xPos, yPos);
		this.image = im;
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
}
