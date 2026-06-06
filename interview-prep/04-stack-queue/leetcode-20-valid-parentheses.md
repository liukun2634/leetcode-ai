# LeetCode 20. 有效的括号 (Valid Parentheses)

> 难度：Easy　|　标签：栈、字符串　|　**栈入门必刷 ⭐⭐⭐**

---

## 一、题目

给定一个只包括 `'('`，`')'`，`'{'`，`'}'`，`'['`，`']'` 的字符串 `s`，判断字符串是否有效。

有效字符串需满足：
1. 左括号必须用 **相同类型** 的右括号闭合。
2. 左括号必须以 **正确的顺序** 闭合（最后开的最先关 → **后进先出**）。

**约束**

- `1 <= s.length <= 10^4`

**示例**

| 输入 | 输出 |
|---|---|
| `"()"` | `true` |
| `"()[]{}"` | `true` |
| `"(]"` | `false` |
| `"([)]"` | `false` |
| `"{[]}"` | `true` |

---

## 二、解题思路（学习重点）

### 1. 为什么是栈？

"最后开的最先关" = **LIFO** = **栈** 的天然定义。

每遇到左括号入栈，遇到右括号时检查栈顶是否匹配：
- 匹配 → 出栈，继续
- 不匹配 / 栈空 → 直接 `false`

最后栈必须为空（所有左括号都被配对）。

> **学习点 ①**：**"嵌套结构 / 匹配性问题"** → 第一反应应是栈。如表达式求值、HTML/XML 校验、最长有效括号。

### 2. 优雅写法：把右括号入栈

遇左括号时入栈"它期望的右括号"，遇右括号时直接判等：

```text
'(' → push ')'
'[' → push ']'
'{' → push '}'
其他 → pop 并与当前字符比较
```

这样比"判断三种左右配对"的 if-else 干净得多。

### 3. 容易踩的坑

| 坑 | 处理 |
|---|---|
| 用 `Stack`（同步开销大） | 用 `ArrayDeque<Character>` 更快 |
| 遇到右括号前不判空 → `pop` 抛异常 | **先判 `isEmpty()` 返 false** |
| 字符串奇数长度还硬跑 | 可加剪枝：`s.length() % 2 != 0` 直接 false |

---

## 三、Java 题解

### 解法 A：栈（推荐写法）

```java
class Solution {
    public boolean isValid(String s) {
        if ((s.length() & 1) == 1) return false;  // 奇数必假
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(') stack.push(')');
            else if (c == '[') stack.push(']');
            else if (c == '{') stack.push('}');
            else if (stack.isEmpty() || stack.pop() != c) return false;
        }
        return stack.isEmpty();
    }
}
```

**记忆口诀**：
> **"左来推右，右来比顶，最终栈空才算赢。"**

### 解法 B：HashMap 维护配对关系（适合括号种类多）

```java
class Solution {
    public boolean isValid(String s) {
        Map<Character, Character> pair = Map.of(')', '(', ']', '[', '}', '{');
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (!pair.containsKey(c)) {           // 是左括号
                stack.push(c);
            } else {
                if (stack.isEmpty() || stack.pop() != pair.get(c)) return false;
            }
        }
        return stack.isEmpty();
    }
}
```

---

## 四、复杂度

| 项 | 复杂度 |
|---|---|
| 时间 | **O(n)** |
| 空间 | O(n) 栈最坏全是左括号 |

---

## 五、示例验证

`s = "{[]}"`

| i | c | 栈（顶在右）| 操作 |
|---|---|---|---|
| 0 | `{` | `}` | push 期望的 `}` |
| 1 | `[` | `} ]` | push `]` |
| 2 | `]` | `}` | 栈顶 `]` == `]` ✅ pop |
| 3 | `}` | (空) | 栈顶 `}` == `}` ✅ pop |

栈空 → 返回 `true` ✅

`s = "([)]"`

| i | c | 栈 | 操作 |
|---|---|---|---|
| 0 | `(` | `)` | push |
| 1 | `[` | `) ]` | push |
| 2 | `)` | `) ]` | 栈顶 `]` != `)` → **false** |

---

## 六、复盘与延伸

### 一句话总结
> **括号配对 = LIFO = 栈。左推右、右比顶、终须空。**

### 自我提问
1. 为什么栈比"成对计数"行不通？→ 计数法（如 `(((` 也能用 `count++/--`）无法区分类型，处理 `([)]` 必出错。
2. 如果允许字符串里含字母呢（如 `"a(b)c"`）？→ 跳过非括号字符即可，主结构不变。
3. 怎么扩展到 **最长有效括号**（LC 32）？→ 栈里压 **下标**，配对时计算长度。

### 同类型推荐（**栈家族**）
- LC 32. 最长有效括号（栈存下标 / DP）
- LC 1249. 移除无效的括号
- LC 921. 使括号有效的最少添加
- LC 150. 逆波兰表达式求值
- LC 71. 简化路径
- LC 394. 字符串解码
- LC 84. 柱状图中最大的矩形（**单调栈**入门天花板）
- LC 739. 每日温度（单调栈）
