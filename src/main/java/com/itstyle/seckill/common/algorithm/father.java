package com.itstyle.seckill.common.algorithm;

public class father {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
     *
     * 遍历打印
     * */
    public void display(int[] list){
        System.out.println("********排序之后的前"+list.length+"个数start********");
        for (int i:list){
            System.out.print(i+" ");
        }
        System.out.println("");
        System.out.println("********排序之后的前"+list.length+"个数end**********");
    }

    /*
     *
     * 交换数组中两个元素的位置
     * */
    public static void swap(int[] list,int left,int right){
        int temp=list[left];
        list[left]=list[right];
        list[right]=temp;

    }

    public  void Sort(int[] list){}
}
