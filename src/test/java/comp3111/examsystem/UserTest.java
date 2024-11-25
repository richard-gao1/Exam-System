package comp3111.examsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void setName() {
        User user = new Student("wktangaf","1234","Kenny",Gender.list[1],18 ,"CSE");
        user.setName("Kenny Tang");
        assertEquals(user.getName(),"Kenny Tang");
    }

    @Test
    void setNullName() {
        User user = new Student("wktangaf","1234","Kenny",Gender.list[1],18 ,"CSE");
        assertThrows(IllegalArgumentException.class,
                ()->user.setName(""));

    }

    @Test
    void setGender() {
        User user = new Student("wktangaf","1234","Kenny",Gender.list[1],18 ,"CSE");
        user.setGender(Gender.list[0]);
        assertEquals(user.getGender(),"Male");
    }

    @Test
    void setAge() {
        User user = new Student("wktangaf","1234","Kenny",Gender.list[1],18 ,"CSE");
        user.setAge(100);
        assertEquals(user.getAge(),100);
    }

    @Test
    void setNegativeAge() {
        User user = new Student("wktangaf","1234","Kenny",Gender.list[1],18 ,"CSE");
        assertThrows(IllegalArgumentException.class,
                ()->user.setAge(-1));
    }

}