package ser321.assign2.lindquis.server;


import java.io.Serializable;
import java.util.Vector;

import org.json.JSONObject;

import java.rmi.server.*;
import java.rmi.*;


public class Message extends Object implements java.io.Serializable{
    
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
    private static Vector<Message> messagesReceived = new Vector<Message>(); //from json file

    /*Default constructor
     * 
     */
    public Message() throws RemoteException {
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
    public Message(String name, String header, String messageBody, String d, String s, String u)throws RemoteException  {     
        this.name = name;
        this.header = header;
        this.messageBody = messageBody;
        this.date = d;
        this.subject = s;
        this.toUser = u;
        
        for(int i = 0; i < headers.length; i++) {
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
    public Message(JSONObject obj) throws RemoteException {
        
        name = obj.getString("name");
        header = obj.getString("header");
        messageBody = obj.getString("messageBody");
        date = obj.getString("date");
        subject = obj.getString("subject");
        toUser = obj.getString("toUser");
        
        Message n = new Message(name, header, messageBody, date, subject, toUser);
        
        messagesReceived.add(n);
        printMessageLibrary();
        System.out.println("SIZE OF MESSAGE LIST: " + messagesReceived.size());
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

    public Vector getMessagesReceived(){
        return messagesReceived;
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
    /*
    // getMessageFromHeaders returns a string array of message headers being sent to toAUserName.
    // Headers returned are of the form: (from user name @ server and message date)
    // e.g., a message from J Buffett with header: Jimmy.Buffet  Tue 18 Dec 5:32:29 2018
    public String[] getMessageFromHeaders(String toAUserName)throws RemoteException{
        System.out.println("USERNAME IN MESSAGE CLASS" + toAUserName);
        String [] a = new String [100];
        int i = 0;
        for(Message m : messagesReceived) {
            if(m.getToUser().equals(toAUserName)) {
                    while(a[i] != null) {
                        i++;
                    }
                    a[i] = m.getHeader();
            }
        }        
        return a;
    }
    

    public Message getMessage(String h) throws RemoteException{
         Message mes = null;
        for(Message m : messagesReceived) {
            if(m.getHeader().equals(h)) {
                mes = m;
            }
        }
        return mes;
        
    }

    public boolean deleteMessage(String header, String toAUserName) {
         Boolean rem_elem = false;
        
        System.out.println("HEADER TO BE REMOVED: " + header);
        System.out.println("TOAUSERNAME TO BE REMOVED: " + toAUserName);
        
        for(Message m : messagesReceived) {
            System.out.println("header for this message: " + m.getHeader());
            System.out.println("toUser for this message: " + m.getToUser());
            if((m.getHeader().equalsIgnoreCase(header)) && (m.getToUser().equalsIgnoreCase(toAUserName))) {
                System.out.println("There is a match! ");
                rem_elem = messagesReceived.remove(m);
                break;
            }
            System.out.println("Object removed: " + rem_elem);
            System.out.println();
        }
        System.out.println("The remaining messages for this " + toAUserName + " in the list are: ");
        printMessageLibrary(toAUserName);
        
        return false;
    }

    */
    //prints out library when a message is added or deleted
    public void printMessageLibrary(String toAUserName){
        
        System.out.println("is list empty: " + messagesReceived.isEmpty());
        
        for(Message obj: messagesReceived) {
            if(obj.getToUser().equalsIgnoreCase(toAUserName)) {    
                System.out.println("Sent from: " + obj.getName());
                System.out.println("Header:    " + obj.getHeader());
                System.out.println("Date:      " + obj.getDate());
                System.out.println("To User:   " + obj.getToUser());
                System.out.println("Subject:   " + obj.getSubject());
                System.out.println("Message Body: "+ obj.messageBody);

            }
                System.out.println();
        }
        System.out.println();
     }
    
    public void printMessageLibrary(){
        
        System.out.println("is list empty: " + messagesReceived.isEmpty() + "ALL MESSAGES");
        
        for(Message obj: messagesReceived) {
                System.out.println("Sent from: " + obj.getName());
                System.out.println("Header:    " + obj.getHeader());
                System.out.println("Date:      " + obj.getDate());
                System.out.println("To User:   " + obj.getToUser());
                System.out.println("Subject:   " + obj.getSubject());
                System.out.println("Message Body: "+ obj.messageBody);

                System.out.println();
        }
        System.out.println();
     }

}
