package ser321.assign2.lindquis.server;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface MessageServerInterface extends Remote {


    public String[] getMessageFromHeaders(String toAUserName) throws RemoteException;

    public Message getMessage(String header) throws RemoteException;

    public boolean deleteMessage(String header, String toAUserName) throws RemoteException;

    
}

