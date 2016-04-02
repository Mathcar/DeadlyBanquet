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
    
    public Time placeholderTime(){
        Time t = new Time();
        t.forceSetTime(-1, 0, 0);
        return t;
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

    public int getHour(){
        return hour;
    }

    public int getMinute(){
        return (int)minutes;
    }

    public int getDay(){
        return day;
    }


}
