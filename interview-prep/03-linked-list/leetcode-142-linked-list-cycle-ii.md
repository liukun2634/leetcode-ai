# LeetCode 142. 环形链表 II (Linked List Cycle II)

> 难度：Medium　|　标签：链表、快慢指针、Floyd 判圈　|	|	**快慢指针经典 ⭐⭐⭐**

---

## 一、题目

给定一个链表的头节点 `head`，返回链表开始入环的 **第一个节点**。如果链表无环，则返回 `null`。

**不允许修改链表**。

**进阶**：你是否可以使用 `O(1)` 空间解决此题？

**约束**

- 链表中节点的数目范围在 `[0, 10^4]`

**示例**

```
   3 → 2 → 0 → -4
       ↑       ↓
       └───────┘
入环节点：2
```

---

## 二、解题思路（学习重点）

### 1. 朴素解法：HashSet 记录走过的节点

第一个被重复访问的节点即入环点。O(n) 时间、O(n) 空间。

### 2. **Floyd 判圈算法（龟兔赛跑）**：O(1) 空间

**第一阶段（判环）**：快指针 `fast` 每次走两步，慢指针 `slow` 每次走一步。若 `fast` 走到 `null` → 无环。若两指针相遇 → 有环。

**第二阶段（找入环点）**：相遇后，把 `slow` 重置回 `head`，然后 **slow 和 fast 都每次走一步**，再次相遇的节点就是入环点。

### 3. 为什么第二阶段成立？（必须能现场推导）

设：
- 链表头到入环点距离 = `a`
- 入环点到相遇点距离（顺环方向） = `b`
- 相遇点回到入环点距离 = `c`（即环长 = `b + c`）

第一次相遇时：
- slow 走了 `a + b`
- fast 走了 `a + b + k(b + c)`（k 是 fast 在环里多绕的圈数，至少 1）
- 因为 fast 速度是 slow 两倍：`2(a + b) = a + b + k(b + c)`
  → `a + b = k(b + c)` → **`a = k(b + c) - b = (k-1)(b+c) + c`**

含义：**从 head 走 `a` 步到入环点，等价于从相遇点走 `c + (k-1)圈`**，也是到入环点。

所以让一个指针从 head、另一个从相遇点同步前进，**必然在入环点首次相遇**。

> **学习点 ①**：这是 **数学推理 + 双指针** 的最优雅例子，面试可以画图边推边讲，加分项。

### 4. 容易踩的坑

| 坑 | 处理 |
|---|---|
| 第一阶段 `while (fast != slow)` 起手就退出 | 必须 `do-while` 或在循环里检查 |
| 第二阶段没把 slow 重置到 head | 算法直接错 |
| 空链表 / 单节点没环 | `fast == null || fast.next == null` 提前返回 |

---

## 三、详细解题步骤

**步骤 1**：判环
```java
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
    if (slow == fast) break;   // 相遇 → 有环
}
if (fast == null || fast.next == null) return null;  // 无环
```

**步骤 2**：找入环点
```java
slow = head;
while (slow != fast) {
    slow = slow.next;
    fast = fast.next;       // 注意：每次走一步！
}
return slow;
```

---

## 四、Java 题解

### 解法 A：Floyd 判圈（推荐，O(1) 空间）

```java
public class Solution {
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {                  // 第一阶段：相遇
                slow = head;
                while (slow != fast) {            // 第二阶段：同速前进
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        return null;
    }
}
```

**记忆口诀**：
> **"快慢相遇有环；慢回头同速走，再相遇即入口。"**

### 解法 B：HashSet（直观）

```java
public class Solution {
    public ListNode detectCycle(ListNode head) {
        Set<ListNode> seen = new HashSet<>();
        for (ListNode cur = head; cur != null; cur = cur.next) {
            if (!seen.add(cur)) return cur;
        }
        return null;
    }
}
```

---

## 五、复杂度

| 解法 | 时间 | 空间 |
|---|---|---|
| Floyd 判圈 | **O(n)** | **O(1)** |
| HashSet | O(n) | O(n) |

---

## 六、示例验证

`3 → 2 → 0 → -4 → (回到 2)`

**第一阶段**：
| 步 | slow | fast |
|---|---|---|
| init | 3 | 3 |
| 1 | 2 | 0 |
| 2 | 0 | 2（绕了一圈） |
| 3 | -4 | -4 | **相遇** |

**第二阶段**：slow 重置到 head
| 步 | slow | fast |
|---|---|---|
| init | 3 | -4 |
| 1 | 2 | 2 | **相遇** ✅

返回入环点 `2` ✅

---

## 七、复盘与延伸

### 一句话总结
> **Floyd 算法：快慢相遇判环；慢指针回头与快指针同速走，再相遇即入环点。**

### 自我提问
1. 为什么 fast 一定能追上 slow（而不会"跳过"）？→ fast 与 slow 距离每步缩短 1，必然在环内相遇。
2. 第一阶段相遇点 = 入环点吗？→ **不是**，相遇点在环内任意位置。
3. 怎么求环长？→ 第一次相遇后让 fast 再走一圈，回到相遇点的步数即环长。
4. 怎么求链表长度（含 head 到入环点 + 环长）？→ 入环点 + 环长一一拆开。

### 同类型推荐（**快慢指针家族**）
- LC 141. 环形链表（只判存在）
- LC 287. 寻找重复数（**数组转链表**，Floyd 神级应用）
- LC 19. 删除链表的倒数第 N 个结点（快慢指针差距 N）
- LC 876. 链表的中间结点
- LC 234. 回文链表（找中点 + 反转后半）
- LC 202. 快乐数（Floyd 判循环）
