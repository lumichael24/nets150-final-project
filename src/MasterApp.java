import static spark.Spark.*;
public class MasterApp {
	public static void main(String[] args) {
		port(8080);
		get("/", (request, response) -> {
			return "<div align=\"center\"> <form action=\"/render\" method=\"GET\">" + 
					"  <label for=\"fname\">URL Of Website:</label>" + 
					"  <input style=\"height:50px;font-size:12pt;width:400px;\" type=\"text\" id=\"url\" name=\"url\" placeholder=\"e.g. https://www.espn.com\"><br><br><br>" + 
					"  <input type=\"submit\" value=\"Submit\" style=\"height:100px; width:100px\">" +
					"</form></div>";
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
				return contents;
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.redirect("/");
			return null;
		});
	}
}
