import java.io.IOException;

import java.io.*;
import java.net.*;


class FinalUDPClient {
	

	static int UDPport;
	static int number_of_iteration= 100;

	public static void main(String[] args) throws Exception {

		double UDPtime[] = new double[number_of_iteration];

		String host = "localhost";
		UDPport = 8990;

		for (int i = 0; i < number_of_iteration; i++) {
			UDPtime[i] = sendUDPRequest(host);
			System.out.println("Time Taken By UDP  @ " + (i) + ":    " + UDPtime[i]);
		}
		
		System.out.println("***********UDP statistics***********************"); 
		System.out.println("UDP Mean time:          " + MeanCalculation(UDPtime));
		System.out.println("UDP Standard deviation: " + StdDevCalculation(UDPtime));

	}

	static double sendUDPRequest(String host) throws IOException {
		
		double t1, t2, t3;
		String sentence = "/****Hello My Name is BIndu .I study in University of New HAmpshire**********//*\n";
		byte[] byteArray = sentence.getBytes();
		byte[] receiveData = new byte[1024];

		
        t2 = System.nanoTime()/Math.pow(10, 6);
        
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		InetAddress IPAddress = InetAddress.getByName(host);
		DatagramPacket sendPacket = new DatagramPacket(byteArray, byteArray.length, IPAddress, UDPport);
		

		
		System.out.println("Starttime in ns  =  "+t2);
		DatagramSocket clientSocket = new DatagramSocket();
		clientSocket.send(sendPacket);
		//clientSocket.receive(receivePacket);
		clientSocket.close();
		t3 = System.nanoTime()/Math.pow(10, 6);
		System.out.println("Endtime in ns =  "+t3 );
		t1 = (t3 - t2) / Math.pow(10, 6);
		return t1;
	}

		
/******************************* Function to calculate Mean  ************************/		
		



	static double MeanCalculation(double[] data) {
		double sum = 0.0,index;
		for (index=0;index<data.length;index++)
			sum =sum+index;
		return (sum / data.length);
	}
	
	
/******************************* Function to calculate StdDeviation  ************************/		
	
	
	static double StdDevCalculation(double[] data) {
		return Math.sqrt(VarianceCalculation(data));
	}
	
	
	
/******************************* Function to calculate Variance ************************/	

	static double VarianceCalculation(double[] data) {
		double index;
		double mean = MeanCalculation(data);
		double temp = 0;
		for (index=0;index<data.length;index++)
			temp =temp+ (mean - index) * (mean - index);
		return temp / data.length;
	}

	
}
