package deadlybanquet.model;

/**
 * Created by Hampus on 2016-03-21.
 */
//Placeholder object has negative day value
public class Time {
    
    private int hour, day;
    private float minutes;

    public Time(){
        hour = 0;
        day = 0;
        minutes = 0;
    }
    
    public Time(int d, int h, float m){
        day=d;
        hour=h;
        minutes=m;
    }

    public TimeStamp time(){
        return new TimeStamp(hour,day,minutes);
    }

    public void incrementTime(float deltaTime){
        //1 second real time => 1 minute
        minutes+= 0.001*deltaTime; //This line decides the speed of the time
        if(minutes >= 60){
            minutes = 0;
            hour++;
            if(hour > 23){
                hour = 0;
                day++;
            }
        }
    }

    public void forceSetTime(int day, int hour, int minutes){
        this.day = day;
        this.hour = hour;
        this.minutes = (float)minutes;
    }
}
