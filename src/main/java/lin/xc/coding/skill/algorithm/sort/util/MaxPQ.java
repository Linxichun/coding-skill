package lin.xc.coding.skill.algorithm.sort.util;

import lin.xc.coding.skill.algorithm.sort.std.StdIn;
import lin.xc.coding.skill.algorithm.sort.std.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/******************************************************************************
 * 最大堆（最大值在堆的根节点） 优先队列
 *  Compilation:  javac MaxPQ.java
 *  Execution:    java MaxPQ < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *
 *  Generic max priority queue implementation with a binary heap.
 *  Can be used with a comparator instead of the natural order,
 *  but the generic Key type must still be Comparable.
 *
 *  % java MaxPQ < tinyPQ.txt
 *  Q X P (6 left on pq)
 *
 *  We use a one-based array to simplify parent and child calculations.
 *
 *  Can be optimized by replacing full exchanges with half exchanges
 *  (ala insertion sort).
 *  @author Robert Sedgewick
 ******************************************************************************/
public class MaxPQ<Key> implements Iterable<Key> {

    private Key[] pq;                    // 基于堆的完全二叉树
    private int N;                       // 存储于pq[1..N]中，pq[0]没有使用，这样就相当于元素个数了
    private Comparator<Key> comparator;  // 可选的比较器

    /**
     * 构造函数，自定义容量
     * */
    public MaxPQ(int initCapacity) {
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

    /**
     * 无参构造函数，默认容量=1
     */
    public MaxPQ(){
        this(1);
    }

    /**
     * 构造函数，带容量参数和比较器
     * */
    public MaxPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

    public MaxPQ(Comparator<Key> comparator) {
        this(1, comparator);
    }

    /**
     * 用keys[]中的元素创建一个优先队列
     */
    public MaxPQ(Key[] keys) {
        // 设置大小值
        N = keys.length;
        // 定义数组
        pq = (Key[]) new Object[keys.length + 1];
        // 先将元素全部存入pg数组
        for (int i = 0; i < N; i++){
            pq[i+1] = keys[i];
        }
        //
        for (int k = N/2; k >= 1; k--){
            sink(k);
        }
        assert isMaxHeap();
    }

    /**
     * 返回队列是否为空
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * 返回优先队列中的元素个数
     */
    public int size() {
        return N;
    }

    /**
     * 返回最大元素
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    /**
     * 重新定义大小
     * */
    private void resize(int capacity) {
        assert capacity > N;
        // 重新定义一个新元素大小的数组
        Key[] temp = (Key[]) new Object[capacity];
        // 将之前的所有元素拷贝过去
        for (int i = 1; i <= N; i++) {
            temp[i] = pq[i];
        }
        // 将新数组重新赋值给pq
        pq = temp;
    }

    /**
     * 向优先队列中插入一个元素
     */
    public void insert(Key x) {
        // double size of array if necessary
        if (N >= pq.length - 1){
            resize(2 * pq.length);
        }
        // add x, and percolate it up to maintain heap invariant
        pq[++N] = x;
        swim(N);
        assert isMaxHeap();
    }

    /**
     * 删除并返回最大元素
     */
    public Key delMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        Key max = pq[1];//从根结点得到最大元素
        exch(1, N--);   //将其和最后一个结点交换
        sink(1);    //恢复堆的有序性
        pq[N+1] = null;     //防止越界
        if ((N > 0) && (N == (pq.length - 1) / 4)) resize(pq.length / 2);
        assert isMaxHeap();
        return max;
    }

    /***************************************************************************
     * Helper functions to restore the heap invariant.
     ***************************************************************************/
    /**
     * 上浮
     * */
    private void swim(int k) {
        // 当本下标元素比父节点（下标为的k/2结点）大时，交换（即上浮）
        while (k > 1 && less(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    /**
     * 下沉
     * */
    private void sink(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && less(j, j+1)){
                j++;
            }
            if (!less(k, j)){
                break;
            }
            exch(k, j);
            k = j;
        }
    }

    /***************************************************************************
     * Helper functions for compares and swaps.
     ***************************************************************************/
    private boolean less(int i, int j) {
        if (comparator == null) {
            // 若没传比较器，则进行默认比较
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) < 0;
        } else {
            return comparator.compare(pq[i], pq[j]) < 0;
        }
    }

    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    // is pq[1..N] a max heap?
    private boolean isMaxHeap() {
        return isMaxHeap(1);
    }

    /**
     * is subtree of pq[1..N] rooted at k a max heap?
     * 关于pq[1..N]数组其子树所对应根k的位置是否是最大堆
     * */
    private boolean isMaxHeap(int k) {
        if (k > N) return true;
        int left = 2*k, right = 2*k + 1;
        if (left  <= N && less(k, left))  return false;
        if (right <= N && less(k, right)) return false;
        return isMaxHeap(left) && isMaxHeap(right);
    }


    /***************************************************************************
     * Iterator.
     ***************************************************************************/

    /**
     * Returns an iterator that iterates over the keys on this priority queue
     * in descending order.
     * The iterator doesn't implement <tt>remove()</tt> since it's optional.
     *
     * @return an iterator that iterates over the keys in descending order
     */
    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {

        // create a new pq
        private MaxPQ<Key> copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            if (comparator == null) copy = new MaxPQ<Key>(size());
            else                    copy = new MaxPQ<Key>(size(), comparator);
            for (int i = 1; i <= N; i++)
                copy.insert(pq[i]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }
    }

    /**
     * Unit tests the <tt>MaxPQ</tt> data type.
     */
    public static void main(String[] args) {
        MaxPQ<String> pq = new MaxPQ<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
        }
        StdOut.println("(" + pq.size() + " left on pq)");
    }

}