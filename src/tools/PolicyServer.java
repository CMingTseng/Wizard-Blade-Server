package tools;

import java.net.ServerSocket;
import java.net.Socket;

import server.Start;
import tools.gui.WizardBladeServerGUI;



public class PolicyServer extends Thread {
	public static final String POLICY_REQUEST = "<policy-file-request/>";
    public static final String POLICY_XML =
            "<?xml version=\"1.0\"?>"
            + "<cross-domain-policy>"
            + "<allow-access-from domain=\"*\" to-ports=\"*\" />"
            + "</cross-domain-policy>";
    
     protected int port;
     protected ServerSocket serverSocket;
     protected boolean listening;
     
     protected WizardBladeServerGUI wizardBladeServerGUI;
    
     public PolicyServer(int serverPort) {
    	 this.port = serverPort;
    	 this.listening = false;
     }
     
     public int getPort() {
         return this.port;
     }
     
     public boolean getListening() {
         return this.listening;
     }
     
     protected void debug(String msg) {
         Start.debug("PolicyServer (" + this.port + ")", msg);
     }
     
     public void run() {
    	 try {
    		 this.serverSocket = new ServerSocket(this.port);
    		 this.listening = true;
    		 //System.out.println("Wizard Blade Policy Server- listening");
    		 debug("listening");
 
    		 while (this.listening) {
    			 Socket socket = this.serverSocket.accept();
    			 //System.out.println("client connection from " + socket.getRemoteSocketAddress());
    			 debug("client connection from " + socket.getRemoteSocketAddress());
    			 PolicyServerConnection socketConnection = new PolicyServerConnection(socket);
    			 socketConnection.start();
    		 };
    	 }
    	 catch (Exception e) {
    		 debug("Exception (run): " + e.getMessage());
    	 }
     }
     
     protected void finalize() {	 
         try {
             this.serverSocket.close();
             this.listening = false;
             debug("stopped");
         }
         catch (Exception e) {
        	 debug("Exception (finalize): " + e.getMessage());
         }
     }
}
