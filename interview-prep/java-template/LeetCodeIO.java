import java.util.*;

/**
 * LeetCode 风格输入解析工具。
 * 支持的输入格式：
 *   [1,2,3,4]            -> int[]
 *   [[1,2],[3,4]]        -> int[][]
 *   ["abc","de"]         -> String[]
 *   "hello"              -> String
 *   1,2,null,3           -> 含 null 的层序数组（给 TreeNode 用）
 */
public class LeetCodeIO {

    /** 解析形如 [1,2,3] 的一维 int 数组（允许空 [] 或空白）。 */
    public static int[] parseIntArray(String s) {
        if (s == null) return new int[0];
        s = s.trim();
        if (s.startsWith("[")) s = s.substring(1);
        if (s.endsWith("]")) s = s.substring(0, s.length() - 1);
        s = s.trim();
        if (s.isEmpty()) return new int[0];
        String[] parts = s.split(",");
        int[] out = new int[parts.length];
        for (int i = 0; i < parts.length; i++) out[i] = Integer.parseInt(parts[i].trim());
        return out;
    }

    /** 解析形如 [[1,2],[3,4]] 的二维 int 数组。 */
    public static int[][] parseInt2D(String s) {
        if (s == null) return new int[0][];
        s = s.trim();
        if (s.startsWith("[")) s = s.substring(1);
        if (s.endsWith("]")) s = s.substring(0, s.length() - 1);
        s = s.trim();
        if (s.isEmpty()) return new int[0][];
        List<int[]> rows = new ArrayList<>();
        int depth = 0, start = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') { if (depth == 0) start = i; depth++; }
            else if (c == ']') {
                depth--;
                if (depth == 0) rows.add(parseIntArray(s.substring(start, i + 1)));
            }
        }
        return rows.toArray(new int[0][]);
    }

    /** 解析形如 ["abc","def"] 的字符串数组。 */
    public static String[] parseStringArray(String s) {
        if (s == null) return new String[0];
        s = s.trim();
        if (s.startsWith("[")) s = s.substring(1);
        if (s.endsWith("]")) s = s.substring(0, s.length() - 1);
        s = s.trim();
        if (s.isEmpty()) return new String[0];
        List<String> out = new ArrayList<>();
        boolean inQuote = false;
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') {
                inQuote = !inQuote;
                if (!inQuote) { out.add(cur.toString()); cur.setLength(0); }
            } else if (inQuote) {
                cur.append(c);
            }
        }
        return out.toArray(new String[0]);
    }

    /** 解析形如 "abc" 或裸 abc 的单串。 */
    public static String parseString(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    /** 解析形如 [["a","b"],["c","d"]] 的二维 char 数组。 */
    public static char[][] parseChar2D(String s) {
        if (s == null) return new char[0][];
        s = s.trim();
        if (s.startsWith("[")) s = s.substring(1);
        if (s.endsWith("]")) s = s.substring(0, s.length() - 1);
        s = s.trim();
        if (s.isEmpty()) return new char[0][];
        List<char[]> rows = new ArrayList<>();
        int depth = 0, start = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') { if (depth == 0) start = i; depth++; }
            else if (c == ']') {
                depth--;
                if (depth == 0) {
                    String[] cells = parseStringArray(s.substring(start, i + 1));
                    char[] row = new char[cells.length];
                    for (int j = 0; j < cells.length; j++) row[j] = cells[j].charAt(0);
                    rows.add(row);
                }
            }
        }
        return rows.toArray(new char[0][]);
    }

    /** 打印二维 int 数组：每行一行，方便人眼对比。 */
    public static void print2D(int[][] a) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < a.length; i++) {
            sb.append(Arrays.toString(a[i]));
            if (i + 1 < a.length) sb.append(",");
        }
        sb.append("]");
        System.out.println(sb);
    }

    /** 打印二维 char 数组，每行一行。 */
    public static void print2D(char[][] a) {
        for (char[] row : a) System.out.println(new String(row));
    }
}
