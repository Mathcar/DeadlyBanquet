
import org.newdawn.slick.Graphics;

import java.util.LinkedList;
import java.util.List;


public class Character implements GameObject{
	
	private int xPos;
	private int yPos;
	
	private List<Trait> traits;
	
	private String name;
	private TraitInfo traitInfo;
	
	private TravaresableState state = TravaresableState.SOLID;
	
	public Character(String name){
		this.name = name;
		this.xPos = 0;
		this.yPos = 0;

		traits = new LinkedList<Trait>();
	}

    /*
    Set the trait option, for the first time.
    Has to be ALL the stuff that is in TraitOption
     */
    public void initTraitInfo(TraitInfo.Sex sex){
        this.traitInfo = new TraitInfo(sex);
    }

    public void draw(Graphics g){

    }

    public void setxPos(int x){
        this.xPos=x;
    }

    public void setyPos(int y){
        this.yPos=y;
    }

	public void addTrait(Trait t){
		for(Trait trait: traits){
			if(t.equals(trait)){
				break;
			}	
		}
		traits.add(t);
	}

    /*
    Do NOT use this
     */
	public List<Trait> getTraits(){
		LinkedList<Trait> returnList = new LinkedList<Trait>();
		returnList.addAll(traits); // still a shallow copy. Needs to be fixed.
		return returnList;
	}

    /*
    Check if trait exists
    return true if it dose, and false if not.
     */
    public Boolean traitExist(Trait t){
        if(traits.contains(t)){
            return true;
        }else{
            return false;
        }
    }
	
	@Override
	public int getX() {
		return xPos;
	}

	@Override
	public int getY() {
		return yPos;
	}

	@Override
	public void uppdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSolid() {
		if(state==TravaresableState.SOLID){
			return true;
		}
		return false;
	}

}
