package comp3111.examsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeTest {

    @Test
    void getCurrentTime() {
        Time time = new Time(10,0,0);
        assertEquals(time.getCurrentTime(),"10:00:00");
    }

    @Test
    void getTotalTime() {
        Time time = new Time(1,59,59);
        assertEquals(time.getTotalTime(),59+59*60+60*60);
    }

    @Test
    void oneSecondPassed() {
        Time time = new Time(23,59,59);
        time.oneSecondPassed();
        assertEquals(time.getCurrentTime(),"00:00:00");
    }

    @Test
    void second(){
        Time time = new Time(12,20,0);
        time.oneSecondPassed();
        assertEquals(time.getCurrentTime(),"12:20:01");
    }
}