package deadlybanquet.ai;


import deadlybanquet.model.Character;

public class TaskTurnTo implements Task{
	
	TaskExecuter taskEx;
	Character cha;
	
	public TaskTurnTo(TaskExecuter tEx, Character c){
		taskEx = tEx;
		cha = c;
	}
	
	@Override
	public boolean execute(AIControler aiControler) {
		return taskEx.attemptTurnToAdjacentCharacter(aiControler.getCharacter(),cha);
	}
}
