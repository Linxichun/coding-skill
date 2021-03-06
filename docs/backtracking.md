# 回溯算法
其实回溯算法其实就是我们常说的 Depth-First Search (DFS：深度优先搜索) 算法，本质上就是一种暴力穷举算法。<br>
废话不多说，直接上回溯算法框架。解决一个回溯问题，实际上就是一个决策树的遍历过程。你只需要思考 3 个问题：<br>
1、路径：也就是已经做出的选择。<br>
2、选择列表：也就是你当前可以做的选择。<br>
3、结束条件：也就是到达决策树底层，无法再做选择的条件。

代码方面，回溯算法的框架：
```python
result = []
def backtrack(路径, 选择列表):
    if 满足结束条件:
        result.add(路径)
        return

    for 选择 in 选择列表:
        做选择
        backtrack(路径, 选择列表)
        撤销选择
```
其核心就是 for 循环里面的递归，在递归调用之前「做选择」，在递归调用之后「撤销选择」
## 全排列问题
我们在高中的时候就做过排列组合的数学题，我们也知道 n 个不重复的数，全排列共有 n! 个。<br>
PS：为了简单清晰起见，我们这次讨论的全排列问题不包含重复的数字。<br>
那么我们当时是怎么穷举全排列的呢？比方说给三个数 [1,2,3]，你肯定不会无规律地乱穷举，一般是这样：<br>
先固定第一位为 1，然后第二位可以是 2，那么第三位只能是 3；然后可以把第二位变成 3，第三位就只能是 2 了；然后就只能变化第一位，变成 2，然后再穷举后两位……<br>
详见[参考代码](../src/main/java/lin/xc/coding/skill/algorithm/backtracking/Case1.java)

## 凑零钱问题
这个问题很经典了，简单解释一下：给你一个 N×N 的棋盘，让你放置 N 个皇后，使得它们不能互相攻击。<br>
PS：皇后可以攻击同一行、同一列、左上左下右上右下四个方向的任意单位。<br>
这个问题本质上跟全排列问题差不多，决策树的每一层表示棋盘上的每一行；每个节点可以做出的选择是，在该行的任意一列放置一个皇后。<br>
详见[参考代码](../src/main/java/lin/xc/coding/skill/algorithm/backtracking/Case2.java)