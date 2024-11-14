package comp3111.examsystem;

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
    public String getScore() {
        return String.valueOf(score);
    }
    public String getFullScore() {
        return String.valueOf(fullScore);
    }
    public String getTimeSpend() {
        return String.valueOf(timeSpend);
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