package deadlybanquet.model;

public class TimeStamp {
	
	private int hour, day;
	private float minute;
	
	public TimeStamp(Time currentTime){
		this.minute = currentTime.getMinute();
		this.hour = currentTime.getHour();
		this.day = currentTime.getDay();
	}
	
	public int getHour() {
		return hour;
	}

	public int getDay() {
		return day;
	}

	public float getMinute() {
		return minute;
	}

	public String toString(){
		return "Day: " + day + " " + hour + ":" + minute;
	}
	
}
