
public class Utilities {
	
	/**
	 * @Description Counts the number of times substring appears in input 
	 * @param substring
	 * @param input
	 * @param isExact
	 * @return number of times the substring appears in the input
	 */
	public static int countAppearances(String substring, String input, boolean isExact) {
		if (!isExact) {
			substring = substring.toLowerCase();
			input = input.toLowerCase();
		}
		int index = input.indexOf(substring);
		int count = 0;
		while (index != -1) {
		    count++;
		    input = input.substring(index + 1);
		    index = input.indexOf(substring);
		}
		return count;
	}
}
