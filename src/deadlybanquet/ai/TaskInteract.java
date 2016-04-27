package deadlybanquet.ai;

public class TaskInteract implements Task {

	private TaskExecuter taskEx;
	
	public TaskInteract(TaskExecuter tEx){
		taskEx = tEx;
	}
	
	
	@Override
	public boolean execute(AIControler aiControler) {
		if(!aiControler.hasPath()){					//WHY THIS CHECK?? /Hampus
			System.out.println("TaskInteract executing...");
			return taskEx.attemptTalk(aiControler.getCharacter());
		}
		return false;
	}

}
