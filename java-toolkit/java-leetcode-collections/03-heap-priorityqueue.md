# 03 · 堆 · `PriorityQueue`

> **结论**：`PriorityQueue` 默认是**小顶堆**，要大顶堆传 comparator。
> 所有堆操作 `offer`/`poll` 都是 O(log n)，`peek` 是 O(1)。

---

## 一、基本 API

```java
PriorityQueue<Integer> pq = new PriorityQueue<>();   // 小顶堆（自然序）
pq.offer(3);      // 入堆 O(log n)
pq.offer(1);
pq.offer(2);
pq.peek();        // 看堆顶 O(1)  → 1
pq.poll();        // 弹堆顶 O(log n) → 1
pq.size();
pq.isEmpty();
```

> 同 `Queue`：**`offer/poll/peek` 空时返回 null / false**，别用 `add/remove/element`。

---

## 二、大顶堆 —— 三种写法

```java
// 方式 1：自然序反转
PriorityQueue<Integer> max1 = new PriorityQueue<>(Comparator.reverseOrder());

// 方式 2：lambda（注意溢出！）
PriorityQueue<Integer> max2 = new PriorityQueue<>((a, b) -> b - a);      // 整型可能溢出
PriorityQueue<Integer> max3 = new PriorityQueue<>((a, b) -> Integer.compare(b, a)); // ✅ 推荐

// 方式 3：自定义比较器
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
```

> ⚠️ **不要写 `(a, b) -> b - a`**！当 `a = Integer.MIN_VALUE`、`b = 1` 时 `b - a` 溢出变负数 → 排序错。
> 习惯统一写 `Integer.compare(...)` 或 `Long.compare(...)`。

---

## 三、复合对象排序

```java
// 按第二个字段升序的小顶堆
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));

// 多字段：先按 a[0] 升序，相等时按 a[1] 降序
PriorityQueue<int[]> pq2 = new PriorityQueue<>(
    (a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]
);

// 用 Comparator 链式（更可读，推荐复杂场景）
PriorityQueue<int[]> pq3 = new PriorityQueue<>(
    Comparator.<int[]>comparingInt(a -> a[0])
              .thenComparingInt(a -> -a[1])
);
```

---

## 四、TopK 三大模板

### 模板 A：**Top K 大** → 维护**大小为 K 的小顶堆**

```java
public int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> pq = new PriorityQueue<>();   // 小顶堆
    for (int x : nums) {
        pq.offer(x);
        if (pq.size() > k) pq.poll();   // 弹掉当前最小的
    }
    return pq.peek();   // 堆里剩 K 个最大的，堆顶就是第 K 大
}
```

> **复杂度** O(n log k)，空间 O(k)。比直接全排 O(n log n) 优。

### 模板 B：**Top K 小** → 维护**大小为 K 的大顶堆**

```java
PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
for (int x : nums) {
    pq.offer(x);
    if (pq.size() > k) pq.poll();
}
// 堆里是 K 个最小的，堆顶是第 K 小
```

### 模板 C：**前 K 高频** → `HashMap` 统计 + 小顶堆

```java
public int[] topKFrequent(int[] nums, int k) {
    Map<Integer, Integer> cnt = new HashMap<>();
    for (int x : nums) cnt.merge(x, 1, Integer::sum);

    // 按频次升序的小顶堆，堆里留 K 个高频
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
    for (var e : cnt.entrySet()) {
        pq.offer(new int[]{e.getKey(), e.getValue()});
        if (pq.size() > k) pq.poll();
    }

    int[] ans = new int[k];
    for (int i = k - 1; i >= 0; i--) ans[i] = pq.poll()[0];
    return ans;
}
```

---

## 五、双堆 —— 数据流中位数（LC 295）

```java
class MedianFinder {
    PriorityQueue<Integer> lo = new PriorityQueue<>(Comparator.reverseOrder()); // 大顶堆：小一半
    PriorityQueue<Integer> hi = new PriorityQueue<>();                          // 小顶堆：大一半

    public void addNum(int num) {
        lo.offer(num);
        hi.offer(lo.poll());            // lo 最大丢到 hi
        if (lo.size() < hi.size())
            lo.offer(hi.poll());        // 维持 lo.size() == hi.size() 或 hi.size() + 1
    }

    public double findMedian() {
        return lo.size() > hi.size() ? lo.peek() : (lo.peek() + hi.peek()) / 2.0;
    }
}
```

---

## 六、常见坑

| 坑 | 现象 | 解决 |
|---|---|---|
| `(a, b) -> b - a` 溢出 | 偶现排序错乱 | 用 `Integer.compare(b, a)` |
| 遍历 `for (int x : pq)` | **顺序不是排序顺序**（是堆数组顺序） | 想有序遍历必须循环 `poll` |
| 改了堆里对象的字段 | 堆性质被破坏 | 先 `poll` 出来，改完再 `offer` 回去 |
| 没用 `isEmpty()` 检查就 `poll` | 返回 `null` 后续 NPE | `while (!pq.isEmpty())` |

---

## 七、回顾自测

1. `PriorityQueue` 默认是大顶堆还是小顶堆？
2. 求**第 K 大**，应该用大小为 K 的**大顶堆**还是**小顶堆**？为什么？
3. `(a, b) -> b - a` 有什么风险？
4. `for (int x : pq)` 输出的顺序保证排序吗？
5. `pq.poll()` 和 `pq.remove()` 在堆空时的行为分别是？

<details>
<summary>答案</summary>

1. 小顶堆（自然序）。
2. **小顶堆**。堆里只保留 K 个最大值，新元素来了和堆顶（K 个里最小的）比，比它大就替换。堆顶即第 K 大。
3. 当 `a` 是负的极大值时 `b - a` 溢出。改用 `Integer.compare(b, a)`。
4. **不保证**。`PriorityQueue` 用数组堆，迭代顺序是数组顺序而非排序顺序。要有序：反复 `poll`。
5. `poll` 返回 `null`，`remove` 抛 `NoSuchElementException`。

</details>
