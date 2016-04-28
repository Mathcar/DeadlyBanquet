package deadlybanquet.ai;

import deadlybanquet.model.Direction;
import deadlybanquet.model.Character;


/*Allows tasks to use the worlds attempt-methods,
 * such as creating paths,talking and moving.
 */
public interface TaskExecuter {

	public boolean attemptTalk(Character c);
	public boolean attemptChangeRooms(Character c);
	public boolean attemptMove(Character c, Direction d);
	public boolean attemptCreatePathToPerson(AIControler aic, String s);
	public boolean attemptCreatePathToDoor(AIControler aic, String s);
	public boolean attemptCreateMasterPath(AIControler aic, String s);
	public boolean attemptTurnToAdjacentCharacter(Character c, Character c2);
}
