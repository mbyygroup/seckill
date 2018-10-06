package com.itstyle.seckill.common.algorithm;

/*
* 直接选择排序
* */
public class SelectionSort extends father{

    /*
    *所谓选择排序就是每次找到未排序中最小的或者最大的元素位置
    *找到后和未排序序列的第一个元素交换值，知道该序列成为有序序列
    *初始状态整个序列为无序序列，每次交换都使有序序列的长度加一，无序
    * 序列的起始位置后移一位。选择排序的平均时间复杂度为O（n^2），且
    * 选择排序相对不稳定
    * */

    /*
    *
    * 直接选择排序算法
    * */
    public void Sort(int[] list){
        //先定义好排序的名字
        setName("直接选择排序");
        int len=list.length;
        for (int i=0;i<len-1;i++){
            //将当前下表定义为最小值下标
            int min=i;
            for (int j=i+1;j<len-1;j++){
                //如果有小于当前最小值的元素，把它的下标赋值给min
                if (list[j]<list[min]){
                    min=j;
                }
            }
            //如果min不等于i.说明找到了真正的最小值
            if (min!=i){
                swap(list,min,i);
            }
        }
    }


}
