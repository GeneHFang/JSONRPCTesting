package client;

import java.io.*;
import java.util.*;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;


public class CalculateClient extends Object implements Calculate {

    public String serviceURL;
    public JsonRpcRequestViaHttp server;
    public static int id = 0;
 
    public CalculateClient(String serviceURL) {
       this.serviceURL = serviceURL;
       try{
          this.server = new JsonRpcRequestViaHttp(new URL(serviceURL));
       }catch (Exception ex){
          System.out.println("Malformed URL "+ex.getMessage());
       }
    }
 
    private String packageCalcCall(String oper, double left, double right){
       JSONObject jsonObj = new JSONObject();

       jsonObj.put("jsonrpc","2.0");
       jsonObj.put("method",oper);
       jsonObj.put("id",++id);
       String almost = jsonObj.toString();
       String toInsert = ",\"params\":["+ String.format("%.2f", left)
                         + "," + String.format("%.2f", right) + "]";
       String begin = almost.substring(0,almost.length()-1);
       String end = almost.substring(almost.length()-1);
       String ret = begin + toInsert + end;
       return ret;
    }
 
    /**
     * Add two numbers
     * @return The sum
     */
    public double plus(double left, double right){
       double result = 0;
       try{
          String jsonStr = this.packageCalcCall("plus",left,right);
          //System.out.println("sending: "+jsonStr);
          String resString = server.call(jsonStr);
          //System.out.println("got back: "+resString);
          JSONObject res = new JSONObject(resString);
          result = res.optDouble("result");
       }catch(Exception ex){
          System.out.println("exception in rpc call to plus: "+ex.getMessage());
       }
       return result;
    }

    public String serviceInfo(){
        return "Service information";
     }
  
     public static void main(String args[]) {
        try {
           String url = "http://127.0.0.1:8080/";
           if(args.length > 0){
              url = args[0];
           }
           CalculateClient cjc = new CalculateClient(url);
           BufferedReader stdin = new BufferedReader(
              new InputStreamReader(System.in));
           System.out.print("Enter end or {+|-|*|/} double double eg + 3 5 >");
           String inStr = stdin.readLine();
           StringTokenizer st = new StringTokenizer(inStr);
           String opn = st.nextToken();
           while(!opn.equalsIgnoreCase("end")) {
              if(opn.equalsIgnoreCase("+")){
                 double result = cjc.plus(Double.parseDouble(st.nextToken()),
                                          Double.parseDouble(st.nextToken()));
                 System.out.println("response: "+result);
              }
              System.out.print("Enter end or {+|-|*|/} double double eg + 3 5 >");
              inStr = stdin.readLine();
              st = new StringTokenizer(inStr);
              opn = st.nextToken();
           }
        }catch (Exception e) {
           System.out.println("Oops, you didn't enter the right stuff");
        }
     }
}