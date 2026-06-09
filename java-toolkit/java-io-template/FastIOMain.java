import java.io.*;

/**
 * ③ StreamTokenizer + PrintWriter（最快，接近 C 速度，ACM/Hard 题必备）。
 *    适合 n ≥ 1e6 或时间紧的数据。
 *
 * 示例题：求和
 *   输入：
 *     5
 *     1 2 3 4 5
 *   输出：
 *     15
 */
public class FastIOMain {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int n = nextInt(in);
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += nextInt(in);
        }
        out.println(sum);

        out.flush(); // 必须 flush！
    }

    /** 读一个 int。 */
    private static int nextInt(StreamTokenizer in) throws IOException {
        in.nextToken();
        return (int) in.nval;
    }

    /** 读一个 long（StreamTokenizer 的 nval 是 double，超过 2^53 会丢精度，这种情况改用 BufferedReader）。 */
    private static long nextLong(StreamTokenizer in) throws IOException {
        in.nextToken();
        return (long) in.nval;
    }

    /** 读一个 double。 */
    private static double nextDouble(StreamTokenizer in) throws IOException {
        in.nextToken();
        return in.nval;
    }
}

/*
 ===== 想用 StreamTokenizer 读字符串？ =====
   默认它只解析数字。读字符串需要先配置：

   in.resetSyntax();
   in.wordChars('a', 'z');
   in.wordChars('A', 'Z');
   in.wordChars('0', '9');
   in.wordChars('_', '_');
   in.whitespaceChars(0, ' ');
   in.commentChar('#');

   然后：
   in.nextToken();
   String s = in.sval;   // 字符串
   // 或 in.ttype == StreamTokenizer.TT_NUMBER 时 in.nval 是数字

 ===== 大数 / 超长 long =====
   如果数字 > 2^53，StreamTokenizer 的 double 会丢精度。
   这时退回 BufferedReader + Long.parseLong / new BigInteger(...)。
*/
