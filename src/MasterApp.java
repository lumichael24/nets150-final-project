import static spark.Spark.*;
public class MasterApp {
	public static void main(String[] args) {
		port(8080);
		get("/", (request, response) -> {
			return "<form action=\"/render\" method=\"GET\">" + 
					"  <label for=\"fname\">URL Of Website:</label><br>" + 
					"  <input type=\"text\" id=\"url\" name=\"url\"><br>" + 
					"  <input type=\"submit\" value=\"Submit\">" +
					"</form>";
		});
		
		get("/render", (request, response) -> {
			String url = request.queryParams("url");
			if (url == null || url == "") {
				response.redirect("/");
				return null;
			}
			Client c = new Client().connect(url);
			String contents = c.readContents();
			return contents;
		});
	}
}
