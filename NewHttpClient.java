
import java.io.*;
//import java.io.DataOutputStream;

import java.net.HttpURLConnection;

import java.net.URL;


import java.net.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

class NewHttpClient {
	static int number_of_iteration = 5;

	static int HTTPport=8081;
	static String welcomeFile;

	public static void main(String[] args) throws Exception {
		
		   

		double[] HTTPtime = new double[number_of_iteration];

		String host = "fd50:4abe:b885:1::2";
		//System.out.println("http://["+host+"]:"+HTTPport+"/~bk1044/index.html");

		for (int i = 0; i < number_of_iteration; i++) {
			HTTPtime[i] = sendGetRequest(host);
			System.out.println("HTTP attempt #" + (i + 1) + ":    " + HTTPtime[i]);

		}
		System.out.println("HTTP Mean time:          " + MeanCalculation(HTTPtime));
		System.out.println("HTTP Standard deviation: " + StdDevCalculation(HTTPtime));

	}

	static double sendGetRequest(String IP) throws Exception {
	
		URL url = new URL("http://["+IP+"]:"+HTTPport+"//~bk1044//index.html");
		System.out.println(url);
		double t1, t2, t3;
		t1 = System.nanoTime();
		
		
		 HttpURLConnection httpcon = (HttpURLConnection) url.openConnection(); 
		 httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
		System.out.println("Connection started");

		httpcon.setRequestMethod("GET");
		 httpcon.setDoInput(true);
			System.out.println("Connection started");
		BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
		
		System.out.println("Connection started");
		String inputLine;
		StringBuffer response = new StringBuffer();

		/*while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
          
		
		String inputLine;*/
        while ((inputLine = in.readLine()) != null) 
           //System.out.println(inputLine);
        in.close();
		
		in.close();
		httpcon.disconnect();

		t2 = System.nanoTime();
		t3 = (t2 - t1) / Math.pow(10, 6);

		return t3;

	}

	static double MeanCalculation(double[] data) {
		double sum = 0.0, index;
		for (index = 0; index < data.length; index++)
			sum = sum + index;
		return (sum / data.length);
	}

	static double VarianceCalculation(double[] data) {
		double index;
		double mean = MeanCalculation(data);
		double temp = 0;
		for (index = 0; index < data.length; index++)
			temp = temp + (mean - index) * (mean - index);
		return temp / data.length;
	}

	static double StdDevCalculation(double[] data) {
		return Math.sqrt(VarianceCalculation(data));
	}

}
