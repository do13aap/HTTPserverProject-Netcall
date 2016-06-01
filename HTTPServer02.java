import java.io.*;
import java.net.*;
import java.util.*;

public class HTTPServer02 {
  public static void main(String args[]) throws Exception {
    int port = Integer.parseInt(args[0]);
    ServerSocket serverSock=new ServerSocket(port);

    while(true) {
      Socket conn = serverSock.accept();
      Scanner scanClientLines = new Scanner(conn.getInputStream());
      String line=null;
      int nlines=0;
      
      String str[] = new String[32];

      while (true) {
        line = scanClientLines.nextLine();
       if(line.length()==0) break;
       str[nlines] = line;
       nlines = nlines + 1;
       // System.out.println("line "+nlines+": "+line);
      }
      
      for(int num=0; num < nlines ;num++){
	  System.out.println("line "+num+": "+str[num]);
      }
     
      
      String s = str[0]; 
      Scanner scans = new Scanner(s);
      String command = scans.next();
      String resource = scans.next();
      String protocol = scans.next(); 
      String host = str[2];

      System.out.println("Command: " + command);
      System.out.println("Resource: " + resource);
      System.out.println("Protocol: " + protocol);
      System.out.println("Host: " + host); 
     
      String filename = "www" + resource;
      // System.out.println(filename);

      String ctype = "";
      if(resource.contains(".jpg") || resource.contains(".jpeg"))
	 {ctype += "Content-Type: image/jpeg\r\n"; }
      else if(resource.contains(".png"))
	 {ctype += "Content-Type: image/png\r\n"; }
      else if(resource.contains(".rtf"))
	 {ctype += "Content-Type: text/plain\r\n"; }
      /// else
      // {ctype = "Content-Type: text/html\r\n"; }
     

      if(protocol.equals("HTTP/1.1") && str[2]==null ){
	   String reply=
	      "HTTP/1.1 400 Bad Request\r\n" +
	       //need to send back host line?
	      "Connection: close\r\n" +
	      ctype +// "Content-Type: text/html\r\n" +
	      "\r\n" +
	      "<h1>Bad Request</h1>\r\n"+ 
	      "\r\n";
	  OutputStream outs = conn.getOutputStream();
	  outs.write(reply.getBytes());
	  break; // conn.close();

	   }
      


       if (!command.equals("GET") && !command.equals("HEAD") && !command.equals("TRACE"))   {
	  String reply=
	      "HTTP/1.0 501 Not Implemented\r\n" +
	      "Connection: close\r\n" +
	      ctype +// "Content-Type: text/html\r\n" +
	      "\r\n" +
	      "<h1>Not Implemented </h1>\r\n"+ 
	      "\r\n";
	  OutputStream outs = conn.getOutputStream();
	  outs.write(reply.getBytes());
	  

}
      
      else  if(!resource.startsWith("/")){
	  String reply=
	      "HTTP/1.0 400 Bad Request\r\n" +
	      "Connection: close\r\n" +
	      ctype + // "Content-Type: text/html\r\n" +
	      "\r\n" +
	      "<h1>Bad Request</h1>\r\n"+ 
	      "\r\n";
	  OutputStream outs = conn.getOutputStream();
	  outs.write(reply.getBytes());
	  break;

}
  
 
   
      File iFile = new File(filename);
      if (command.equals("HEAD")) {
	  InputStream filein = new FileInputStream(iFile);
	  String  cLength = "" +  iFile.length();
	  String reply=
	      "\r\n" + "HTTP/1.0 200 OK\r\n" +
	      "Connection: close\r\n" +
	      ctype +// "Content-Type: text/html\r\n" +
	      "Content-Length: " + cLength +"\r\n" +
	      "\r\n";
	  OutputStream outs = conn.getOutputStream();
	  outs.write(reply.getBytes());
	  
	  conn.close();

} 
   
      // File iFile = new File(filename);
    else  if (command.equals("TRACE")) {
	  InputStream filein = new FileInputStream(iFile);
	  String  cLength = "" +  iFile.length();
	  String reply=
	      "\r\n" + "HTTP/1.0 200 OK\r\n" +
	      "Repeat Message: " + str[0]+"\r\n" +
	      "Connection: close\r\n" +
	      ctype +// "Content-Type: text/http\r\n" +
	      "Content-Length: " + cLength +"\r\n" +
	      "\r\n";
	  OutputStream outs = conn.getOutputStream();
	  outs.write(reply.getBytes());
	  
	  conn.close();

} 
 

    else  if (!iFile.exists()) {
	  // System.out.println("file " + filename + " does not exist");
	  String reply=
	      "HTTP/1.0 404 Not Found\r\n" +
	      "Connection: close\r\n" +
	      ctype + // "Content-Type: text/html\r\n" +
	      "\r\n" +
	      "<h1>Sorry, work in progress </h1>\r\n"+ 
	      "\r\n";
	  OutputStream outs = conn.getOutputStream();
	  outs.write(reply.getBytes());
	  conn.close();
     
      }
     
 else {
	  // System.out.println("bytes in file: " + iFile.length());

	  InputStream filein = new FileInputStream(iFile);
	  String  cLength = "" +  iFile.length();
	  String reply=
	    "\r\n" + "HTTP/1.0 200 OK\r\n" +
	      "Connection: close\r\n" +
	      ctype + // "Content-Type: text/html\r\n" +
	      "Content-Length: " + cLength +"\r\n" +
	      "\r\n";
	   
	  byte fiodata[] = new byte[128];
	  while(true) {
	      int in = filein.read(fiodata,0,128);	  
	      if (in <= 0) break; 
	      OutputStream outs = conn.getOutputStream();
	      //outs.write(cLength.getBytes());
	      outs.write(reply.getBytes());
	      outs.write(fiodata,0,in);
	       
	  }
	  conn.close();
      }
      

      // OutputStream logsOut = new FileOutputStream(iFile);
      // PrintStream ps = new PrintStream(logsOut);
      
      // InetAddress iaddr = conn.getInetAddress();
      // String request = iaddr +  str[0];
      // ps.println(request);


       
 // System.out.println(iaddr);
 //LogFormat IPAddress-"%h  date/Time/zone-%t RequestLine-\"%r\" ResponceNumber-%>s
 // SizeOfObect-B to retun 0 if not content, otherwise "-"-%b"
      
      


    }
  }
}
