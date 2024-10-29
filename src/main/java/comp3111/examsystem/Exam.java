package comp3111.examsystem;

import java.util.ArrayList;
import java.util.HashMap;

public class Exam {
    // not sure if Student is hashable, if it isn't we can use name (String)
    private HashMap<Student, Integer> grades = new HashMap<Student, Integer>();
    private ArrayList<Question> questions;
}
