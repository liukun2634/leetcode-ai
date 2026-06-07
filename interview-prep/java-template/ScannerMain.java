import java.util.*;

/**
 * ① 最简单的 Scanner 模板（推荐用于 n ≤ 1e5 的题目）。
 *
 * 示例题：求和
 *   输入：
 *     5
 *     1 2 3 4 5
 *   输出：
 *     15
 */
public class ScannerMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += sc.nextInt();
        }
        System.out.println(sum);

        sc.close();
    }
}

/*
 ===== Scanner API 速查 =====
   sc.nextInt()        读一个 int
   sc.nextLong()       读一个 long
   sc.nextDouble()     读一个 double
   sc.next()           读一个空白分隔的 token (String)
   sc.nextLine()       读一整行
   sc.hasNext()        是否还有 token（读到 EOF 时为 false）
   sc.hasNextInt()     下一个 token 是不是 int

 ===== 常见坑 =====
   nextInt() 后想读整行：要先调一次 sc.nextLine() 把残留的 \n 吃掉。
*/
