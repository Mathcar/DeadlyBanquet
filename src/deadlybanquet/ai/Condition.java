package deadlybanquet.ai;

public class condition {
	
	public enum ConditionState{
		INTERUPTED,
		SLEAPING,
		WHANT_TO_TALK_TO,
		STANDING_NEXT_TO,
		DONT_WHANT_TO_SE
	}
	
	private ConditionState condition;
	private String characterName;
	
	public condition(ConditionState condition, String characterName){
		this.condition = condition;
		this.characterName = characterName;
	}
	
	public condition(ConditionState condition){
		this.condition = condition;
		this.characterName = "";
	}
	
}
