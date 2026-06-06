# LeetCode 206. 反转链表 (Reverse Linked List)

> 难度：Easy　|　标签：链表、递归、双指针　|　**链表入门必背 ⭐⭐⭐**

---

## 一、题目

给你单链表的头节点 `head`，请你反转链表，并返回反转后的链表头。

**约束**

- 链表中节点的数目范围是 `[0, 5000]`

**示例**

```
输入：1 → 2 → 3 → 4 → 5 → null
输出：5 → 4 → 3 → 2 → 1 → null
```

---

## 二、解题思路（学习重点）

### 1. 迭代法：三指针滚动（必背模板）

维护三个指针：
- `prev`：已反转部分的头（初始 `null`）
- `curr`：当前要处理的节点（初始 `head`）
- `next`：暂存 `curr.next`，否则反转后会断开

每一轮：
```
next = curr.next
curr.next = prev   // 反转一根边
prev = curr        // prev 前移
curr = next        // curr 前移
```

> **学习点 ①**：**"操作前先保存 next"** 是所有链表修改题的肌肉记忆。

### 2. 递归法：理解链表是天然的递归结构

定义：`reverse(head)` = 把以 `head` 开头的链表反转，返回新头。

递归式：
- 若 `head` 或 `head.next` 为 null，返回 `head`（递归出口）。
- 否则：`newHead = reverse(head.next)`，此时 `head.next` 已是反转后链表的尾。
- 让它指回来：`head.next.next = head`；`head.next = null`（断开旧指向，否则成环）。
- 返回 `newHead`（递归过程中始终是最深处那个节点）。

> **学习点 ②**：递归思路的关键是 **画出"递归回溯那一刻"的局部图**，而不是从头模拟。

### 3. 容易踩的坑

| 坑 | 处理 |
|---|---|
| 没暂存 `curr.next` → 直接 `curr.next = prev` 后丢失后续 | **先暂存** |
| 递归忘记 `head.next = null` | 会形成 `head ↔ head.next` 的环 |
| 空链表 / 单节点 | 两种解法都自然处理（迭代时 `curr=null` 直接退出） |

---

## 三、Java 题解

### 解法 A：迭代三指针（强烈推荐，必须熟到秒写）

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode next = curr.next; // 暂存
            curr.next = prev;          // 反转
            prev = curr;               // prev 前移
            curr = next;               // curr 前移
        }
        return prev; // 新头
    }
}
```

**记忆口诀**：
> **"暂存、反转、前移、前移。"**（next / curr.next = prev / prev = curr / curr = next）

### 解法 B：递归

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }
}
```

---

## 四、复杂度

| 解法 | 时间 | 空间 |
|---|---|---|
| 迭代 | **O(n)** | **O(1)** |
| 递归 | O(n) | O(n) 递归栈 |

> 面试默认写迭代版；递归当亮点补充。

---

## 五、示例验证

`1 → 2 → 3 → null`

| 轮次 | prev | curr | next | 操作后链表 |
|---|---|---|---|---|
| init | null | 1 | — | `1→2→3` |
| 1 | 1 | 2 | 2 | `1→null` 旁边 `2→3` |
| 2 | 2 | 3 | 3 | `2→1→null` 旁边 `3` |
| 3 | 3 | null | null | `3→2→1→null` |

返回 `prev = 3` ✅

---

## 六、复盘与延伸

### 一句话总结
> **"暂存 → 反转 → 双前移"，三指针滚动一遍即得反转链表。**

### 自我提问
1. 为什么 `prev` 初始要是 `null` 而不是 `head`？→ 反转后原 `head` 是尾节点，尾节点的 `next` 必须是 `null`。
2. 递归版的栈深度多少？是否会爆栈？→ 与链表长度同阶，本题 5000 不会爆；10⁵ 量级要改迭代。
3. 反转链表能否原地"只动指针不动值"？→ 上面两种解法都是。
4. 如何写"反转第 m 到第 n 个节点"？→ 配合 **dummy 头**，先走到 m-1，再用本模板反转 (n - m + 1) 步。

### 同类型推荐（**链表模板家族**）
- LC 92. 反转链表 II（区间反转）
- LC 25. K 个一组翻转链表（递归套迭代）
- LC 24. 两两交换链表中的节点
- LC 21. 合并两个有序链表（dummy 头模板）
- LC 141 / 142. 环形链表（快慢指针）
- LC 160. 相交链表（双指针消除长度差）
- LC 234. 回文链表（找中点 + 反转后半 + 比较）
