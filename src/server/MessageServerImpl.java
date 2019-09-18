package server;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import org.json.JSONObject;
import org.json.JSONTokener;


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
import java.util.Date;



public class MessageServerImpl extends UnicastRemoteObject{
    private static final String patt = "EEE MMM d K:mm:ss yyyy";
    private String libName; //the name of the message library that has been manually made

    private static Vector<Message> messages = new Vector<Message>();//TO HOLD SENT MESSAGES
    Date today = new Date();   


    /**
    *
    *
    */
    public MessageServerImpl() throws RemoteException {
      try{
        	this.libName = "noname";
         
      }catch(Exception e){
         	System.out.println("exception initializing employee store "+e.getMessage());
      }

     }  
    
    public MessageServerImpl(String fn) throws RemoteException{
        try {
             FileInputStream in = new FileInputStream(fn);
             JSONObject obj = new JSONObject(new JSONTokener(in));
             String [] dates = JSONObject.getNames(obj);
             
             //putting dates (library names) into a string 
             //This is actually printing off headers from each library
             System.out.print("dates are: ");
             for(int j=0; j< dates.length; j++){
                System.out.print(dates[j]+", ");
             }

             for (int i=0; i< dates.length; i++){
                   Message aMessage = new Message((JSONObject)obj.getJSONObject(dates[i]));   
             }

        }catch(Exception ex){
            System.out.print("Ex:" + ex.getMessage());
        }
    }
    
    public void setLibName(String aName){
        libName = aName;
     }

    /*Method to put a json message library in a string.
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
        System.out.println("STRING toJSONString" + r);

        return r;
     }
    
    /*This will be used for sent messages but for this assignment was utilized 
     * to put messages for this user to read.
     */
    public void addMessagetoLib(String name, String header, String message, String d, String s, String t) {
        
        messages.addElement(new Message(name, header, message, d, s, t ));
        
    }
    
    public void printMessageLibrary(){

        System.out.println("Message Library " + libName + " has the following headers: ");
        for(Enumeration e = messages.elements(); e.hasMoreElements();) {
            System.out.print((((Message) e.nextElement()).getHeader()) + ", ");
        }
        System.out.println();
     }
    
     /*
     *create initial library of messages
     *
     */
    public void createLibrary() throws FileNotFoundException, RemoteException {
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
        
        System.out.print("json string"+ mailIntake.toJSONString());
        
        PrintWriter out = new PrintWriter("mess.json");
        
        //writes to the folder a json object
        //And it also sends the message object in json
        //to the message method to get deserialized.
        out.println(mailIntake.toJSONString());
        out.close();
        System.out.println("Done exporting group in json to messages.json");
        
        //creating actual library from file that is to be read in later
        MessageServerImpl mes = new MessageServerImpl("mess.json");    
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

         //Initialize the library of messages:
         obj.createLibrary();
         Naming.rebind("rmi://"+hostId+":"+regPort+"/EmployeeServer", obj);
         System.out.println("Server bound in registry as: "+
                            "rmi://"+hostId+":"+regPort+"/EmployeeServer");
      }catch (Exception e) {
         e.printStackTrace();
      }

    }



}


