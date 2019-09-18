//package ser321.assign2.lindquis.client;

import server.*;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.rmi.Naming;


import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class MessageClientGUI extends ser321.assign2.lindquis.client.MessageGui
                           implements ActionListener, ListSelectionListener {

   private String user;   // originator of all message sent by this client.
   private String serverHostPort; // such as lindquisrpi.local:8080
   private static final String patt = "EEE MMM d K:mm:ss yyyy";
   private String [] headers = new String[100];
   DefaultListModel<String> dlm = (DefaultListModel<String>)messageListJL.getModel();

   

   public MessageClientGUI(String hostId, String regPort, String user) {
    super("Dr. Lindquist", user);
    this.user = user;
    System.out.println("USER IS: " + user);
    
    try{   
          //REMOTE RMI***********************************************
          //DECLARE MESSAGE OBJECT
          Message message;

          //USING REMOTE INTERFACE
          MessageServerInterface server;

          //GETTING REMOTE OBJECT REFERENCE
          //RMI SERVER IMPLEMENTS MessageServerInterface
          server = (MessageServerInterface) Naming.lookup(
                             "rmi://"+hostId+":"+regPort+"/EmployeeServer");
          
          System.out.println("Client obtained remote object reference to" +
                                " the EmployeeServer at:\n"+
                                 "rmi://"+hostId+":"+regPort+"/EmployeeServer");
          //REMOTE RMI***********************************************

          // add this object as an action listener for all menu items.
          for(int j=0; j<userMenuItems.length; j++){
             for(int i=0; i<userMenuItems[j].length; i++){
                userMenuItems[j][i].addActionListener(this);
             }
          }
          
          // add this object as an action listener for the view buttons
          deleteJB.addActionListener(this);
          replyJB.addActionListener(this);
          sendTextJB.addActionListener(this);
          sendCipherJB.addActionListener(this);
          // listen for the user to select a row in the list of messages.
          // When a selection is made, the method valueChanged will be called.
          messageListJL.addListSelectionListener(this);
         
          //getting headers for the user
          /*
          MessageLibrary getMail = new MessageLibrary();
          try {
              getMail.createLibrary();
          } catch (FileNotFoundException e1) {
              e1.printStackTrace();
          }
          */
          
          //Message a = new Message();
          
          headers = server.getMessageFromHeaders(user);

          messageListJL.setSelectedIndex(0); 
          
          for(int i = 0; i < headers.length; i++) {
              if(headers[i] != null) {
                  System.out.println("header's index for this user are: " + i );
                  dlm.addElement(headers[i]);
              }
          }
          setVisible(true);
         
          //while (true);
          //System.exit(0);
      }catch (Exception e) {
         e.printStackTrace();}
   }

   public void valueChanged(ListSelectionEvent e) {
       int index = messageListJL.getSelectedIndex();
     try{  
         if (!e.getValueIsAdjusting()){
             if(index > -1) {
                 System.out.println("You selected messageList item: "
                         + messageListJL.getSelectedIndex()); 
                 Message b = new Message();
                 String s = dlm.get(index);
                 Message a = b.getMessage(s);
                 messageContentJTA.setText(a.getMessageBody());
                 fromJTF.setText(a.getName());
                 toJTF.setText(a.getToUser());
                 subjectJTF.setText(a.getSubject());
                 dateJTF.setText(a.getDate());
               }
          }
      }catch(Exception ex){
          ex.printStackTrace();
      }
    }

   public void actionPerformed(ActionEvent e) {
      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
      try{
          if(e.getActionCommand().equals("Exit")) {
             System.exit(0);
          }else if(e.getActionCommand().equals("Get Mail")){
              
              //will refresh stream later
              
          }else if(e.getActionCommand().equals("Reply")) {
                      
              int index = messageListJL.getSelectedIndex();
              Message b = new Message();
              String s = dlm.get(index);
              Message a = b.getMessage(s);
              Date today = new Date();
              SimpleDateFormat form = new SimpleDateFormat(patt);
              String todayStr = form.format(today);

              dateJTF.setText(todayStr);
              fromJTF.setText(user);
              toJTF.setText(a.getName());
              subjectJTF.setText("");
              messageContentJTA.setText("");
              messageStatusJTA.setText("");


          }else if(e.getActionCommand().equals("Delete")) {
             int selected = messageListJL.getSelectedIndex();
             
         
             //Delete on back-end FIRST
             Message a = new Message();
             for(int i = 0; i < headers.length; i++) {
                 if (i == selected) {
                     String b = headers[i];
                     a.deleteMessage(b, user);
                 }
             }
          
              dlm.remove(selected);
             
             //dlm.clear(); //use this to clear the entire list.
             fromJTF.setText(user);
             toJTF.setText("");
             subjectJTF.setText("");
             messageContentJTA.setText("");
             messageStatusJTA.setText("");
          }
          
          // get rid of the waiting cursor
          setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

      }catch(Exception ex){
          ex.printStackTrace();
      }  
   }

   public static void main(String args[]) {
      String hostId="localhost";
      String regPort="2222";
      String userId = "tmhernan";
      if (args.length >= 3){
         hostId=args[0];
         regPort=args[1];
         userId=args[2];
      }
      //System.setSecurityManager(new RMISecurityManager());
      MessageClientGUI rmiclient = new MessageClientGUI(hostId, regPort, userId);
   }
}



