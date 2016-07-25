package moofDoS;

public class Utils {
	public static String format(String toFormat, String... values) {
		for (int i = 0; i < values.length; i++) {
			String str = "%" + i;
			while (toFormat.indexOf(str) > -1)
				toFormat = toFormat.substring(0, toFormat.indexOf(str)) + values[i]
						+ toFormat.substring(toFormat.indexOf(str) + str.length());
		}

		return toFormat;
	}
}