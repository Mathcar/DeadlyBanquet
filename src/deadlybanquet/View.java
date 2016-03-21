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
	
	public void drawRenderObjects(RenderSet rs, Graphics g){
		rs.getMap().render(0, 0);
		for(RenderObject r : rs.getRenderObjects()){
			if(r.isMoving() == false){
				r.getImage().draw(convert(r.getPos().getX()), convert(r.getPos().getY())); 
			}
			else{
				r.getAnimation().draw(convert(r.getPos().getX() - r.getDistX()), convert(r.getPos().getY() - r.getDistY()));
			}
		}
	}		
	
	public int convert(int i){
		return i*SCALE;
		
	}
}
