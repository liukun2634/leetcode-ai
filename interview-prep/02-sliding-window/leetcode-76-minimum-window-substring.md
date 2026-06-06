# LeetCode 76. 最小覆盖子串 (Minimum Window Substring)

> 难度：Hard　|　标签：哈希表、字符串、滑动窗口　|	|	**滑动窗口天花板 ⭐⭐⭐⭐**

---

## 一、题目

给你一个字符串 `s` 和一个字符串 `t`。返回 `s` 中涵盖 `t` 所有字符的 **最小子串**。如果不存在则返回空字符串 `""`。

**注意**：
- `t` 中的每一个字符（含重复次数）都必须被覆盖
- 答案唯一

**约束**

- `1 <= m, n <= 10^5`
- `s` 和 `t` 由英文字母组成

**示例**

| 输入 | 输出 | 说明 |
|---|---|---|
| `s="ADOBECODEBANC", t="ABC"` | `"BANC"` | |
| `s="a", t="a"` | `"a"` | |
| `s="a", t="aa"` | `""` | 缺一个 a |

---

## 二、解题思路（学习重点）

### 1. 滑动窗口框架（变长窗口、求最短）

通用变长窗口骨架：
```text
l = 0, ans = ∞
for r = 0..n-1:
    把 s[r] 加入窗口
    while (窗口已覆盖 t):
        更新答案（窗口长度 r - l + 1）
        把 s[l] 移出窗口
        l++
```

**对比 LC 3（求最长不重复子串）**：
- LC 3：`while (窗口不合法)` 缩到合法，**更新答案在外面**
- LC 76：`while (窗口已合法)` 缩到刚好不合法，**更新答案在 while 内**（每次都更新）

> **学习点 ①**：**"求最短"用"满足后再缩"，"求最长"用"不满足才缩"**。这是变长窗口的两套模板。

### 2. 用 `valid` 计数避免每次比较两个哈希表

朴素做法：每移一次窗口都比较"窗口字符计数 == t 字符计数" → O(Σ) 每步。

优化：维护一个 `valid` 整数，表示"**有多少种 t 中要求的字符已经在窗口里数量达标**"。
- 当某字符的窗口计数刚好等于需要计数时 `valid++`；
- 当某字符的窗口计数从达标值减少时 `valid--`。
- 窗口已覆盖 ⇔ `valid == t 中不同字符的总数`。

> **学习点 ②**：用 `valid` 计数把 O(Σ) 比较降到 O(1)。**这是 76 这类题的核心优化**，必背。

### 3. 容易踩的坑

| 坑 | 处理 |
|---|---|
| 用 `Map` 操作慢，TLE 边缘 | 字符集小用 `int[128]` 数组 |
| `valid++` 的判断写成 `cnt[c] >= need[c]` → 多次累加 | 必须 `==`，让每个字符只增 1 次 |
| 缩窗口时漏掉"先比较再减" | 必须 `if (cnt[c] == need[c]) valid--;` 然后再 `cnt[c]--;` |
| 返回 `""` 的判定漏掉 | 用 `bestLen = Integer.MAX_VALUE` 当哨兵 |

---

## 三、详细解题步骤

**步骤 1**：统计 `t` 中每个字符的需要次数
```java
int[] need = new int[128];
for (char c : t.toCharArray()) need[c]++;
int required = 0;
for (int i = 0; i < 128; i++) if (need[i] > 0) required++;  // 需要"达标"的字符种数
```

**步骤 2**：初始化窗口变量
```java
int[] cnt = new int[128];
int l = 0, valid = 0;
int bestL = 0, bestLen = Integer.MAX_VALUE;
```

**步骤 3**：右指针扩
```java
for (int r = 0; r < s.length(); r++) {
    char c = s.charAt(r);
    cnt[c]++;
    if (need[c] > 0 && cnt[c] == need[c]) valid++;  // 刚好达标
    // 步骤 4 在这里展开
}
```

**步骤 4**：左指针在窗口满足时尽量缩
```java
    while (valid == required) {                    // 窗口已覆盖 t
        if (r - l + 1 < bestLen) {                 // 更新最优
            bestLen = r - l + 1;
            bestL = l;
        }
        char lc = s.charAt(l);
        if (need[lc] > 0 && cnt[lc] == need[lc]) valid--;  // 从达标变不达标
        cnt[lc]--;
        l++;
    }
```

**步骤 5**：返回
```java
return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestL, bestL + bestLen);
```

---

## 四、Java 题解（完整代码）

```java
class Solution {
    public String minWindow(String s, String t) {
        int[] need = new int[128];
        int required = 0;
        for (char c : t.toCharArray()) {
            if (need[c] == 0) required++;
            need[c]++;
        }

        int[] cnt = new int[128];
        int l = 0, valid = 0;
        int bestL = 0, bestLen = Integer.MAX_VALUE;

        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            cnt[c]++;
            if (need[c] > 0 && cnt[c] == need[c]) valid++;

            while (valid == required) {
                if (r - l + 1 < bestLen) {
                    bestLen = r - l + 1;
                    bestL = l;
                }
                char lc = s.charAt(l);
                if (need[lc] > 0 && cnt[lc] == need[lc]) valid--;
                cnt[lc]--;
                l++;
            }
        }
        return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestL, bestL + bestLen);
    }
}
```

**记忆口诀**：
> **"右扩满则左缩；'刚好达标'时 valid++，'刚好破标'时 valid--。"**

---

## 五、复杂度

| 项 | 复杂度 |
|---|---|
| 时间 | **O(m + n)** —— `l`、`r` 各最多走 n 步 |
| 空间 | O(Σ)，Σ = 128 |

---

## 六、示例验证

`s = "ADOBECODEBANC", t = "ABC"`，`need = {A:1, B:1, C:1}`，required = 3

只列关键步骤：

| r | s[r] | cnt | valid | 窗口 | 操作 |
|---|---|---|---|---|---|
| 0 | A | A:1 | 1 | [0..0] | — |
| 1 | D | A:1,D:1 | 1 | [0..1] | — |
| 2 | O | ...O:1 | 1 | [0..2] | — |
| 3 | B | ...B:1 | 2 | [0..3] | — |
| 4 | E | ...E:1 | 2 | [0..4] | — |
| 5 | C | ...C:1 | **3** | [0..5] "ADOBEC" | **缩**：bestLen=6, l→1 (A 减) → valid=2 退出 |
| ... | ... | ... | ... | ... | ... |
| 10 | A | A:2 | 3 | [...] | 缩到 [9..10]?... |
| 12 | C | ...C 再现 | 3 | [9..12] "BANC" 长 4 | bestLen=4 ✅ |

输出 `"BANC"` ✅

---

## 七、复盘与延伸

### 一句话总结
> **变长窗口求最短：先扩到满足，再缩到刚好不满足；用 valid 计数判满足，O(1) 更新。**

### 自我提问
1. 为什么 `valid` 用计数而不是直接对比两表？→ 比较两表 O(Σ)，每步乘 n 共 O(nΣ)；valid 是 O(1)，全程 O(n)。
2. 求最短和求最长在缩窗口位置上的区别？→ **求最短**在 while 顶端更新；**求最长**在 while 外更新。
3. 怎么改成 **窗口含 t 全字符（顺序敏感）**？→ 这是子序列问题，需 DP/双指针，不是滑动窗口。
4. `t` 含重复怎么办？→ 本算法已支持：`need[c]` 存的就是次数。

### 同类型推荐（**变长滑动窗口家族**）
- LC 3. 无重复字符的最长子串
- LC 209. 长度最小的子数组（求最短）
- LC 567. 字符串的排列（固定长度窗口）
- LC 438. 找到字符串中所有字母异位词
- LC 30. 串联所有单词的子串
- LC 159. 至多包含两个不同字符的最长子串
- LC 340. 至多包含 K 个不同字符的最长子串
- LC 992. K 个不同整数的子数组（**恰好转换成"至多 K"减"至多 K-1"** 的经典套路）
