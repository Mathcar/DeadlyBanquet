package deadlybanquet;

import org.newdawn.slick.Image;

/**
 * Created by Hampus on 2016-03-04.
 */
public class RenderObject {
	
	private int xPos;
	private int yPos;
	private Image image;
	
	public RenderObject(int xPos, int yPos, Image im){
		this.xPos = xPos;
		this.yPos = yPos;
		this.image = im;
	}
	
	public int getXPos(){
		return xPos;
	}
	
	public int getYPos(){
		return yPos;
	}
	
	public Image getImage(){
		return image;
	}
}
