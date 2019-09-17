package server;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import org.json.JSONObject;

import java.rmi.server.*;
import java.rmi.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Set;


public class MessageServerImpl extends UnicastRemoteObject{
    
    private String name; //lib name
    private String header; //unique key for each message

    private static Vector<Message> messages = new Vector<Message>(); 
    private String [] headers = new String[100];
    private Message initial;

    /**
    *
    *
    */
    public MessageServerImpl() throws RemoteException {
      initial = null;
      try{
         File inFile = new File("messages.ser");
         if(inFile.exists()){
            ObjectInputStream is = 
               new ObjectInputStream(new FileInputStream(inFile));
            //empList = (Hashtable)is.readObject();
         }else{
	        String message1 = "I see where you are going with this. "
	                + "Um, I do drink red wine. But I also drink white wine. "
	                + "And I have been known to sample the occasional rosé. And"
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
	        
	        String t = "Tiffany.Hernandez";
	        
	        //mailIntake.addMessagetoLib("David Rose", h1, message1, d1, s1, t);
	        //mailIntake.addMessagetoLib("Alexis Rose", h2, message2, d2, s2, t);
	        //mailIntake.addMessagetoLib("Moira Rose", h3, message3, d3, s3, t);
	        //mailIntake.addMessagetoLib("Johnny Rose", h4, message4, d4, s4, t);
         }
      }catch(Exception e){
         System.out.println("exception initializing employee store "+e.getMessage());
      }

     }  
    
    /*Reads in a file. Initializes a library as well as 
     * calls the message class to make messages out of 
     * the json content. Is not utilized here yet, because
     * messages.json was not read in. Instead, I used the
     * createLibrary method in this class and the toJSONString 
     * method in this class to deserialize the messages from
     * a json object. 
     * 
     * For now, it throws an error because of the way it's trying to
     * iterate through json. When the "send" button is utilized on
     * a different client, this will work to receive messages in.
     */
    public MessageServerImpl(String fn) throws RemoteException{
        try {
            FileInputStream recieve = new FileInputStream(fn);
            JSONObject obj = new JSONObject();
            
            Message a = new Message(obj);

            String [] headers = JSONObject.getNames(a);
            
            System.out.println("Headers for json String: ");
            for(int j=0; j< headers.length; j++){
                System.out.print(headers[j]+", ");
            }
            recieve.close();
        
        }catch(Exception ex){
            System.out.print("Ex:" + ex.getMessage());
        }
    }
    
    public void setName(String aName){
        name = aName;
     }
    
    public Vector<Message> getMessages() {
        return messages;
    }
    
    public String[] getHeaders() {
        return headers;
    }


    /*Method to put a json message library in a string.
     * 
     */
    public String toJSONString(){
        String r;
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        
        for (Enumeration<Message> e = messages.elements(); e.hasMoreElements();) {
            Message m = (Message) e.nextElement();
            obj.put(m.getHeader(), m.toJSONObject());
        }
        r = obj.toString();
        System.out.println("string" + r);
        return r;
         
     }
    
    /*This will be used for sent messages but for this assignment was utilized 
     * to put messages for this user to read.
     */
    public void addMessagetoLib(String name, String header, String message, String d, String s, String t) {
        
        messages.addElement(new Message(name, header, message, d, s, t ));
        
    }
    
    public void printMessageLibrary(){

        System.out.println("Message Library " + name + " has the following headers: ");
        for(Enumeration e = messages.elements(); e.hasMoreElements();) {
            System.out.print((((Message) e.nextElement()).getHeader()) + ", ");
        }
        System.out.println();

     }
    
    public void printMessages() {
        
        System.out.println("Iterating thorough vector Messages: ");
        System.out.println(messages.isEmpty());
        for(Message m : messages) {
            System.out.println(m.getHeader());
        }
    }
    /*Method is currently used to populate the list of messages for
     * the UI to read from. To eleborate, it sends the message library
     * and all of it's contents to the jsontoString to get read by the
     * messsage class.
     * 
     * It also writes it to a json file. 
     */
    public void createLibrary() throws FileNotFoundException {
        /*
        MessageServerImpl mailIntake = new MessageServerImpl();
        mailIntake.setName("first set");
        
        String message1 = "I see where you are going with this. "
                + "Um, I do drink red wine. But I also drink white wine. "
                + "And I have been known to sample the occasional rosé. And"
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
        
        String t = "Tiffany.Hernandez";
        
        mailIntake.addMessagetoLib("David Rose", h1, message1, d1, s1, t);
        mailIntake.addMessagetoLib("Alexis Rose", h2, message2, d2, s2, t);
        mailIntake.addMessagetoLib("Moira Rose", h3, message3, d3, s3, t);
        mailIntake.addMessagetoLib("Johnny Rose", h4, message4, d4, s4, t);
        
        System.out.print("json string"+ mailIntake.toJSONString());
        
        PrintWriter out = new PrintWriter("messages.json");
        
        //writes to the folder a json object
        //And it also sends the message object in json
        //to the message method to get deserialized.
        out.println(mailIntake.toJSONString());
        out.close();
        System.out.println("Done exporting group in json to messages.json");
        mailIntake.printMessageLibrary();

        // Input the group from admin.json
        System.out.println("Importing group from messages.json");
        
        //creating actual library from file that is to be read in later
        MessageServerImpl mes = new MessageServerImpl("messages.json");
        
        mailIntake.printMessages();
        */
    
    }

    public static void main(String args[]){
      try {
         String hostId="localhost";
         String regPort="1099";
         if (args.length >= 2){
	    hostId=args[0];
            regPort=args[1];
         }
         //System.setSecurityManager(new RMISecurityManager()); // rmisecmgr deprecated
         MessageServerImpl obj = new MessageServerImpl();
         Naming.rebind("rmi://"+hostId+":"+regPort+"/EmployeeServer", obj);
         System.out.println("Server bound in registry as: "+
                            "rmi://"+hostId+":"+regPort+"/EmployeeServer");
      }catch (Exception e) {
         e.printStackTrace();
      }

    }



}


