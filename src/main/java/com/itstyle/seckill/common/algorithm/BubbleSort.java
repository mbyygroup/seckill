package com.itstyle.seckill.common.algorithm;

/*
*
* 冒泡排序
* */
public class BubbleSort extends father{
    /*
    * 冒泡排序，持续比较相邻元素，大的挪到后面，因此大的会逐渐向后挪，故称为冒泡排序
    * 复杂度分析：平均情况和最坏情况均是O（n^2），使用了temp作为
    * 临时交换变量，空间复杂度为O（1）
    * */

    /*
    *
    * 冒泡排序算法
    * */
    public void Sort(int[] list){
        //先定义好排序的名字
        setName("冒泡排序");
        int len=list.length;
        //做多少轮排序（最多length-1轮）
        for (int i=0;i<len-1;i++){
            //每一轮比较多少个
            for (int j=0;j<len-i-1;j++){
                if (list[j]>list[j+1]){
                    //交换排序
                    swap(list,j,j+1);
                }
            }
        }
    }

}
