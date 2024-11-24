package comp3111.examsystem;

/**
 * Time class for storing time value
 */
public class Time {
    private int hour;
    private int minute;
    private int second;

    /**
     * Initializes the Time object with hour, minute and second
     *
     * @param hour   an integer of hours
     * @param minute an integer of minute
     * @param second an integer of second
     */
    public Time(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    /**
     * Initializes the Time object
     *
     * @param currentTime a string of current time
     */
    public Time(String currentTime) {
        String[] time = currentTime.split(":");
        hour = Integer.parseInt(time[0]);
        minute = Integer.parseInt(time[1]);
        second = Integer.parseInt(time[2]);
    }

    /**
     * Retrieves the current time
     *
     * @return a string of current time
     */
    public String getCurrentTime(){
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * Retrieves the current time
     *
     * @return an integer of time in seconds
     */
    public int getTotalTime(){
        return hour * 3600 + minute * 60 + second;
    }

    /**
     * Increments the time by 1 second
     */
    public void oneSecondPassed(){
        second++;
        if(second == 60){
            minute++;
            second = 0;
            if(minute == 60){
                hour++;
                minute = 0;
                if(hour == 24){
                    hour = 0;
                    System.out.println("Next day");
                }
            }
        }
    }
}
