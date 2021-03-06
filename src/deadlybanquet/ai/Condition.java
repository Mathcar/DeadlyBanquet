package deadlybanquet.ai;

public class Condition {
	
	public enum ConditionState{
		INTERUPTED,
		SLEEPING,
		WANT_TO_TALK_TO,
		STANDING_NEXT_TO,
		DONT_WANT_TO_SE,
		TALKING,
		STANDING_NEXT_TO_DOOR
	}
	
	private ConditionState condition;
	private String characterName;
	
	public Condition(ConditionState condition, String characterName){
		this.condition = condition;
		this.characterName = characterName;
	}
	
	public Condition(ConditionState condition){
		this.condition = condition;
		this.characterName = "";
	}
	
	public ConditionState getCondition() {
		return condition;
	}

	public String getCharacterName() {
		return characterName;
	}
	
	@Override
	public boolean equals(Object rhs ){
		if(rhs != null && rhs instanceof Condition){
			Condition tmp = (Condition)rhs;
			if(this.condition == tmp.condition && this.characterName == tmp.characterName) return true;
		}
		return false;
	}
	
}
