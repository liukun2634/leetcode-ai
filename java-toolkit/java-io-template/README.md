# Java 通用输入模板（人工/竞赛风格）

> 适用场景：自己手敲输入测试、ACM/笔试题、数据量较大需要快速 I/O。
> 输入格式：空格/换行分隔，**不需要** `[1,2,3]` 这种括号包裹。
> 想要 LeetCode 风格（带 `[]`）请用同级 [`java-leetcode-io-template`](../java-leetcode-io-template/README.md)。

## 目录结构

```
java-io-template/
├── README.md
├── ScannerMain.java         // ① 最简单：Scanner（n ≤ 10^5 都够用）
├── BufferedReaderMain.java  // ② 中速：BufferedReader + StringTokenizer（n ≤ 10^6）
├── FastIOMain.java          // ③ 最快：StreamTokenizer + PrintWriter（n ≥ 10^6 / ACM 必备）
├── input.txt
└── .gitignore
```

## 示例题目

> 给定 `n`，第二行有 `n` 个整数，输出它们的和。

输入：
```
5
1 2 3 4 5
```
输出：
```
15
```

三个模板做同一件事，方便你对比写法。

## 编译运行（PowerShell）

```powershell
cd java-toolkit/java-io-template
javac *.java

# 交互输入：直接键盘敲
java ScannerMain

# 从文件读
Get-Content input.txt | java ScannerMain
Get-Content input.txt | java BufferedReaderMain
Get-Content input.txt | java FastIOMain

# 内联输入
"5`n1 2 3 4 5" | java FastIOMain
```

---

## 三种 I/O 方案速选

| 方案 | 优点 | 缺点 | 何时用 |
|---|---|---|---|
| `Scanner` | API 易用，`nextInt/nextLine/nextDouble` 直接调 | 慢（内部正则解析），不适合大数据 | n ≤ 10^5，面试/学习 |
| `BufferedReader + StringTokenizer` | 比 Scanner 快 5~10 倍，仍易写 | 要自己 `parseInt` | n ≤ 10^6，OA 笔试 |
| `StreamTokenizer + PrintWriter` | 接近 C 的速度 | 只能读数字，不便读字符串 | n ≥ 10^6，ACM/Hard |

---

## ① Scanner 用法速查

```java
Scanner sc = new Scanner(System.in);
int n          = sc.nextInt();
long x         = sc.nextLong();
double d       = sc.nextDouble();
String tok     = sc.next();          // 读一个空白分隔的 token
String line    = sc.nextLine();      // 读整行（注意混用 nextInt 后要先吃掉换行）
while (sc.hasNextInt()) { ... }      // 读到 EOF
```

**坑**：`nextInt()` 之后想 `nextLine()` 读整行，要先调一次 `sc.nextLine()` 把残留的 `\n` 吃掉。

---

## ② BufferedReader + StringTokenizer 用法速查

```java
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
int n = Integer.parseInt(br.readLine().trim());

StringTokenizer st = new StringTokenizer(br.readLine());
int[] a = new int[n];
for (int i = 0; i < n; i++) a[i] = Integer.parseInt(st.nextToken());

// 也可以全部一次读完
String all = br.lines().collect(java.util.stream.Collectors.joining(" "));
StringTokenizer st = new StringTokenizer(all);
while (st.hasMoreTokens()) { ... }
```

**输出加速**：用 `StringBuilder` 拼好后一次 `System.out.print`，比多次 `println` 快很多。

---

## ③ StreamTokenizer + PrintWriter 用法速查

```java
StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

in.nextToken(); int n = (int) in.nval;        // 读 int
in.nextToken(); double d = in.nval;            // 读 double
in.nextToken(); String s = in.sval;            // 读字符串（需 in.wordChars 配置，默认只识别数字）

out.println(answer);
out.flush();                                   // 必须 flush！
```

**配置识别字符串**（如果你需要读 `"abc"` 这种 token）：

```java
in.resetSyntax();
in.wordChars('a', 'z'); in.wordChars('A', 'Z'); in.wordChars('0', '9');
in.whitespaceChars(0, ' ');
```

---

## 何时切换到 LeetCode 模板？

| 场景 | 用哪个 |
|---|---|
| 复现 LeetCode 题（输入是 `[2,7,11,15]` + `9`） | [`../java-leetcode-io-template`](../java-leetcode-io-template/README.md) |
| 牛客/PAT/ACM/笔试（输入是 `5\n1 2 3 4 5`） | 本目录 |
