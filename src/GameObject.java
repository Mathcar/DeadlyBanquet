
public interface GameObject {
	
	public int getX();
	
	public int getY();
	
	public void uppdate();
	
	public boolean isSolid();
	
	enum travaresableState{
		SOLID, SMALL, FLAT;
	}
}
