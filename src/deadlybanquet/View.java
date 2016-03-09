package deadlybanquet;


import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Hampus on 2016-03-04.
 */
public class View {
	public static final int SCALE = 32;
	private int height, width;
	
	public View(int height, int width) {
		this.height = height;
		this.width = width;
	}
	
	public void drawRednerObjects(ArrayList<RenderObject> ro, Graphics g){
		for(RenderObject r : ro){
			r.getImage().draw(convert(r.getPosition().getX()), convert(r.getPosition().getY())); 
		}
	}		
	
	public int convert(int i){
		return i*SCALE;
		
	}
}
