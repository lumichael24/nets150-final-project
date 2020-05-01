import static spark.Spark.*;

import org.tartarus.martin.Stemmer;

public class MasterApp {
	static String prevPage = "";
	public static void main(String[] args) {
		port(8080);
		get("/", (request, response) -> {
			String query = request.queryParams("query");
			StringBuilder sb = new StringBuilder();
			sb.append("<div><form action=\"/render\" method=\"GET\">" + 
					"  <label for=\"fname\">Type the URL of the Website You Want to Visit:</label><br><br>" + 
					"  <input style=\"height:50px;font-size:12pt;width:400px;\" type=\"text\" id=\"url\" name=\"url\" placeholder=\"e.g. https://www.espn.com\">" + 
					"  <input type=\"submit\" value=\"Submit\" style=\"height:100px; width:100px\">" +
					"</form>"
					+ "<form action=\"/\" method=\"GET\">" + 
					"  <label for=\"fname\">Type the Topic that You Want to Learn About on Website:</label><br><br>" + 
					"  <input style=\"height:50px;font-size:12pt;width:400px;\" type=\"text\" id=\"query\" name=\"query\" placeholder=\"e.g. Tom Brady\">" + 
					"  <input type=\"submit\" value=\"Submit\" style=\"height:100px; width:100px\">" +
					"</form></div>");
			if (query == null || query.equals("") || prevPage.equals("")) return sb.toString();
			
			sb.append("<table style=\"width:50%\" align=\"center\">" + 
					"  <tr>" + 
					"    <th>Tokenized Query</th>" + 
					"    <th># Times Appeared in Website</th>" + 
					"    <th>Porter Stemmed Token</th>" + 
					"    <th># Times Porter Stemmed Token Appeared in Website" +
					"  </tr>");
			
			Stemmer stemmer = new Stemmer();
			String[] queryTokens = query.split(" ");
			String[] tokens = new String[queryTokens.length + 1];
			tokens[0] = query;
			if (queryTokens.length > 1) {
				for (int i = 1; i < tokens.length; i++) {
					tokens[i] = queryTokens[i - 1];
				}
			}
			for (String token : tokens) {
				if (token == null) break;
				sb.append("<tr>");
				sb.append("<td align=\"center\">" + token + "</td>");
				sb.append("<td align=\"center\">" + Utilities.countAppearances(token, prevPage) + "</td>");
				
				stemmer.add(token.toCharArray(), token.length());
				stemmer.stem();
				String stemmedToken = stemmer.toString();
				sb.append("<td align=\"center\">" + stemmedToken + "</td>");
				sb.append("<td align=\"center\">" + Utilities.countAppearances(stemmedToken, prevPage) + "</td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
			return sb.toString();
		});
		
		get("/render", (request, response) -> {
			String url = request.queryParams("url");
			if (url == null || url == "") {
				response.redirect("/");
				return null;
			}
			try {
				Client c = new Client().connect(url);
				String contents = c.readContents();
				prevPage = contents;
				return contents;
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.redirect("/");
			return null;
		});
	}
}
