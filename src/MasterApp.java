import static spark.Spark.*;
public class MasterApp {
	public static void main(String[] args) {
		port(8080);
		get("/", (request, response) -> {
			return null;
		});
	}
}
