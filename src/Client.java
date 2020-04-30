import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Client {
	private URL url;
	private HttpURLConnection httpConnection;
	
	/**
	 * Creates a URL from a string.
	 * Opens the connection to be used later.
	 * @param url the url to get information from
	 * @throws Exception 
	 */
	public Client connect(String urlName) throws Exception {
		try {
			this.url = new URL(urlName);
			
			URLConnection connection = url.openConnection();
			this.httpConnection = (HttpURLConnection) connection;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
		return this;
	}
	
	public String readContents() {
		StringBuilder body = new StringBuilder();
		Scanner in;
		try {
			in = new Scanner(httpConnection.getInputStream());
			while (in.hasNextLine()) {
				String line = in.nextLine();
				body.append(line);
				body.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body.toString();
	}
	
}