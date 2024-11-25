package comp3111.examsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    @Test
    void testEquals1() {
        Manager manager1 = new Manager();
        Manager manager2 = new Manager();
        assertFalse(manager1 == manager2);
    }

    @Test
    void testEquals2() {
        Manager manager = new Manager();
        assertFalse(manager == null);
    }

    @Test
    void testUpdate() {
        Manager manager = new Manager("admin", "comp3111");
        manager.update("whwma", "comp3211");
        Manager expected = new Manager("whwma", "comp3211");
        assertEquals(expected, manager);
    }

    @Test
    void testToString() {
        Manager manager = new Manager("admin", "comp3111");
        String output = manager.toString();
        String expected = "{\"username\":\"admin\",\"password\":\"comp3111\"}";
        assertEquals(expected, output);
    }

    @Test
    void testSetPassword() {
        Manager manager = new Manager("admin", "comp3111");
        manager.setPassword("comp3711");
        Manager expected = new Manager("admin", "comp3711");
        assertEquals(expected, manager);
    }

    @Test
    void testSetPassword_empty() {
        Manager manager = new Manager("admin", "comp3111");
        try {
            manager.setPassword("");
        } catch (Exception e) {

        }
    }

    @Test
    void testSetUsername() {
        Manager manager = new Manager("admin", "comp3111");
        manager.setUsername("whwma");
        Manager expected = new Manager("whwma", "comp3111");
        assertEquals(expected, manager);
    }

    @Test
    void testSetUsername_empty() {
        Manager manager = new Manager("admin", "comp3111");
        try {
            manager.setUsername("");
        } catch (Exception e) {

        }
    }

    @Test
    void testGetUsername() {
        Manager manager = new Manager("admin", "comp3111");
        String output = manager.getUsername();
        String expected = "admin";
        assertEquals(expected, output);
    }

    @Test
    void testGetPassword() {
        Manager manager = new Manager("admin", "comp3111");
        String output = manager.getPassword();
        String expected = "comp3111";
        assertEquals(expected, output);
    }
}