package deadlybanquet.ai;

import java.awt.event.ActionListener;

import deadlybanquet.model.NPC;

public class AIControler {
	private NPC npc;
	
	
	private final static int MOVEMNET_DELAY = 500;
	private int movmentTimer = 0;
	
	public AIControler(ActionListener al){
		this.npc = new NPC(al, "Frådo", 3, 3);
	}
	
	public AIControler(ActionListener al, deadlybanquet.model.Character c){
		this.npc = new NPC(al, c);
	}
}
