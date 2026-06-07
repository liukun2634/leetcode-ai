# Java 本地刷题模板

> 目的：在本地用 `javac` + `java` 直接跑 LeetCode 风格输入，快速验证题解，不依赖在线判题。

## 目录结构

```
java-template/
├── README.md
├── LeetCodeIO.java     // 输入解析工具：[1,2,3] / [[1,2],[3,4]] / "abc" 等
├── ListNode.java       // 链表结构 + 构造/打印工具
├── TreeNode.java       // 二叉树结构 + 层序构造/打印工具
├── Solution.java       // 把你的题解放这里（默认是 Two Sum 示例）
└── Main.java           // 入口：从 stdin 读输入 → 调用 Solution → 打印结果
```

## 快速开始

### 编译 + 运行（PowerShell）

```powershell
cd interview-prep/java-template
javac *.java
# 方式一：交互输入
java Main
# 方式二：管道输入
"[2,7,11,15]`n9" | java Main
# 方式三：从文件读
Get-Content input.txt | java Main
```

### Two Sum 默认示例输入

```
[2,7,11,15]
9
```

输出：

```
[0, 1]
```

---

## 如何换题

1. 打开 [`Solution.java`](./Solution.java)，把方法签名换成目标题的签名；
2. 打开 [`Main.java`](./Main.java)，按需要调用 `LeetCodeIO` 的解析方法读输入；
3. `javac *.java` 重编译运行。

### 常见解析方法（在 `LeetCodeIO.java` 里）

| 输入样例 | 调用 |
|---|---|
| `[1,2,3,4]` | `LeetCodeIO.parseIntArray(line)` |
| `[[1,2],[3,4]]` | `LeetCodeIO.parseInt2D(line)` |
| `["abc","def"]` | `LeetCodeIO.parseStringArray(line)` |
| `"hello"` | `LeetCodeIO.parseString(line)` |
| `5` | `Integer.parseInt(line.trim())` |
| 链表 `[1,2,3]` | `ListNode.fromArray(LeetCodeIO.parseIntArray(line))` |
| 二叉树 `[1,2,3,null,4]` | `TreeNode.fromLevelOrder(line)` |

### 打印结果

| 类型 | 调用 |
|---|---|
| `int[]` | `System.out.println(Arrays.toString(arr))` |
| `int[][]` | `LeetCodeIO.print2D(arr)` |
| `List<List<Integer>>` | `System.out.println(list)` |
| `ListNode` | `ListNode.print(head)` |
| `TreeNode` | `TreeNode.printLevelOrder(root)` |

---

## 示例：换成 3Sum

`Solution.java`：

```java
import java.util.*;
public class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int l = i + 1, r = nums.length - 1;
            while (l < r) {
                int s = nums[i] + nums[l] + nums[r];
                if (s == 0) {
                    ans.add(Arrays.asList(nums[i], nums[l], nums[r]));
                    while (l < r && nums[l] == nums[l + 1]) l++;
                    while (l < r && nums[r] == nums[r - 1]) r--;
                    l++; r--;
                } else if (s < 0) l++;
                else r--;
            }
        }
        return ans;
    }
}
```

`Main.java`：

```java
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] nums = LeetCodeIO.parseIntArray(sc.nextLine());
        System.out.println(new Solution().threeSum(nums));
    }
}
```

输入 `[-1,0,1,2,-1,-4]` → 输出 `[[-1, -1, 2], [-1, 0, 1]]`。

---

## 调试小贴士

- **看中间状态**：直接 `System.out.println(...)` 打中间变量；正式跑题前注释掉即可。
- **断言验证**：`assert result == expected;` 跑时加 `java -ea Main` 启用断言。
- **多组用例**：把测试 case 写到 `tests/` 下的 `.txt`，写个 `run_all.ps1` 批量跑。
- **VS Code**：装 *Extension Pack for Java* 后，`Main.java` 顶部会出现 ▶ Run，点一下即可。
