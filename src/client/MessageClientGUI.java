package client;

//package ser321.assign2.lindquis.client;


//import ser321.assign2.lindquis.server.MessageServerInterface;
//import ser321.assign2.lindquis.server.Message;


import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ser321.assign2.lindquis.client.MessageGui;
import server.Message;

/*
 * Copyright 2019 Tim Lindquist,
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
 * Purpose: demonstrate use of MessageGui class for students to use as a
 * basis for solving Ser321 Spring 2019 Homework Problems.
 * The class SampleClient can be used by students in constructing their 
 * controller for solving homework problems. The view class is MessageGui.
 *
 * This problem set uses a swing user interface to implement (secure) messaging.
 * Messages are communicated to/from message clients, via a common well-known.
 * server.
 * Messages can be sent in clear text, or using password based encryption 
 * (last assignment). For secure messages, the message receiver must enter
 * the password (encrypted).
 * The Message tab has two panes. left pane contains a JList of messages
 * for the user. The right pane is a JTextArea, which can display the
 * contents of a selected message. This pane is also used to compose
 * messages that are to be sent.
 *
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version January 2019
 */

//Reading in 


public class MessageClientGUI extends MessageGui
                           implements ActionListener, ListSelectionListener {

   private String user;   // originator of all message sent by this client.
   //private String serverHostPort; // such as lindquisrpi.local:8080
   private static final String patt = "EEE MMM d K:mm:ss yyyy";
   private String [] headers = new String[100];
   DefaultListModel<String> dlm = (DefaultListModel<String>)messageListJL.getModel();
   MessageLibraryTcpProxy  sc;
   

   public MessageClientGUI(String host, String port, String user) {
    super("Dr. Lindquist", user);
    this.user = user;
    System.out.println("USER IS: " + user);
    
    try{   
        
        String url = "http://"+host+":"+port+"/";
        System.out.println("Opening connection to: "+url);
        
        //Connect to proxy. Open input stream to read
        //input from server
        MessageLibraryTcpProxy  sc = (MessageLibraryTcpProxy )new MessageLibraryTcpProxy (host, Integer.parseInt(port));
        BufferedReader stdin = new BufferedReader(
           new InputStreamReader(System.in));
        
        /*INPUTSTREAM READ IN**************NOT NECESSARY?
        //read in stream
        String inStr = stdin.readLine();
        StringTokenizer streamSt = new StringTokenizer(inStr);
        
        //always reding in
        String opn = streamSt.nextToken();
        
        /*INPUTSTREAM READ IN**************NOT NECESSARY? */

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
          
          //calling to get email headers from through client 
          //proxy to server proxy
          headers = sc.getMessageFromHeaders(user);

          messageListJL.setSelectedIndex(0); 
          
          for(int i = 0; i < headers.length; i++) {
              if(headers[i] != null) {
                  System.out.println("header's index for this user are: " + i );
                  dlm.addElement(headers[i]);
              }
          }
          
          setVisible(true);
         
      
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
                 //Message b = new Message();
                 String s = dlm.get(index);

                 //Message a = sc.getMessage(s);
                 
                 messageContentJTA.setText(sc.getMessageBody(s));
                 fromJTF.setText(sc.getName(s));
                 toJTF.setText(sc.getToUser(s));
                 subjectJTF.setText(sc.getSubject(s));
                 dateJTF.setText(sc.getDate(s));
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
              
          }else if(e.getActionCommand().equals("Reply")) {
              //DO I NEED TO GET BY HEADER INSTEAD OF INDEX?????
              int index = messageListJL.getSelectedIndex();

              String s = dlm.get(index);
              
              //calling proxy
              Message a = sc.getMessage(s);
              
              //DATE
              Date today = new Date();
              SimpleDateFormat form = new SimpleDateFormat(patt);
              String todayStr = form.format(today);
              dateJTF.setText(todayStr);
              

              fromJTF.setText(user);
              toJTF.setText(a.getName());
              subjectJTF.setText("");
              messageContentJTA.setText("");
              messageStatusJTA.setText("");

          }else if(e.getActionCommand().equals("Send Text")) {

              System.out.println("INSIDE OF THE SENT ACTION");
              String date = dateJTF.getText();
              String fromUser = fromJTF.getText();
              String toUser = toJTF.getText();
              String subject = subjectJTF.getText();
              String messageBody = messageContentJTA.getText();
              String header = (fromUser + "  " + date);

              //calling proxy
              sc.addMessagetoLib(fromUser, header, messageBody, date, subject, toUser);

          }else if(e.getActionCommand().equals("Get Mail")) {
          
              //calling proxy
              headers = sc.getMessageFromHeaders(user);              
                  
              for(int i = 0; i < headers.length; i++) {
                   if(headers[i] != null) {
                       if(!dlm.contains(headers[i])) {
                           System.out.println("header's index for this user are: " + i );
                           dlm.addElement(headers[i]);
                       }
                     }    
              }

          }else if(e.getActionCommand().equals("Delete")) {
          
             int selected = messageListJL.getSelectedIndex();
             
         
             //Delete on back-end FIRST
             //Message a = new Message();
             for(int i = 0; i < headers.length; i++) {
                 if (i == selected) {
                     String b = headers[i];
                     
                     //calling proxy
                     sc.deleteMessage(b, user);
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
       String port="8080";
       String userId = "Tim.Lindquist";
       if (args.length >= 3){
          hostId=args[1];
          port=args[2];
          userId=args[0];
       }
       
       MessageClientGUI runGUI = new MessageClientGUI(hostId, port, userId);
    }
}



