package comp3111.examsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {
    Question q;
    @BeforeEach
    void set(){
        SystemDatabase.removeAll();
        SystemDatabase a = new SystemDatabase();
        q = new Question("Tes",new String[]{"a","c","fw","dq"},"A",10,"Single");
    }

    @Test
    void setContent() {
        q.setContent("test");
        assertEquals(q.getContent(),"test");
    }

    @Test
    void setOptions() {
        ArrayList<String> ops = new ArrayList<>();
        ops.add("a");
        ops.add("b");
        ops.add("a");
        ops.add("b");
        q.setOptions(ops);
        assertEquals(q.getOptions(),ops);
        Question q2 = new Question("q",ops,"A",1,"Single");
    }

    @Test
    void testSetOptions() {
        String[] op = new String[]{"a","b","a","b"};
        q.setOptions(op);
        assertEquals(q.getOptions().getFirst(),op[0]);
        assertEquals(q.getOptions().get(1),op[1]);
    }

    @Test
    void setAnswer() {
        String ans = "D";
        q.setAnswer(ans);
        assertEquals(q.answerProperty().get(),ans);
    }

    @Test
    void testSetAnswer() {
        int ans = 3;
        q.setTypeChoice(1);
        q.setAnswer(ans);
        assertEquals(q.answerProperty().get(),"AB");
    }

    @Test
    void testSetAnswerFail() {
        int ans = 3;
        assertThrows(IllegalArgumentException.class,()->q.setAnswer(ans));
    }

    @Test
    void setScore() {
    }

    @Test
    void setTypeChoice() {
    }

    @Test
    void testSetTypeChoice() {
    }

    @Test
    void contentProperty() {
    }

    @Test
    void answerProperty() {
    }

    @Test
    void scoreProperty() {
    }

    @Test
    void typeChoiceProperty() {
    }
}