package cz.vojtechvondra.ldbill;


public class Utils {

    private Utils() {
    }

    /**
     * Join string with a separator
     * @param s Strings to be concatenated
     * @param glue Separator
     * @return concatenated string
     */
    public static String joinString(String[] s, String glue) {
        int k = s.length;
        if (k == 0) {
            return null;
        }

        StringBuilder out = new StringBuilder();
        out.append(s[0]);

        for (int x = 1; x < k; ++x) {
            out.append(glue).append(s[x]);
        }

        return out.toString();
    }
}
