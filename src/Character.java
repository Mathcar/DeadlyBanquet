import java.util.LinkedList;
import java.util.List;


public class Character implements GameObject{
	
	private int xPos;
	private int yPos;
	
	private List<Trait> traits;
	
	private String name;
	
	
	private TravaresableState state = TravaresableState.SOLID;
	
	public Character(String name){
		this.name = name;
		this.xPos = 0;
		this.yPos = 0;
		
		traits = new LinkedList<Trait>();
	}


	public Character(String name, int x, int y){
		this.name=name;
		this.xPos=x;
		this.yPos=y;

		traits = new LinkedList<Trait>();
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
