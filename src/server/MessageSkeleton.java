package server;

import org.json.JSONArray;
import org.json.JSONObject;

public class MessageSkeleton {

    private static final boolean debugOn = true;
    MessageServerInterface messageColl;

    public MessageSkeleton(MessageServerInterface m) {
        
        this.messageColl = m;
        
    }
    
    private void debug(String message) {
        if (debugOn)
           System.out.println("debug: "+message);
     }
    
    //client sends json request here
    //this proxy de-serializes the request, calls the appropriate 
    //method w/ the arguements and gets the results

    public String callMethod(String request){
       JSONObject result = new JSONObject();
       
       try{
          JSONObject theCall = new JSONObject(request);
          
          //printing out json object in the form of a string
          //from the cient
          debug("Request is: "+theCall.toString());
          
          //get the method name from the jsonString
          String method = theCall.getString("method");
          //get the method id from the jsonString
          int id = theCall.getInt("id");
          //
          JSONArray params = null;
          if(!theCall.isNull("params")){
             params = theCall.getJSONArray("params");
          }
          
          result.put("id",id);
          result.put("jsonrpc","2.0");
          
          if(method.equals("addMessagetoLib")){
             JSONObject studJson = params.getJSONObject(0);
             Message messToAdd = new Message(studJson);
             debug("adding stud: "+messToAdd.toJsonString());
             messageColl.addMessagetoLib(messToAdd);
             result.put("result",true); // correct?
          
          }else if(method.equals("deleteMessage")){
             String header = params.getString(0);
             String toAUserName = params.getString(1);
             debug("removing message header "+ header + "that is sent to" + toAUserName);
             messageColl.deleteMessage(header, toAUserName);
             result.put("result",true);  // correct?
          
          }else if(method.equals("getMessage")){
             String header = params.getString(0);
             Message message = messageColl.getMessage(header);
             JSONObject messageJSON = message.toJSONObject();
             debug("getMessage for header: "+ message.toString());
             result.put("result", messageJSON); // correct?
             
          }else if(method.equals("getMessageFromHeaders")){
             String toAUserName = params.getString(0);
             String[] headersForAUser = messageColl.getMessageFromHeaders(toAUserName);
             
             JSONArray resultArray = new JSONArray();
             for (int i=0; i<headersForAUser.length; i++){
                 resultArray.put(headersForAUser[i]);
             }
             
             debug("result array headers: "+ resultArray.toString());
             result.put("result",resultArray);
          
          }else if(method.equals("getMessageBody")){
              String header = params.getString(0);
              String message = messageColl.getMessageBody(header);
              result.put("result", message); // correct?   
         
           }else if(method.equals("getDate")){
               String header = params.getString(0);
               String message = messageColl.getDate(header);
               result.put("result", message); // correct?    
          
           }else if(method.equals("getSubject")){
               String header = params.getString(0);
               String message = messageColl.getSubject(header);
               result.put("result", message); // correct?   
  
           }else if(method.equals("getName")){
               String header = params.getString(0);
               String message = messageColl.getName(header);
               result.put("result", message); // correct?   
          
           }else if(method.equals("getToUser")){
               String header = params.getString(0);
               String message = messageColl.getToUser(header);
               result.put("result", message); // correct?   
           }
          else{
             debug("Unable to match method: "+method+". Returning 0.");
             result.put("result",0.0);          
          }
       }catch(Exception ex){
          System.out.println("exception in callMethod: "+ex.getMessage());
       }
       return result.toString();
    }    
    
    
}
