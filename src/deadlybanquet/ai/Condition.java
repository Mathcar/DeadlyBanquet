package deadlybanquet.ai;

public class Condition {
	
	public enum ConditionState{
		INTERUPTED,
		SLEAPING,
		WHANT_TO_TALK_TO,
		STANDING_NEXT_TO,
		DONT_WHANT_TO_SE
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
	
}
