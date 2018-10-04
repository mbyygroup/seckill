package com.itstyle.seckill.common.algorithm;

/*
* 排序算法 vs
* */
public class AlgorithmVs {
    public static void main(String[] args) {
        testQuick();
    }
    /*
    * 测试快速排序耗费的时间
    * */
    public static void testQuick(){
        int[] list=new int[10000];
        for (int i=0;i<10000;i++){
            list[i] = (int) (Math.random()*100000);
        }

        //快速排序
        long start=System.currentTimeMillis();
        QuickSort.quickSort(list,0,list.length-1);
        long end=System.currentTimeMillis();
        System.out.println("快速排序耗费的时间:"+(end-start));
        display(list);
    }

    /**
     * 遍历打印前10个数
     */
    public static void display(int[] list) {
        System.out.println("********排序之后的前10个数start********");
        if (list != null && list.length > 0) {
            for (int i = 0; i < 10; i++) {
                System.out.print(list[i] + " ");
            }
            System.out.println("");
        }
        System.out.println("********排序之后的前10个数end**********");
        System.out.println("");
    }
}
