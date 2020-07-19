package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import server.Message;

public class MessageLibraryTcpProxy {

    private static final boolean debugOn = true;
    private static final int buffSize = 4096;
    private static int id = 0;
    private String host;
    private int port;
    
    public MessageLibraryTcpProxy (String host, int port){
        this.host = host;
        this.port = port;
     }
    
    private void debug(String message) {
        if (debugOn)
           System.out.println("debug: "+message);
     }
    
    //A JSONOjbect is created by the client's proxy to represent a method 
    //call with its args. The client's proxy sends the json request object 
    //to the server's proxy.
    public String callMethod(String method, Object[] params) {
        JSONObject theCall = new JSONObject();
        String ret = "{}";
        
        try{
           debug("Request is: "+theCall.toString());
           
           theCall.put("method",method);
           theCall.put("id",id);
           theCall.put("jsonrpc","2.0");
           
           ArrayList<Object> al = new ArrayList();
           for (int i=0; i<params.length; i++){
              al.add(params[i]);
           }
           
           JSONArray paramsJson = new JSONArray(al);
           
           theCall.put("params",paramsJson);
           Socket sock = new Socket(host,port);
           
           OutputStream os = sock.getOutputStream();
           InputStream is = sock.getInputStream();
           
           int numBytesReceived;
           int bufLen = 1024;
           
           String strToSend = theCall.toString();
           
           byte bytesReceived[] = new byte[buffSize];
           byte bytesToSend[] = strToSend.getBytes();
           
           os.write(bytesToSend,0,bytesToSend.length);
           numBytesReceived = is.read(bytesReceived,0,bufLen);
           ret = new String(bytesReceived,0,numBytesReceived);

           debug("callMethod received from server: "+ret);
           
           os.close();
           is.close();
           sock.close();
        }catch(Exception ex){
           
           System.out.println("exception in callMethod: "+ex.getMessage());
        }
        return ret;
    }
    
    //collection client will use the following methods to send over 'methods 
    //requests' to server proxy through the callMethod above
    
    
    public String[] getMessageFromHeaders(String header) {
        String[] returnArray = new String[]{};
        String resultFromBuffer = callMethod("getMessageFromHeaders", new Object[]{header});
        
        debug("result of getMessageFromHeaders is: " + resultFromBuffer);
        
        JSONObject resultInJsonObj = new JSONObject(resultFromBuffer);
        JSONArray headersInJsonArr = resultInJsonObj.optJSONArray("result");
        
        returnArray = new String[headersInJsonArr.length()];
        
        for (int i=0; i<headersInJsonArr.length(); i++){
            returnArray[i] = headersInJsonArr.optString(i);
        }        
        
        return returnArray;
    }   
    
    public Message getMessage(String header) {
        //intializing message object that will become returned at end
        Message returnedMessage = new Message();
        
        //calling getMessage method to server through rpc using name passed in
        String resultFromBuffer = callMethod("get", new Object[]{header});
        
        //intializing json object that will hold result from server
        JSONObject resultInJsonObj = new JSONObject(resultFromBuffer);
        
        //Reciving string from buffer. Put in a json object
        JSONObject messageInJsonObj = resultInJsonObj.optJSONObject("result");
        
        //Initialzing new message w/ json object (method in Message class)
        returnedMessage = new Message(messageInJsonObj);
        
        return returnedMessage;
     }
    
    public boolean deleteMessage(String header, String toAUserName) {
        boolean returnedResult = false;
        String result = callMethod("delete", new Object[]{header,toAUserName});
        JSONObject returnResultJson = new JSONObject(result);
        returnedResult = returnResultJson.optBoolean("result",false);
        return returnedResult;
     }    
    
    public boolean addMessagetoLib(Message m) {
        boolean returnedResult = false;
        
        String result = callMethod("addMessagetoLib", new Object[]{m.toJSONObject()});
        
        JSONObject returnResultJson = new JSONObject(result);
        returnedResult = returnResultJson.optBoolean("result",false);
        
        return returnedResult;
     }
    
    public boolean addMessagetoLib(String n, String h, String m, String d, String s, String t) {
        boolean returnedResult = false;
        
        String result = callMethod("addMessagetoLib", new Object[]{n, h, m, d, s, t});
        
        JSONObject returnResultJson = new JSONObject(result);
        returnedResult = returnResultJson.optBoolean("result",false);
        
        return returnedResult;
     }
     
    public String getMessageBody(String header) {
        String result = callMethod("getMessageBody", new Object[] {header});
        JSONObject returnResultJson = new JSONObject(result);
        String messageBody = returnResultJson.toString();
        
        return messageBody;
        
    }

    public String getDate(String header) {
        String result = callMethod("getDate", new Object[] {header});
        JSONObject returnResultJson = new JSONObject(result);
        String date = returnResultJson.toString();
        
        return date;
    }

    public String getSubject(String header) {
        String result = callMethod("getSubject", new Object[] {header});
        JSONObject returnResultJson = new JSONObject(result);
        String subject = returnResultJson.toString();
        
        return subject;
    }

    public String getName(String header) {
        String result = callMethod("getName", new Object[] {header});
        JSONObject returnResultJson = new JSONObject(result);
        String name = returnResultJson.toString();
        
        return name;
    }

    public String getToUser(String header) {
        String result = callMethod("getToUser", new Object[] {header});
        JSONObject returnResultJson = new JSONObject(result);
        String toUser = returnResultJson.toString();
        
        return toUser;
    }
    
    
}
