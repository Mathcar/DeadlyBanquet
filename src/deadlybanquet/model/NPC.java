package deadlybanquet.model;

import java.awt.event.ActionListener;

/**
 * Created by Hampus on 2016-03-04.
 */
public class NPC extends Character{

	public NPC(ActionListener al , String name, int xPos, int yPos) {
		super(al, name, xPos, yPos);
		
	}
	
	public NPC(ActionListener al, Character c){
		super(al, c);
	}
	
}
