package comp3111.examsystem;

/**
 * Grade item for every exam that every student took.
 * @author whwmaust2125
 * @since 2024-11-21
 */
public class Grade {
    private final String studentName;
    private final String courseNum;
    private final String examName;
    private final int score;
    private final int fullScore;
    private final int timeSpend;

    public String getStudentName() {
        return studentName;
    }
    public String getCourseNum() {
        return courseNum;
    }
    public String getExamName() {
        return examName;
    }
    public int getScore() { return score; }
    public int getFullScore() { return fullScore; }
    public int getTimeSpend() { return timeSpend; }

    public Grade() {
        this("student", "COMP3111", "final", 100, 100, 60);
    }

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