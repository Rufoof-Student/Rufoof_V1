package dev.Program.Backend.BusinessLayer.ServerMessages;

public class Question {
    public String data;
    public String type;
    public final String tag="Question";

    public Question(String question){
        this.type=question;
        data =  "";
    }

    public Question(String question , String data){
        this.type = question;
        this.data = data;
    }
}
