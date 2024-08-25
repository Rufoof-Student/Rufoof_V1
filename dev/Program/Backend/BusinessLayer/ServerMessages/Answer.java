package dev.Program.Backend.BusinessLayer.ServerMessages;

public class Answer  {
    public  final String tag="Answer";
    public String type;
    public String data;
    public Answer(String type, String data){
        this.data=data;
        this.type= type;
    }
}
