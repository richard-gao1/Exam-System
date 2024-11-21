package comp3111.examsystem;

/**
 * Represents a grade record for a student after taking an exam.
 * @author whwmaust2125
 */
public class Grade {
    private final String studentName; // The name of the student who took the exam.
    private final String courseNum;   // The number or identifier of the course.
    private final String examName;    // The name of the exam taken.
    private final int score;          // The score achieved by the student on the exam.
    private final int fullScore;      // The maximum possible score on the exam.
    private final int timeSpend;      // The amount of time the student spent taking the exam.

    /**
     * Retrieves the name of the student who took the exam.
     *
     * @return The student's name as a String.
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * Retrieves the course number or identifier for which the exam was taken.
     *
     * @return The course number as a String.
     */
    public String getCourseNum() {
        return courseNum;
    }

    /**
     * Retrieves the name of the exam taken by the student.
     *
     * @return The exam name as a String.
     */
    public String getExamName() {
        return examName;
    }

    /**
     * Retrieves the score achieved by the student on the exam.
     *
     * @return The score as an integer value.
     */
    public int getScore() { return score; }

    /**
     * Retrieves the maximum possible score on the exam.
     *
     * @return The full score as an integer value.
     */
    public int getFullScore() { return fullScore; }

    /**
     * Retrieves the amount of time the student spent taking the exam.
     *
     * @return The time spent in minutes as an integer value.
     */
    public int getTimeSpend() { return timeSpend; }

    /**
     * Constructs a new Grade object with default values for all fields except
     * studentName, which is set to "student", courseNum set to "COMP3111",
     * examName set to "final", and other numeric fields initialized to 100 for fullScore,
     * score, and 60 for timeSpend.
     */
    public Grade() {
        this("student", "COMP3111", "final", 100, 100, 60);
    }

    /**
     * Constructs a new Grade object with the specified values for all fields.
     *
     * @param studentName The name of the student who took the exam.
     * @param courseNum   The number or identifier of the course.
     * @param examName    The name of the exam taken.
     * @param score       The score achieved by the student on the exam.
     * @param fullScore   The maximum possible score on the exam.
     * @param timeSpend   The amount of time the student spent taking the exam in minutes.
     */
    public Grade(
            String studentName,
            String courseNum,
            String examName,
            int score,
            int fullScore,
            int timeSpend
    ) {
        this.studentName = studentName;
        this.courseNum = courseNum;
        this.examName = examName;
        this.score = score;
        this.fullScore = fullScore;
        this.timeSpend = timeSpend;
    }
}