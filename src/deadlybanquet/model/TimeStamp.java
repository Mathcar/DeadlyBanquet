package deadlybanquet.model;

public class TimeStamp {
	
    private int hour, day;
    private float minutes;
	
    public TimeStamp(int d, int h, float m){
        day=d;
        hour=h;
        minutes=m;
    }
    
    public void incrementTime(float deltaTime){
        //1 second real time => 1 minute
        minutes+= deltaTime; 
        if(minutes >= 60){
            minutes = 0;
            hour++;
            if(hour > 23){
                hour = 0;
                day++;
            }
        }
    }
	
    public int getHour() {
            return hour;
    }

    public int getDay() {
            return day;
    }

    public float getMinute() {
            return minutes;
    }
    
    public boolean isPlaceHolder(){
        return day<0;
    }
    
    public TimeStamp copy(){
        return new TimeStamp(day,hour,minutes);
    }

    public String toString(){
            return "Day: " + day + " " + hour + ":" + minutes;
    }
    
    public TimeStamp placeholderTime(){
        TimeStamp t = new TimeStamp(-1,0,0);
        return t;
    }
    
    
	
}
