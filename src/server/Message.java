package server;


import java.io.Serializable;
import java.util.Vector;

import org.json.JSONObject;

import java.rmi.server.*;
import java.rmi.*;


public class Message extends Object implements Serializable, MessageServerInterface{
    
    private static final long serialVersionUID = 3415902006212375222L;
    private String name;
    private String header;
    private String messageBody;
    private String date;
    private String subject;
    private String toUser;

    private String [] headers = new String[100];
    //holds message objects sent from the createLibrary method in 
    //Message Library class.
    private static Vector<Message> messagesReceived = new Vector<Message>(); 

    /*Default constructor
     * 
     */
    public Message() {
        this.name = null;
        this.header = null;
        this.messageBody = null;
        this.date = null;
        this.subject = null;
        this.toUser = null;
    }
    
    /*Method to be used for the emails sent from the user however
     * it has been temporarily used for the purposes of this 
     * assignment to make message objects for the UI.      
     */
    public Message(String name, String header, String messageBody, String d, String s, String u) {     
        this.name = name;
        this.header = header;
        this.messageBody = messageBody;
        this.date = d;
        this.subject = s;
        this.toUser = u;
        
        for(int i = 0; i < headers.length; i++) {//not needed
            if(headers[i] == null) {
                headers[i] = header;
            }
        }
    }
    /*Method to make a messages received vector list from the 
     * json imput stream. The UI did not utilize this vector 
     * but it easily could have. 
     * 
     * The information coming through here for this assignment
     * is received by the createLibrary method that sends string 
     * input to the toJSONString method which then sends it here.
     */
    public Message(JSONObject obj) {
        
        name = obj.getString("name");
        header = obj.getString("header");
        messageBody = obj.getString("messageBody");
        date = obj.getString("date");
        subject = obj.getString("subject");
        toUser = obj.getString("toUser");
        
        Message n = new Message(name, header, messageBody, date, subject, toUser);
        
        messagesReceived.add(n);
    }
    
    public String getHeader() {
        
        return header;
    }
    
    public String getMessageBody(){
        
        return messageBody;
    }
    
    public String getDate(){
        
        return date;
    }
    
    public String getSubject(){
        
        return subject;
    }
    
    public String getName(){
        
        return name;
    }
    
    public String getToUser(){
        
        return toUser;
    }
    
    /*This method is to be used for this user's
     * sent messages. They will be turned into 
     * json strings then objects to be sent out.
     */
    public JSONObject toJSONObject() {
        
        JSONObject o = new JSONObject();
        o.put("name", name);
        o.put("header", header);
        o.put("messageBody", messageBody);
        o.put("date", date);
        o.put("subject", subject);
        o.put("toUser", toUser);

        
        return o;
    }
    
    // getMessageFromHeaders returns a string array of message headers being sent to toAUserName.
    // Headers returned are of the form: (from user name @ server and message date)
    // e.g., a message from J Buffett with header: Jimmy.Buffet  Tue 18 Dec 5:32:29 2018
    public String[] getMessageFromHeaders(String toAUserName)throws RemoteException{
        String [] a = new String [100];
        int i = 0;
        try{
        MessageServerImpl lib = new MessageServerImpl();
        Vector<Message> messages = lib.getMessages();
        for(Message m : messages) {
            if(m.getToUser().equals(toAUserName)) {
                    while(a[i] != null) {
                        i++;
                    }
                    a[i] = m.getHeader();
            }
        }
        } catch(Exception e){
          System.out.println("exception initializing employee store "+e.getMessage());
           
        }        
        return a;
    }
    
    /* 
     * getMessage returns the Message having the corresponding header. Assume headers are unique.
     *As above, the header has includes (from user name - server and message date)
     */
    public Message getMessage(String h) throws RemoteException{
        Message mes = null;
        try{
            MessageServerImpl lib = new MessageServerImpl();
            Vector<Message> messages = lib.getMessages();
            for(Message m : messages) {
                if(m.getHeader().equals(h)) {
                    mes = m;
                }
            }
        }catch(Exception ex){
            System.out.print("Ex:" + ex.getMessage());

        }

        return mes;
        
    }
    /*
     * Method deletes the message from the MessagesReceived vector list
     * 
     */
    public boolean deleteMessage(String header, String toAUserName) {
        for(Message m : messagesReceived) {
            if( m.getHeader() == header) {
                messagesReceived.remove(m);
            }
            
        }

        return false;
    }


}
