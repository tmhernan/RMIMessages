package server;

//package ser321.assign2.lindquis.server;


import java.io.FileInputStream;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import org.json.JSONObject;
import org.json.JSONTokener;

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
 * Purpose: To implement the MessageServerInterface.
 *
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Tiffany Hernandez Tiffany.Hernandez@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version January 2019
*/

public class MessageServerImpl implements java.io.Serializable, MessageServerInterface{
    private static final String patt = "EEE MMM d K:mm:ss yyyy";
    private String libName; //the name of the message library that has been manually made

    private static Vector<Message> messages = new Vector<Message>();//TO HOLD SENT MESSAGES
    private static Vector<Message> sentMessages = new Vector<Message>();//TO HOLD SENT MESSAGES

    Date today = new Date();   
    
    /**
     * Default Constructor
     * @throws RemoteException
     */
    public MessageServerImpl(){
      try{
        	this.libName = "noname";
         
      }catch(Exception e){
         	System.out.println("exception initializing employee store "+e.getMessage());
      }

     }  
     
     /**
     * Constructor
     * Used to read in json file to populate default messages.
     * @param String
     * @throws RemoteException
     */
    public MessageServerImpl(String fn){
        try {
             FileInputStream in = new FileInputStream(fn);
             JSONObject obj = new JSONObject(new JSONTokener(in));
             String [] dates = JSONObject.getNames(obj);
             
             //putting dates (library names) into a string 
             /*
             //This is actually printing off headers from each library
             //System.out.print("dates are: ");
             for(int j=0; j< dates.length; j++){
                System.out.print(dates[j]+", ");
             }
			 */
             for (int i=0; i< dates.length; i++){
                   Message aMessage = new Message((JSONObject)obj.getJSONObject(dates[i]));   
             }

        }catch(Exception ex){
            //System.out.print("Ex:" + ex.getMessage());
        }
    }
    

    public String getMessageBody(String header){
        Message mes = null;
        Vector<Message> messagesReceived;
        messagesReceived = mes.getMessagesReceived();
        String messageBody = "";
        
        for(Message m : messagesReceived) {
            if(m.getHeader().equals(header)) {
                messageBody = m.getMessageBody();
                break;
            }
        } 
        return messageBody;
    }
    
    public String getDate(String header){
        Message mes = null;
        Vector<Message> messagesReceived;
        messagesReceived = mes.getMessagesReceived();
        String date = "";
        
        for(Message m : messagesReceived) {
            if(m.getHeader().equals(header)) {
                date = m.getDate();
                break;
            }
        } 
        return date;
    }
    
    public String getSubject(String header){
        Message mes = null;
        Vector<Message> messagesReceived;
        messagesReceived = mes.getMessagesReceived();
        String subject = "";
        
        for(Message m : messagesReceived) {
            if(m.getHeader().equals(header)) {
                subject = m.getSubject();
                break;
            }
        } 
        return subject;
    }
    
    public String getName(String header){
        Message mes = null;
        Vector<Message> messagesReceived;
        messagesReceived = mes.getMessagesReceived();
        String name = "";
        
        for(Message m : messagesReceived) {
            if(m.getHeader().equals(header)) {
                name = m.getName();
                break;
            }
        }
        return name; 
    }
    
    public String getToUser(String header){
        Message mes = null;
        Vector<Message> messagesReceived;
        messagesReceived = mes.getMessagesReceived();
        String toUser = "";
        
        for(Message m : messagesReceived) {
            if(m.getHeader().equals(header)) {
                toUser = m.getToUser();
                break;
            }
        }
        return toUser;
    }
    
    
    public void setLibName(String aName){
        libName = aName;
     }

     /**
     * Json toString
     * Used to turn a string into a json string.
     * Used to populate default email messages.
     * @return String
     * 
     */
    public String toJSONString(){
        String r;
        JSONObject obj = new JSONObject();
        obj.put("libName", libName);
        
        for (Enumeration<Message> e = messages.elements(); e.hasMoreElements();) {
            Message m =(Message) e.nextElement();
            obj.put(m.getHeader(), m.toJSONObject());
        }
        r = obj.toString();
        //System.out.println("STRING toJSONString" + r);

        return r;
     }
    
     /**
     * Takes strings and makes them into Message Objects. 
     * Then, adds them to the sever's list of messages.
     * 
     * Used for the "Send Text" functionality. It's also
     * a shared method included in the interface.
     * @param Attributes that make up a Message Object
     * @throws RemoteException
     */
    public void addMessagetoLib(String name, String header, String message, String d, String s, String t) {
        
        if(libName.equals("Tue 18 Dec 5:32:29 2019")){
            messages.addElement(new Message(name, header, message, d, s, t ));
        }else {
        	//When a user selects "sent," a message object is made:
        	//Headers then get added to the headers string in the Message class
            sentMessages.addElement(new Message(name, header, message, d, s, t ));
        }
        
    }
    
    public boolean addMessagetoLib(Message m){
        
        boolean ret = false;
        
        if(libName.equals("Tue 18 Dec 5:32:29 2019")){
            messages.addElement(m);
            ret = messages.contains(m);
        }else {
            //When a user selects "sent," a message object is made:
            //Headers then get added to the headers string in the Message class
            sentMessages.addElement(m);
        }
        
        return ret;
        
    }  
    
    
     /**
     * Initiates messages by sending strings to become json
     * objects then adds them to the server's list of messages.
     * Used as default inbox messages for the users. 
     * @throws RemoteException
     */
    public void createLibrary() {
    	try{
		        MessageServerImpl mailIntake = new MessageServerImpl();
		        mailIntake.setLibName("Tue 18 Dec 5:32:29 2019");
		        
		        String message1 = "I see where you are going with this. "
		                + "Um, I do drink red wine. But I also drink white wine. "
		                + "And I have been known to sample the occasional rosÃ©. And"
		                + " a couple summers back I tried a merlot that used to be"
		                + " a chardonnay, which got a bit complicated.";
		        String h1 = "David.Rose  Tue 18 Dec 5:32:29 2018";
		        String d1 = "Tue 18 Dec 5:32:29 2018";
		        String s1 = "Please No";
		        
		        String message2 =  "I miss being surrounded by loose acquantances"
		                + "who think I am funny and smart and charming";
		        String h2 = "Alexis.Rose  Tue 18 Jan 5:32:29 2018";
		        String d2 = "Tue 18 Jan 5:32:29 2018";
		        String s2 = "Ew David";
		        
		        String message3 = "David, stop acting like a disgruntled pelican.";
		        String h3 = "Moira.Rose  Tue 18 Feb 5:32:29 2018";
		        String d3 = "Tue 18 Feb 5:32:29 2018";
		        String s3 = "No I will Not";
		        
		        String message4 = "You better remember which nails you pulled those"
		                + " wigs from because your mother keeps a spreadsheet.";
		        String h4 = "Johnny.Rose  Tue 18 Mar 5:32:29 2018";
		        String d4 = "Tue 18 Mar 5:32:29 2018";
		        String s4 = "We're getting out";
		        
		        String t = "Tim.Lindquist";
		        String t1 = "Jimmy.Buffett";

		        //SIMULATING SEND FUNCTION
		        mailIntake.addMessagetoLib("David Rose", h1, message1, d1, s1, t);
		        mailIntake.addMessagetoLib("Alexis Rose", h2, message2, d2, s2, t);
		        mailIntake.addMessagetoLib("Moira Rose", h3, message3, d3, s3, t1);
		        mailIntake.addMessagetoLib("Johnny Rose", h4, message4, d4, s4, t1);
		        
		        //System.out.print("json string"+ mailIntake.toJSONString());
		        
		        PrintWriter out = new PrintWriter("mess.json");
		        
		        //writes to the folder a json object
		        //And it also sends the message object in json
		        //to the message method to get deserialized.
		        out.println(mailIntake.toJSONString());
		        out.close();
		        //System.out.println("Done exporting group in json to messages.json");
		        
		        //creating actual library from file that is to be read in later
		        MessageServerImpl mes = new MessageServerImpl("mess.json");

		        Message m = new Message();
		        System.out.println("THE FOLLOWING MESSAGES HAVE BEEN IMPORTED FROM A JSON FILE: ");
		        m.printMessageLibrary();

		}catch(Exception e){
			System.out.println("exception: "+e.getMessage());
       		//e.printStackTrace();
		}   
    }
     /**
     * Gets the array of strings that holds all headers for 
     * messages on the server's side. Then fiters out those 
     * headings according to the user passed in. 
     * Used so the GUI can filter out messages for that user.
     * @param String
     * @throws RemoteException 
     * @returns String Array
     */
    public String[] getMessageFromHeaders(String toAUserName){

        System.out.println("USERNAME IN MESSAGE CLASS" + toAUserName);
        
        String [] a = new String [100];
        Message v = new Message();
        Vector<Message> messagesReceived = v.getMessagesReceived();
        
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
     /**
     * Returns the message object with a matching header from
     * the list of messages on the server side. 
     * It's also a shared method for the interface.
     * Used so the GUI can show all message components when a 
     * user wishes to see it.
     * @param String
     * @throws RemoteException
     * @returns Message Object
     */
    public Message getMessage(String header) {
        
        Message mes = null;
        Message v = new Message();
        Vector<Message> messagesReceived = v.getMessagesReceived();
        for(Message m : messagesReceived) {
            if(m.getHeader().equals(header)) {
                mes = m;
            }
        }
        return mes;
    }
    
     /**
     * Deletes a message object on the back end, off
     * of the list of messages on the servers side.
     * It is also a shared method on the interface.
     * @param Strings
     * @returns boolean
     */
    @Override
    public boolean deleteMessage(String header, String toAUserName){
        Boolean rem_elem = false;
        
        System.out.println("HEADER TO BE REMOVED: " + header);
        System.out.println("TOAUSERNAME TO BE REMOVED: " + toAUserName);
        
        Message v = new Message();
        Vector<Message> messagesReceived = v.getMessagesReceived();
        
        for(Message m : messagesReceived) {
            System.out.println("header for this message: " + m.getHeader());
            System.out.println("toUser for this message: " + m.getToUser());
            if((m.getHeader().equalsIgnoreCase(header)) && (m.getToUser().equalsIgnoreCase(toAUserName))) {
                System.out.println("There is a match! ");
                rem_elem = messagesReceived.remove(m);
                System.out.println("Object removed: " + rem_elem);
                break;
            }
            System.out.println();
        }
        System.out.println("The remaining messages for this " + toAUserName + " in the list are: ");
        
        v.printMessageLibrary(toAUserName);
        
        return false;
    }

    @Override
    public Vector<Message> getMessagesReceived() {
        // TODO Auto-generated method stub
        return null;
    }


}


