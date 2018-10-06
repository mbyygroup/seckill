package com.itstyle.seckill.common.algorithm;

/*
* 堆排序
* */
public class HeapSort extends father{
    /*
    * 堆排序的是集合了插入排序的单数组操作，又有归并排序的时间复杂度，完美结合了两者的优点
    *
    * */

    /*
    *
    * 堆排序算法
    * */
    public void Sort(int[] list){
        setName("堆排序算法");
        //将无序堆构造成一个大根堆,大根堆有length/2个父节点
        for (int i=list.length/2 -1;i>-0;i --){
            headAdjust(list,i,list.length);
        }

        //逐步将每个最大值的跟节点和末尾元素交换，并且再调整其为大根堆
        for (int i=list.length-1;i>0;i--){
            //将堆顶节点和当前未经排序的子序列的最后一个元素交换位置
            swap(list,0,i);
            headAdjust(list,0,i);
        }
    }

    /*
    *
    * 构造大堆根
    * */
    private static void headAdjust(int[] list,int parent,int length){
        //保存当前父节点
        int temp=list[parent];

        //得到左孩子节点
        int leftChild=2*parent+1;

        while (leftChild<length){
            //如果parent有右孩子，则要判断左孩子是否小于右孩子
            if (leftChild+1<length&&list[leftChild]<list[leftChild+1]){
                leftChild++;
            }

            //父亲节点大于子节点，就不用做交换
            if (temp>=list[leftChild]){
                break;
            }

            //将较大节点的值赋给父亲节点
            list[parent] =list[leftChild];
            //然后将子节点作为父亲节点
            parent=leftChild;
            //找到该父亲节点较小的左孩子节点
            leftChild=2*parent+1;
        }
        //最后把temp值赋给较大的子节点，以形成两值交换
        list[parent]=temp;
    }
}
