package deadlybanquet.model;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

import java.awt.event.ActionListener;

/**
 * Created by Hampus on 2016-03-04.
 */
public class NPC extends Character implements Mover{

	private Character c;
	
	public NPC(String name, int xPos, int yPos) {
		super(name, xPos, yPos);
		
	}
	
	public NPC(ActionListener al, Character c){
		super(c);
		this.c = c;
	}

	public void addPath(Path p){
		//Might want to run this one by the AI to see that it is what you want

	}
	
	public Character getChar(){
		return this.c; 
	}
	
}
