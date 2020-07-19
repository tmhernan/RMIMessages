package server;

import java.util.Vector;

/**
* Shared interface between server and client(s)
* 
*/
public interface MessageServerInterface {


    public String[] getMessageFromHeaders(String toAUserName);

    public Message getMessage(String header);

    public boolean deleteMessage(String header, String toAUserName);

    //not needed anymore?
    public void addMessagetoLib(String name, String header, String message, String d, String s, String t); 

    public boolean addMessagetoLib(Message m);
    
    
    //public boolean getHeader();

    public String getMessageBody(String header);

    public String getDate(String header);

    public String getSubject(String header);

    public String getName(String header);

    public String getToUser(String header);

    public Vector<Message> getMessagesReceived();
}

