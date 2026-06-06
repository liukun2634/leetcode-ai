# LeetCode 21. 合并两个有序链表 (Merge Two Sorted Lists)

> 难度：Easy　|　标签：链表、递归　|	|	**dummy 头模板必背 ⭐⭐⭐**

---

## 一、题目

将两个升序链表合并为一个新的 **升序** 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。

**约束**

- 两个链表的节点数目范围是 `[0, 50]`

**示例**

```
输入：list1 = 1 → 2 → 4
      list2 = 1 → 3 → 4
输出：1 → 1 → 2 → 3 → 4 → 4
```

---

## 二、解题思路（学习重点）

### 1. dummy 头节点：链表题的"统一边界处理神器"

**核心痛点**：链表合并时第一个新节点要"格外照顾"（要么需要 `if` 判断当前是不是第一个，要么需要单独写代码）。

**dummy 节点**：在结果链表前 **多放一个哨兵节点**，所有插入操作都按"接在某节点后面"统一处理，最后返回 `dummy.next`。

> **学习点 ①**：**任何"构造新链表" / "可能删头节点" 的题，都先 `new ListNode(-1)` 当 dummy**。如：LC 21（合并）、LC 23（k 路合并）、LC 25（k 个一组反转）、LC 86（分割）、LC 203（删除值为 val 的节点）。

### 2. 标准模板

```text
dummy = new ListNode(-1)
tail = dummy
while l1 != null && l2 != null:
    if l1.val <= l2.val:
        tail.next = l1
        l1 = l1.next
    else:
        tail.next = l2
        l2 = l2.next
    tail = tail.next
tail.next = (l1 != null) ? l1 : l2   // 接上剩余
return dummy.next
```

### 3. 容易踩的坑

| 坑 | 处理 |
|---|---|
| 忘记 `tail = tail.next` → 死循环 | 模板第 8 行必写 |
| 没接剩余链表 → 漏一截 | while 之后单独一行 |
| 用 `<` 而非 `<=` 影响稳定性？ | 本题不影响，按习惯即可 |

---

## 三、详细解题步骤（迭代法）

**步骤 1**：建 dummy 与 tail 指针
```java
ListNode dummy = new ListNode(-1);
ListNode tail = dummy;
```

**步骤 2**：循环比较两个链表头
```java
while (l1 != null && l2 != null) {
    if (l1.val <= l2.val) {
        tail.next = l1;      // 把 l1 接到尾巴
        l1 = l1.next;        // l1 前移
    } else {
        tail.next = l2;
        l2 = l2.next;
    }
    tail = tail.next;        // 尾巴前移
}
```

**步骤 3**：把还剩的那个链表整条接到尾巴
```java
tail.next = (l1 != null) ? l1 : l2;
```

**步骤 4**：返回 `dummy.next`（跳过 dummy 哨兵）。

---

## 四、Java 题解

### 解法 A：迭代 + dummy（推荐）

```java
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1), tail = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                tail.next = l1;
                l1 = l1.next;
            } else {
                tail.next = l2;
                l2 = l2.next;
            }
            tail = tail.next;
        }
        tail.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }
}
```

**记忆口诀**：
> **"dummy 当锚、tail 走龙；小的接、大的等、剩的整条挂。"**

### 解法 B：递归

```java
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        if (l1.val <= l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }
}
```

> 面试推荐迭代版（O(1) 额外空间），递归当亮点补充。

---

## 五、复杂度

| 解法 | 时间 | 空间 |
|---|---|---|
| 迭代 | **O(m + n)** | **O(1)** |
| 递归 | O(m + n) | O(m + n) 递归栈 |

---

## 六、示例验证

`l1 = 1→2→4`，`l2 = 1→3→4`

| 步 | l1 | l2 | 比较 | 接谁 | tail 链表 |
|---|---|---|---|---|---|
| 1 | 1 | 1 | 1≤1 | l1 | dummy→1 |
| 2 | 2 | 1 | 2>1 | l2 | dummy→1→1 |
| 3 | 2 | 3 | 2≤3 | l1 | dummy→1→1→2 |
| 4 | 4 | 3 | 4>3 | l2 | dummy→1→1→2→3 |
| 5 | 4 | 4 | 4≤4 | l1 | dummy→1→1→2→3→4 |
| 6 | null | 4 | 退出循环 | 剩 l2 整条挂 | dummy→1→1→2→3→4→4 |

返回 `dummy.next = 1→1→2→3→4→4` ✅

---

## 七、复盘与延伸

### 一句话总结
> **dummy + tail 走龙：小的接、大的等、剩的整条挂。**

### 自我提问
1. 不用 dummy 怎么写？→ 要单独处理"第一个节点"，代码长一截。
2. 为什么不用复制节点值？→ 直接复用原链表节点更高效。
3. 怎么处理 K 路合并？→ LC 23，**小顶堆** 装 k 个头节点；或两两合并 + 分治。
4. 链表降序？→ 把 `<=` 改 `>=` 即可。

### 同类型推荐（**dummy + 双指针 家族**）
- LC 23. 合并 K 个升序链表（K 路 + 堆）
- LC 86. 分割链表（双 dummy）
- LC 203. 删除链表中等于给定值的节点
- LC 1669. 合并两个链表（区间删除 + 插入）
- LC 88. 合并两个有序数组（数组版，从后往前更妙）
