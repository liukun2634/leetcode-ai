# 07 · 常用 Java 函数速查

> 前 6 篇讲容器和节点结构。这一篇收**散装但高频**的工具函数：`Math`、`Integer`、`Character`、`Arrays`、`Collections`、位运算、随机数等。
> 全部按「**LeetCode 出现频率**」排序。

---

## 一、`Math` —— 数学函数

```java
Math.max(a, b);          Math.min(a, b);
Math.abs(x);             // 注意 Math.abs(Integer.MIN_VALUE) 仍是负数（溢出）
Math.pow(a, b);          // 返回 double！整数幂用快速幂，别用它
Math.sqrt(x);            // double
Math.log(x);             // 自然对数
Math.log10(x);
Math.ceil(d);            // double，向上取整
Math.floor(d);           // double，向下取整
Math.round(d);           // long，四舍五入
Math.floorDiv(a, b);     // 真·向下整除（-7 / 2 = -3，floorDiv = -4）
Math.floorMod(a, b);     // 真·非负取模（-7 % 3 = -1，floorMod = 2）
Math.addExact(a, b);     // 溢出抛异常
Math.multiplyExact(a, b);
Math.gcd(...);           // ❌ Java 没有！用 BigInteger 或自写
```

### `Math.abs(Integer.MIN_VALUE)` 经典坑

```java
Math.abs(Integer.MIN_VALUE);   // 仍是 -2147483648（溢出）
// 安全：转 long
Math.abs((long) Integer.MIN_VALUE);
```

### 自写 GCD / LCM

```java
int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }
long lcm(int a, int b) { return (long) a / gcd(a, b) * b; }
```

### 快速幂（替代 `Math.pow`）

```java
long fastPow(long base, long exp, long mod) {
    long res = 1; base %= mod;
    while (exp > 0) {
        if ((exp & 1) == 1) res = res * base % mod;
        base = base * base % mod;
        exp >>= 1;
    }
    return res;
}
```

---

## 二、`Integer` / `Long` —— 数字与位运算

### 基本

```java
Integer.MAX_VALUE;  // 2^31 - 1 = 2147483647
Integer.MIN_VALUE;  // -2^31

Integer.parseInt("123");          // 抛异常
Integer.parseInt("FF", 16);       // 255
Integer.toString(255);            // "255"
Integer.toString(255, 16);        // "ff"
Integer.toBinaryString(5);        // "101"
Integer.toHexString(255);         // "ff"

Integer.compare(a, b);            // 安全比较（避免减法溢出）
Long.compare(a, b);
```

### 位操作（高频）

```java
Integer.bitCount(x);              // x 二进制中 1 的个数（LC 191）
Integer.highestOneBit(x);         // 最高位 1 对应的值（如 5 → 4）
Integer.lowestOneBit(x);          // 最低位 1 对应的值（lowbit），= x & -x
Integer.numberOfLeadingZeros(x);  // 前导 0 个数
Integer.numberOfTrailingZeros(x); // 末尾 0 个数
Integer.reverse(x);               // 二进制反转
Integer.reverseBytes(x);
```

### 位运算手写技巧

```java
x & 1                     // 取末位 / 判奇偶
x >> 1                    // 除以 2（带符号）
x >>> 1                   // 除以 2（无符号，高位补 0）
x | (1 << k)              // 第 k 位置 1
x & ~(1 << k)             // 第 k 位置 0
x ^ (1 << k)              // 第 k 位翻转
(x >> k) & 1              // 取第 k 位
x & -x                    // lowbit，取最低位 1
x & (x - 1)               // 去掉最低位 1
```

> **状压 DP / 子集枚举**：`for (int sub = mask; sub > 0; sub = (sub - 1) & mask)` 枚举 mask 所有非空子集。

---

## 三、`Character` —— 字符判断与转换

```java
Character.isDigit('7');            // true
Character.isLetter('a');           // true
Character.isLetterOrDigit('_');    // false
Character.isUpperCase('A');
Character.isLowerCase('a');
Character.isWhitespace(' ');
Character.isAlphabetic('β');       // 含 Unicode 字母

Character.toLowerCase('A');        // 'a'
Character.toUpperCase('a');        // 'A'

Character.getNumericValue('7');    // 7（'A' 返回 10，类似 16 进制）
Character.digit('F', 16);          // 15
```

### 字符 ↔ 数字

```java
'7' - '0'                  // 7
(char) ('a' + 3)           // 'd'
c - 'a'                    // 0..25 索引（小写）
c - 'A'                    // 0..25（大写）
```

---

## 四、`Arrays` —— 静态工具方法

> 已在 [01-array-and-string](./01-array-and-string.md) 提过，这里补充剩下高频的：

```java
Arrays.asList(1, 2, 3);                          // 定长 List<Integer>
Arrays.stream(a).sum();                          // int[] 求和
Arrays.stream(a).max().getAsInt();
Arrays.hashCode(a);                              // 一维数组哈希
Arrays.deepHashCode(grid);                       // 二维 / 嵌套数组哈希
Arrays.deepEquals(g1, g2);
Arrays.toString(a);                              // [1, 2, 3]
Arrays.deepToString(grid);                       // [[1,2],[3,4]]

// Java 8+ 并行（n ≥ 1e5 才有收益）
Arrays.parallelSort(a);

// Java 8+ setAll：按索引函数填充
int[] a = new int[10];
Arrays.setAll(a, i -> i * i);                    // [0,1,4,9,16,25,...]
```

---

## 五、`Collections` —— `List`/`Set`/`Map` 的工具方法

```java
Collections.sort(list);                          // 升序
Collections.sort(list, (x, y) -> y - x);          // 降序
Collections.reverse(list);                       // 原地反转
Collections.shuffle(list);                       // 洗牌（带种子见下）
Collections.shuffle(list, new Random(42));

Collections.max(list);
Collections.min(list);
Collections.frequency(list, x);                  // x 出现次数

Collections.swap(list, i, j);
Collections.fill(list, val);                     // 全填同一个值
Collections.nCopies(n, val);                     // 返回不可变 List，n 个 val

Collections.emptyList();   Collections.emptyMap();   Collections.emptySet();
Collections.singletonList(x);                    // 不可变单元素 List
```

> 不要用 `Collections.synchronizedXxx()` —— LeetCode 单线程，纯开销。

---

## 六、`String` 拼接 / 格式化

```java
String.join(",", "a", "b", "c");                 // "a,b,c"
String.join("-", list);                          // List<String> 拼接

String.format("%d-%s", 42, "x");                 // 类似 printf
String.format("%.2f", 3.14159);                  // "3.14"
String.format("%05d", 7);                        // "00007"

String.valueOf(123);                             // "123"
String.valueOf(new char[]{'a','b'});             // "ab"

"abc".repeat(3);                                 // "abcabcabc" (Java 11+)

new String(charArray);                           // char[] → String
new String(charArray, 0, len);                   // 截断
```

---

## 七、`Random` —— 随机

```java
Random rand = new Random();
rand.nextInt();                                  // 任意 int
rand.nextInt(n);                                 // [0, n)
rand.nextInt(l, r);                              // [l, r)  Java 17+
rand.nextLong(); rand.nextDouble(); rand.nextBoolean();

// 固定种子（调试 / 复现）
Random r2 = new Random(42);

// 更快的线程局部随机（单元素抽样常用）
ThreadLocalRandom.current().nextInt(n);
```

### 蓄水池抽样模板（LC 382）

```java
int pick(int target) {
    int count = 0, ans = -1;
    for (int i = 0; i < nums.length; i++) {
        if (nums[i] == target) {
            count++;
            if (rand.nextInt(count) == 0) ans = i;   // 1/count 概率替换
        }
    }
    return ans;
}
```

---

## 八、`Comparator` —— 比较器组合（Java 8+）

```java
// 单字段
Comparator.comparingInt(Person::age);

// 多字段：先按年龄升序，再按名字字典序
Comparator<Person> cmp = Comparator
    .comparingInt(Person::age)
    .thenComparing(Person::name);

// 降序
Comparator.comparingInt(Person::age).reversed();

// null 安全
Comparator.nullsFirst(Comparator.naturalOrder());

// int[] 例子
Arrays.sort(arr,
    Comparator.<int[]>comparingInt(a -> a[0])
              .thenComparingInt(a -> -a[1])
);
```

---

## 九、`Objects` —— 安全比较 / hash

```java
Objects.equals(a, b);             // null 安全的 .equals()
Objects.hashCode(obj);
Objects.hash(a, b, c);            // 自定义类重写 hashCode 用
Objects.requireNonNull(x, "msg"); // null 直接 NPE，带消息
```

### 自定义类的 `equals/hashCode` 一键模板

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Point p)) return false;     // Java 16+ pattern match
    return x == p.x && y == p.y;
}
@Override
public int hashCode() {
    return Objects.hash(x, y);
}
```

---

## 十、`BigInteger` / `BigDecimal` —— 大数

> LeetCode 偶尔遇到 `pow(a, b) mod m` 中 `a, b` 极大、或精确小数比较：

```java
BigInteger a = BigInteger.valueOf(123);
BigInteger b = new BigInteger("99999999999999999999");

a.add(b); a.subtract(b); a.multiply(b); a.divide(b); a.mod(b);
a.modPow(b, m);                       // 大数快速幂
a.gcd(b);                             // ✅ 自带 gcd！
a.compareTo(b);                       // -1/0/1

BigDecimal d = new BigDecimal("3.14");
d.setScale(2, RoundingMode.HALF_UP);
```

> 性能差，能不用就不用。求 GCD 时也可以 `BigInteger.valueOf(a).gcd(...)` 偷懒。

---

## 十一、`assert` & 简单调试

```java
// 命令行加 -ea 启用：java -ea Main
assert nums.length > 0 : "empty input";

// 打印数组
System.out.println(Arrays.toString(a));
System.out.println(Arrays.deepToString(grid));

// 计时
long t = System.nanoTime();
// ... 跑代码
System.out.println((System.nanoTime() - t) / 1_000_000 + " ms");
```

---

## 十二、回顾自测

1. `Math.abs(Integer.MIN_VALUE)` 等于多少？
2. `x & -x` 取的是什么？
3. `Integer.bitCount(7)` 等于多少？
4. `String.format("%05d", 7)` 输出什么？
5. 自定义类要当 `HashMap` 的 key，必须重写哪两个方法？
6. 想让 `-7 % 3` 得到正值 2，用哪个函数？

<details>
<summary>答案</summary>

1. `-2147483648`（自己本身），int 溢出。
2. lowbit —— 二进制最低位 1 对应的值。例如 `12 (1100) & -12 = 4 (100)`。
3. `3`。`7 = 111`。
4. `"00007"`。`%0Nd` 用 0 补足 N 位。
5. `hashCode()` 和 `equals()`，缺一不可。
6. `Math.floorMod(-7, 3)` → `2`。

</details>
