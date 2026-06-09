import java.io.*;
import java.util.*;

/**
 * ② BufferedReader + StringTokenizer（推荐 n ≤ 1e6，OA/笔试常用）。
 *    比 Scanner 快 5~10 倍。
 *
 * 示例题：求和
 *   输入：
 *     5
 *     1 2 3 4 5
 *   输出：
 *     15
 */
public class BufferedReaderMain {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += Integer.parseInt(st.nextToken());
        }

        // 输出加速：用 StringBuilder 拼好后一次输出
        StringBuilder out = new StringBuilder();
        out.append(sum).append('\n');
        System.out.print(out);
    }
}

/*
 ===== 常用片段 =====

 // 读单个 int
 int n = Integer.parseInt(br.readLine().trim());

 // 读一行 n 个 int
 StringTokenizer st = new StringTokenizer(br.readLine());
 int[] a = new int[n];
 for (int i = 0; i < n; i++) a[i] = Integer.parseInt(st.nextToken());

 // 不确定每行多少个 token：把所有输入合并再切
 String all = br.lines().collect(java.util.stream.Collectors.joining(" "));
 StringTokenizer st = new StringTokenizer(all);
 while (st.hasMoreTokens()) { ... }

 // 读字符串行
 String s = br.readLine();

 // 多组数据直到 EOF
 String line;
 while ((line = br.readLine()) != null) {
     if (line.isEmpty()) continue;
     // process
 }
*/
