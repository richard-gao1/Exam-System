package comp3111.examsystem;


public class Question {
    enum TypeChoice{ // enum for question type, do we have long question?
        SINGLE ,
        MULTIPLE
    }
    private String content;
    private String[] options = new String[4]; //Only options ABCD? How about short/long answer?
    private String answer;
    private int score;
    private TypeChoice typeChoice;

    public Question(String content, String[] options, String answer, int score, int typeChoice){
        this.content = content;
        if (options == null) {
            this.options = new String[4];
        } else {
            System.arraycopy(options,0,this.options,0,4);
        }
        this.score = score;
        this.typeChoice = (typeChoice > 0) ? TypeChoice.MULTIPLE : TypeChoice.SINGLE;
        if (typeChoice == 0 && answer.length() > 1) {
                throw new IllegalArgumentException("Single choice question must have one answer");
            }
        this.answer = answer;
        
    }

    public String getQuestion(){
        return this.content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String[] getOptions(){
        return this.options;
    }

    public void setOptions(String[] options){
        System.arraycopy(options,0,this.options,0,4);
    }

    public String getAnswer(){
        return this.answer;
    }

    public void setAnswer(String answer){
        if (this.typeChoice == TypeChoice.SINGLE && answer.length() > 1) {
            throw new IllegalArgumentException("Single choice question must have one answer");
        }
        this.answer = answer;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getTypeChoice(){
        return (this.typeChoice == TypeChoice.MULTIPLE)? 0:1;
    }
    
    public void setTypeChoice(int typeChoice){
        this.typeChoice = (typeChoice > 0) ? TypeChoice.MULTIPLE : TypeChoice.SINGLE;
    }
    

