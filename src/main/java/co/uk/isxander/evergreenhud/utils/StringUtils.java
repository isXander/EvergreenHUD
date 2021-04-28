package co.uk.isxander.evergreenhud.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    // i made this in 2 secs dont @ me ik its bad
    public static String capitalize(String in) {
        String out = in.toLowerCase();
        boolean lastSpace = true;
        List<String> chars = new ArrayList<>();
        for (char c : out.toCharArray()) {
            chars.add(c + "");
        }

        for (int i = 0; i < chars.size(); i++) {
            String c = chars.get(i);
            if (lastSpace) {
                chars.set(i, c.toUpperCase());
            }

            lastSpace = c.equals(" ");
        }

        StringBuilder sb = new StringBuilder();
        for (String s : chars)
            sb.append(s);

        return sb.toString();
    }

}
