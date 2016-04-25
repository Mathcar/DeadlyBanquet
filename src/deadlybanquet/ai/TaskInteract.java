package deadlybanquet.ai;

public class TaskInteract implements Task {

	private TaskExecuter taskEx;
	
	public TaskInteract(TaskExecuter tEx){
		taskEx = tEx;
	}
	
	
	@Override
	public boolean execute(AIControler aiControler) {
		return taskEx.attemptTalk(aiControler.getCharacter());
		
	}

}
