package com.itstyle.seckill.common.algorithm;

/*
* 希尔排序
* */
public class ShellSort extends father{
    //希尔排序是快速，易于理解和实现的，但是复杂度分析比较复杂

    /*
    *
    * 希尔排序
    * */
    public void Sort(int[] list){
        setName("希尔排序");
        int len=list.length;
        //取增量
        int gap=len/2;

        while (gap>=1){
            //无序序列
            for (int i=gap;i<len;i++){
                int temp=list[i];
                int j;

                //有序序列
                for (j=i-gap;j>=0&&list[j]>temp;j=j-gap){
                    list[j+gap]=list[j];
                }
                list[j+gap]=temp;
            }

            //缩小增量
            gap=gap/2;
        }
    }
}
