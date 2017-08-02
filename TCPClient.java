import java.io.*;
import java.net.*;

class NewTCPClient
{

	static int TCPport;
	static int number_of_iteration= 100;
	
 public static void main(String argv[]) throws Exception
 {
	 
	 
	 

	  double[] TCPtime = new double[number_of_iteration];
	 

	     String host = "localhost";
		TCPport =9451;



		for (int i = 0; i < number_of_iteration; i++) {
			TCPtime[i] = sendTCPRequest(host);
			System.out.println("Time Taken By TCP  @ " + (i) + ":    " + TCPtime[i]);
		}
		System.out.println("***********TCP statistics***********************"); 
		System.out.println("TCP Mean time:          " + MeanCalculation(TCPtime));
		System.out.println("TCP Standard deviation: " + StdDevCalculation(TCPtime));
 }
 static double sendTCPRequest(String host) throws IOException { 
	 
	 
	 double t1, t2, t3;
	    String sentence = "TCP Calling TCP Calling TCP Calling TCP Calling TCP Calling TCP Calling TCP Calling TCP Calling TCP Calling TCP Calling TCP Calling TCP Calling TCP Calling\n";
	  //  String sentence = "/****Hello My Name is BIndu .I study in University of New HAmpshire**********//*\n";
		byte[] byteArray = sentence.getBytes();
	 
  //String sentence;
  String modifiedSentence;
  BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
  Socket clientSocket = new Socket("localhost", 6789);
  InetAddress IPAddress = InetAddress.getByName(host);
t1=System.currentTimeMillis();
  DatagramPacket sendPacket = new DatagramPacket(byteArray, byteArray.length, IPAddress, TCPport);
  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
  BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  sentence = inFromUser.readLine();
  outToServer.writeBytes(sentence + '\n');
  modifiedSentence = inFromServer.readLine();
  //System.out.println("FROM SERVER: " + modifiedSentence);
  t2=System.currentTimeMillis();
  t3=t2-t1;

  clientSocket.close();
  return t3;
 }
 
 
 static double MeanCalculation(double[] data) {
		double sum = 0.0,index;
		for (index=0;index<data.length;index++)
			sum =sum+index;
		return (sum / data.length);
	}
	

static double VarianceCalculation(double[] data) {
		double index;
		double mean = MeanCalculation(data);
		double temp = 0;
		for (index=0;index<data.length;index++)
			temp =temp+ (mean - index) * (mean - index);
		return temp / data.length;
	}
	
	
	static double StdDevCalculation(double[] data) {
		return Math.sqrt(VarianceCalculation(data));
	}
 
 
 
}
