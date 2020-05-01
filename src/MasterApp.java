import static spark.Spark.*;

public class MasterApp {
	static String prevPage = "";
	static String prevURL = "";
	static String prevQuery = "";
	public static void main(String[] args) {
		//launch localhost:8080 to run app
		port(8080);
		
		//handler for home page
		get("/", (request, response) -> {
			String query = request.queryParams("query");
			
			//set up initial forms
			StringBuilder sb = new StringBuilder();
			sb.append("<b align=\"center\">Welcome to Michael, Rishab, and Sam's Web Browser!</b><br><br>");
			sb.append("<div align=\"center\"><form style=display: inline; action=\"/render\" method=\"GET\">" + 
					"  <label for=\"fname\">Type the URL of the Website You Want to Visit:</label><br><br>" + 
					"  <input style=\"height:50px;font-size:12pt;width:400px;\" type=\"text\" id=\"url\" name=\"url\" placeholder=\"e.g. https://www.espn.com\">" + 
					"  <input type=\"submit\" value=\"Submit\" style=\"height:100px; width:100px\">");
			sb.append("</form>"
					+ "<form style=display: inline; action=\"/\" method=\"GET\">" + 
					"  <label for=\"fname\">Type the Topic that You Want to Learn About:</label><br><br>" + 
					"  <input style=\"height:50px;font-size:12pt;width:400px;\" type=\"text\" id=\"query\" name=\"query\" placeholder=\"e.g. Tom Brady\">" + 
					"  <input type=\"submit\" value=\"Submit\" style=\"height:100px; width:100px\">" +
					"</form></div>");
			
			if (prevPage.equals("") || prevURL == null || prevURL.equals("")) return sb.toString();
			prevQuery = query;
			
			//set up table
			sb.append("<br><br><br>");
			sb.append("<b align=\"center\"> <center> Displaying results of query \"" + query + "\" on " + prevURL + "</center></b><br>");
			sb.append("<table style=\"width:50%\" align=\"center\">" + 
					"  <tr>" + 
					"    <th>Tokenized Query</th>" + 
					"    <th>Exact Appearances in Website</th>" + 
					"    <th># Case Insensitive Appearances</th>" + 
					"    <th>Porter Stemmed Token</th>" + 
					"    <th># Times Porter Stemmed Token Appeared in Website" +	
					"  </tr>");
			
			//initialize porter stemming
			Stemmer stemmer = new Stemmer();
			String[] queryTokens = query.split(" ");
			String[] tokens = new String[queryTokens.length + 1];
			tokens[0] = query;
			if (queryTokens.length > 1) {
				for (int i = 1; i < tokens.length; i++) {
					tokens[i] = queryTokens[i - 1];
				}
			}
			
			//count number of tokens appearing in text
			for (String token : tokens) {
				if (token == null) break;
				sb.append("<tr>");
				sb.append("<td align=\"center\">" + token + "</td>");
				sb.append("<td align=\"center\">" + Utilities.countAppearances(token, prevPage, true) + "</td>");
				sb.append("<td align=\"center\">" + Utilities.countAppearances(token, prevPage, false) + "</td>");
				
				stemmer.add(token.toCharArray(), token.length());
				stemmer.stem();
				String stemmedToken = stemmer.toString();
				sb.append("<td align=\"center\">" + stemmedToken + "</td>");
				sb.append("<td align=\"center\">" + Utilities.countAppearances(stemmedToken, prevPage, true) + "</td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
			sb.append("<br><br><br><br><br>");
			sb.append("<a href=\"/display\">Back to Previous Page to See Syntax Highlighting!</a>");
			return sb.toString();
		});
		
		//handler for rendering the url to screen/storing some temporary state
		get("/render", (request, response) -> {
			String url = request.queryParams("url");
			if (url == null || url == "") {
				response.redirect("/");
				return null;
			}
			if (!url.startsWith("https://") && !url.startsWith("http://")) {
				url = "https://" + url;
			}
			try {
				Client c = new Client().connect(url);
				String contents = c.readContents();
				prevURL = url;
				prevPage = contents;
				return contents;
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.redirect("/");
			return null;
		});
		
		//handler for displaying the cached url
		get("/display", (request, response) -> {
			if (prevPage == null || prevPage.equals("")) {
				response.redirect("/");
				return null;
			}
			String[] queryTokens = prevQuery.split(" ");
			String temp = prevPage;
			for (String token : queryTokens) {
				String replace = "<b>" + token + "</b>";
				temp = prevPage.replaceAll(token, replace);
			}
			return temp;
		});
	}
}
