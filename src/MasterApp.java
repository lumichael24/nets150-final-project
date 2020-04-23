import static spark.Spark.*;
public class MasterApp {
	public static void main(String[] args) {
		get("/", (request, response) -> {
			return null;
		});
	}
}
