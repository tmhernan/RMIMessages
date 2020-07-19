package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageLibraryTCPServer extends Thread {
    private Socket connection;
    private int id;
    private MessageSkeleton skeleton;

    public MessageLibraryTCPServer (Socket sock, int id, MessageServerInterface m) {
       this.connection = sock;
       this.id = id;
       skeleton = new MessageSkeleton(m);
    }

    public void run() {
       try {
          OutputStream outSock = connection.getOutputStream();
          InputStream inSock = connection.getInputStream();
          byte clientInput[] = new byte[1024]; // up to 1024 bytes in a message.
          int numr = inSock.read(clientInput,0,1024);
          if (numr != -1) {
             //System.out.println("read "+numr+" bytes");
             String request = new String(clientInput,0,numr);
             System.out.println("request is: "+request);
             String response = skeleton.callMethod(request);
             byte clientOut[] = response.getBytes();
         outSock.write(clientOut,0,clientOut.length);
             System.out.println("response is: "+response);
          }
          inSock.close();
          outSock.close();
          connection.close();
       } catch (IOException e) {
          System.out.println("I/O exception occurred for the connection:\n"+e.getMessage());
       }
    }
     
    public static void main (String args[]) {
        Socket sock;
        MessageServerInterface m = new MessageServerImpl();

       //Initialize the library of messages:
       MessageServerImpl start = new MessageServerImpl();
       start.createLibrary();


        int id=0;
        try {
           if (args.length > 2) {
              System.out.println("Usage: java ser321.tcpjsonrpc.server."+
                                 "StudentCollectionTCPJsonRPCServer [portNum]");
              System.exit(0);
           }
           //int portNo = Integer.parseInt(args[1]);
           int portNo = 8080;
           if (portNo <= 1024) portNo=8888;
           ServerSocket serv = new ServerSocket(portNo);
           // accept client requests. For each request create a new thread to handle
           while (true) { 
              System.out.println("Student server waiting for connects on port "
                                 +portNo);
              sock = serv.accept();
              System.out.println("Student server connected to client: "+id);
              MessageLibraryTCPServer myServerThread =
                 new MessageLibraryTCPServer(sock,id++, m);
              myServerThread.start();
           }
        } catch(Exception e) {e.printStackTrace();}
     }
 }