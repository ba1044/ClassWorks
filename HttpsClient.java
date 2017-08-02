import java.io.*;
//import java.io.DataOutputStream;
import java.net.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.Arrays;

class NewHTTPSClient {
	static int numberOfAttempts = 10;

	static int portHTTPS;

	public static void main(String[] args) throws Exception {

		double[] timeHTTPS = new double[numberOfAttempts];

		// String host = "127.0.0.1";

		String host = "www.facebook.com";
		portHTTPS = 8280;

		for (int i = 0; i < numberOfAttempts; i++) {
			timeHTTPS[i] = sendGetSecure(host);
			System.out.println("HTTPS attempt #" + (i + 1) + ":    " + timeHTTPS[i]);
		}
		System.out.println("HTTPS Mean time:          " + getMean(timeHTTPS));
		System.out.println("HTTPS Standard deviation: " + getStdDev(timeHTTPS));
	}

	// HTTPS GET request
	static double sendGetSecure(String IP) throws Exception {

		// certificate workaround - I used
		// http://www.rgagnon.com/javadetails/java-fix-certificate-problem-in-HTTPS.html
		// ------------------------------------------
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}

		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		// ---------------------------------------
		// end of the certificate workaround

		// URL url = new URL("https://[" + IP + "]:" + portHTTPS + "/" +
		// fileToAccess);

		// URL url = new URL("https://[" + IP + "]:" + portHTTPS);

		URL url = new URL("https://" + IP);

		double time, time1, time2;
		time1 = System.nanoTime();
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();
		con.disconnect();
		time2 = System.nanoTime();
		time = (time2 - time1) / Math.pow(10, 6);

		return time;
	}

	// statistics tools from:
	// http://stackoverflow.com/questions/7988486/how-do-you-calculate-the-variance-median-and-standard-deviation-in-c-or-java
	static double getMean(double[] data) {
		double sum = 0.0;
		for (double a : data)
			sum += a;
		return (sum / data.length);
	}

	static double getVariance(double[] data) {
		double mean = getMean(data);
		double temp = 0;
		for (double a : data)
			temp += (mean - a) * (mean - a);
		return temp / data.length;
	}

	static double getStdDev(double[] data) {
		return Math.sqrt(getVariance(data));
	}

}
