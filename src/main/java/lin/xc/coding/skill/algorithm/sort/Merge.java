package lin.xc.coding.skill.algorithm.sort;

/**
 * 归并排序
 * @author lin.xc
 * @date 2021/5/18
 **/
public class Merge {

    // 归并所需的辅助数组
    private static Comparable[] aux;

    /**
     * “自顶向下”的归并排序
     * */
    public static void sort(Comparable[] a){
        aux = new Comparable[a.length];
        sort(a, 0, a.length-1);
    }

    /**
     * 递归方法
     * 不停的分成左右两边，直到分不动了，最底层形态，左右各一个数，即左右肯定都是按顺序排序的
     * 利用递归自顶向下去进行归并排序
     *
     * 基于原地归并的抽象实现了另一种递归归并，这也是应用高效算法设计中分治思想的最典型的一个例子。
     * 这段递归代码是归纳证明算法能够正确地将数组排序的基础：如果它能将两个子数组排序，它就能够通过归并两个子数组来将整个数组排序。
     * */
    private static void sort(Comparable[] a, int lo, int hi){
        // 将数组a[lo...hi]排序
        if(hi <= lo) return;
        // 计算得出中间点
        int mid = lo + (hi - lo)/2;
        // 将左半边排序
        sort(a, lo, mid);
        // 将右半边排序
        sort(a, mid+1, hi);
        // 归并结果
        merge(a, lo, mid, hi);
    }

    /**
     * “自底向上”的归并排序
     * 从最相邻的两个数开始比较归并比较
     * */
    public static void sortBU(Comparable[] array){
        // 进行lgN次两两归并
        int N = array.length;
        // “辅助数组”的大小和“比较数组”的大小一致
        aux = new Comparable[N];
        for(int sz=1; sz<N; sz+=sz){
            // sz子数组大小 sz+=sz sz=sz+sz
            System.out.println("sz="+sz);
            int mid, hi;
            for(int lo=0; lo<N-sz; lo+=sz+sz){
                mid = lo+sz-1;
                hi = Math.min(lo+sz+sz-1, N-1);
                System.out.println(String.format("lo=%d mid=%d hi=%d", lo, mid, hi));
                // lo：子数组索引 lo=lo+sz+sz
                merge(array, lo, mid, hi);
            }
        }
        /**
         * 每一块区域元素划分个数 sz=1
         * lo=0 mid=0 hi=1  ----- [0,1]
         * lo=2 mid=2 hi=3  ----- [2,3]
         * lo=4 mid=4 hi=5  ----- [4,5]
         * lo=6 mid=6 hi=7  ----- [6,7]
         * lo=8 mid=8 hi=9  ----- [8,9]
         * lo=10 mid=10 hi=11  ----- [10,11]
         * lo=12 mid=12 hi=13  ----- [12,13]
         * lo=14 mid=14 hi=15  ----- [14,15]
         * 每一块区域元素划分个数 sz=2
         * lo=0 mid=1 hi=3  ----- [(0,1),(2,3)]
         * lo=4 mid=5 hi=7  ----- [(4,5),(6,7)]
         * lo=8 mid=9 hi=11  ----- [(8,9),(10,11)]
         * lo=12 mid=13 hi=15  ----- [(12,13),(14,15)]
         * 每一块区域元素划分个数 sz=4
         * lo=0 mid=3 hi=7  ----- [(0,1,2,3),(4,5,6,7)]
         * lo=8 mid=11 hi=15  ----- [(8,9,10,11),(12,13,14,15)]
         * 每一块区域元素划分个数 sz=8
         * lo=0 mid=7 hi=15  ----- [(0,1,2,3,4,5,6,7),(8,9,10,11,12,13,14,15)]
         * 每一块区域元素划分个数 sz=16
         * lo=0 mid=15 hi=16  ----- [(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15),(16)]
         * sz由1,2,4,8,16变化
         * */
    }

    /**
     * 原地归并方法
     * 【注意】：该方法主要是将两个已经分别排好序的数组归并成整个有序的数组，颗粒度最细的时候，左右都是一个元素。
     * 此时为了不影响原数组，且左块和右块的内容不会混乱变动。需要引入辅助数组，将块1和块2逐个向右比较元素大小，小的元素先放入辅助数组，小的块元素继续向右移动。
     * 需要保证a[lo ~ mid]和a[mid ~ hi]都是有序的，这样每一半向右移动（即+1）就是在增大，所以最好从细颗粒到粗颗粒，能得到保证
     */
    public static void merge(Comparable[] a, int lo, int mid, int hi){
        // 将a[lo..mid]和a[mid+1..hi]归并
        // 将数组分为a[lo~mid]和a[mid+1~hi]，他们的首个元素下标分别为lo和mid+1，将这两个值分别赋给idx_0和idx_1
        int idx_0 = lo, idx_1 = mid+1;

        // 将a[lo..hi]复制到aux[lo..hi]，辅助数组，去复制a数组中所有需要用到的元素
        for(int k=lo; k<=hi; k++){
            aux[k] = a[k];
        }

        // aux初始点和切点开始移动，分别为idx_0和idx_1
        // 归并回到a[lo..hi]  请按下面数字标示的顺序进行阅读，更益于理解
        for(int k=lo; k<=hi; k++){
            if(idx_0>mid){
                // 3-当aux的idx_0光标>=切口值，说明左边一半都比较完了，此时idx_1光标所对应位置的值，就是当前a[遍历值]该对应的值
                // 因为按1和2的说明，都是谁小谁往后移一位再做比较，所以此时左边都走完了，说明停在idx_1的这个值就是对应的当前a[遍历值]该有的值
                a[k] = aux[idx_1++];
            }else if(idx_1>hi){
                // 4-当aux的idx_1光标>=结尾值，说明右边一半都比较完了，此时idx_0光标所对应位置的值，就是当前a[遍历值]该对应的值
                // 参照3，其实这种时候3和4任一一种比较完都不会再走循环了，因为光标一半移动完，剩下的一半本身就是有序的，无需再进行比较了，肯定后面的值都比另一半大
                a[k] = aux[idx_0++];
            }else if(less(aux[idx_1], aux[idx_0])){
                // 1-若aux[idx_1]比aux[idx_0]小，则将较小的aux[idx_1]给到a[遍历值]，且idx_1 +1，即切面值往后移动
                // 此时认为aux左边的大概率会大于右边的，所以右边的光标移动，进行下一次判断【注意】！这里指的是aux
                a[k] = aux[idx_1++];
            }else{
                // 2-该情况为aux[idx_0]比aux[idx_1]大，则将较小的aux[idx_0]的值给a[遍历值]，且idx_0 +1，即往后移动
                // 此时认为aux右边的大概率会大于左边的，所以左边的光标移动，进行下一次判断
                a[k] = aux[idx_0++];
            }
        }
    }

    /**
     * 是否小于方法：对可比较的元素进行比较
     * */
    public static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /**
     * 交换方法
     * */
    public static void exchange(Comparable[] arr, int i, int j) {
        Comparable temp=arr[i]; arr[i]=arr[j]; arr[j]=temp;
    }

    /**
     * 展示
     * */
    public static void show(Comparable[] arr) {
        for(int i=0; i<arr.length; i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println();
    }

    /**
     * 测试元素是否有序
     * */
    public static boolean isSorted(Comparable[] arr) {
        for(int i=1; i<arr.length; i++){
            // 若后一个数比前一个数小，则非有序数组
            if(less(arr[i], arr[i-1])){
                return false;
            }
        }
        // 都是后一个数比前一个数大，则返回true
        return true;
    }

    public static void main(String[] args) {
//        String[] a = new String[]{"L","E","E","A","M","H","L","E","T","S","O","L","P","S","X","R"};
//        String[] a = new String[]{"12","11","10","9","8","7","6","5","4","3","2","1","16","15","14","13"};
//        String[] a = new String[]{"16","15","14","13","12","11","10","09","08","07","06","05","04","02","01","03","17"};
        // 需保证切点的两端都是有排序的
        String[] a = new String[]{"03","03","04","07","09","11","12","16","01","05","06","06","08","11","12","13","17"};
        show(a);
        sortBU(a);
        System.out.println("排序操作后：");
        show(a);
        System.out.println("是否已排序？"+isSorted(a));
        int lo=5,sz=2;
        lo+=sz+sz;
        System.out.println(lo);
    }

    /**
     * 后记：
     * */

}
