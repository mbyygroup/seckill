package com.itstyle.seckill.common.algorithm;

/*
* 快速排序
* */
public class QuickSort extends father{
    /*
    * 通过一趟排序把将要排序的数组分割为独立的两部分，其中一部分的所有数据
    * 都比另外一部分的所有数据小然后再按照此方法对两部分数据分别进行快速排
    * 序,整个排序过程可以递归进行以此达到整个数据变成有序序列
    *
    *
    * */

    public void Sort(int[] list){
        //先定义好排序的名字
        setName("快速排序");
        quickSort(list,0,list.length-1);
    }

    /*
    *
    * 快速排序算法
    * */
    private void quickSort(int[] list,int left,int right){
        if (left<right){
            //分割数组，找到分割点
            int point=partition(list,left,right);

            //递归调用，对左边子数组进行快读排序
            quickSort(list,left,point-1);
            //递归调用，对右边子数组进行快速排序
            quickSort(list,point+1,right);
        }
    }

    /*
    *
    * 分割数组，找到分割点
    * */
    private int partition(int[] list,int left,int right){
        //用数组的第一个元素作为基准数
        int first=list[left];
        while (left<right){
            while (left<right&&list[right]>first){
                right--;
            }
            //交换
            swap(list,left,right);

            while (left<right&&list[left]<=first){
                left++;
            }
            //交换
            swap(list,left,right);
        }
        //返回分割点所在的位置
        return left;
    }


}
