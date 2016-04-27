package deadlybanquet.ai;

import deadlybanquet.model.Debug;

public class TaskInteract implements Task {

	private TaskExecuter taskEx;
	
	public TaskInteract(TaskExecuter tEx){
		taskEx = tEx;
	}
	
	
	@Override
	public boolean execute(AIControler aiControler) {
		if(!aiControler.hasPath()){					//WHY THIS CHECK?? /Hampus
			Debug.printDebugMessage("TaskInteract executing...", Debug.Channel.NPC, aiControler.getCharacterName());
			return taskEx.attemptTalk(aiControler.getCharacter());
		}
		return false;
	}

}
