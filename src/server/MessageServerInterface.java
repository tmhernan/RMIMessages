package server;

import java.rmi.*;


public interface MessageServerInterface extends Remote {


    public String[] getMessageFromHeaders(String toAUserName) throws RemoteException;

    public Message getMessage(String header) throws RemoteException;

    public boolean deleteMessage(String header, String toAUserName) throws RemoteException;

    
}

