package ser321.assign2.lindquis.server;


import java.io.Serializable;
import java.util.Vector;

import org.json.JSONObject;

import java.rmi.server.*;
import java.rmi.*;

/*
 * Copyright 2019 Tiffany Hernandez,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Purpose: To hold message objects for the server.
 *
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Tiffany Hernandez Tiffany.Hernandez@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version January 2019
*/

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

    /**
     * Default Constructor
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
    
    /**
     * Parameterized constructor
     *  @param Items to make a email message
     */
    public Message(String name, String header, String messageBody, String d, String s, String u)throws RemoteException  {     
        this.name = name;
        this.header = header;
        this.messageBody = messageBody;
        this.date = d;
        this.subject = s;
        this.toUser = u;
        
        //header gets added when creation of message object
        for(int i = 0; i < headers.length; i++) {
            if(headers[i] == null) {
                headers[i] = header;
                break;
            }
        }
        
        for(int i = 0; i < headers.length; i++) {//not needed
            if(headers[i] != null) {
                //System.out.print("TESTING HEADERS" + headers[i]);
            }
        }

        
        messagesReceived.add(this);
        //printMessageLibrary();
    }

    /**
     * Constructor used for the inital emails from json objects.
     * Takes jsonobject and makes them message objects.
     *  @param JSONobject
     */
    public Message(JSONObject obj) throws RemoteException {
        
        name = obj.getString("name");
        header = obj.getString("header");
        messageBody = obj.getString("messageBody");
        date = obj.getString("date");
        subject = obj.getString("subject");
        toUser = obj.getString("toUser");
        

        //printMessageLibrary();
        //System.out.println("SIZE OF JSON MESSAGE LIST: " + messagesReceived.size());
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
    
    /**
     * Used for the initial email messages to put them
     * in JSONObjects from strings.
     * Can be used by a string to make them JSONObjects.
     * @return JSONObject
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

    /**
     * Takes the "to" field in the message object 
     * and prints off all messages for that user. 
     * 
     * Used for debugging on the server side.
     * @param String
     */
    public void printMessageLibrary(String toAUserName){
        
        //System.out.println("is list empty: " + messagesReceived.isEmpty());
        
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

    /**
     * Prints all messages on the server side. 
     * 
     * Used for debugging on the server side.
     * 
     */    
    public void printMessageLibrary(){
        
        //System.out.println("is list empty: " + messagesReceived.isEmpty() + "ALL MESSAGES");
        
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
