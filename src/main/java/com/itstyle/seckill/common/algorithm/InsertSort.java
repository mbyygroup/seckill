package com.itstyle.seckill.common.algorithm;

public class InsertSort extends father{
    /*
    * 直接插入排序是一种最简单的排序方法，它的基本操作是将一个记录插入已排序好的有序的表中
    * 从而得到一个新的，记录数加一的有序表。当前元素的前面元素皆为有序，要插入时，从当前元
    * 素的左边开始往前找（从后往前找），比当前元素大的均往右移一个位置，最后把当前于元素放
    * 在它应该呆的地方就行了
    * */
    public void Sort(int[] list){
        setName("直接插入排序");
        int len=list.length;
        //从无序列表中取出第一个元素，无需序列是从第二个元素开始的
        for (int i=1;i<len;i++){
            int temp=list[i];
            int j;
            //遍历有序序列
            //如果有序序列中的元素比临时元素大，则将有序序列中比临时元素大的元素依次后移
            for (j=i-1;j>=0&&list[j]>temp;j--){
                list[j+1]=list[j];
            }
            //将临时元素插入到腾出的位置上
            list[j+1]=temp;
        }
    }

}
