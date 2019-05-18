package com.nhom6.mediaplayer.activity;

public class SaveData {
    public static boolean flag = false;
    public static int countList;
    public static void setFlag(Boolean check){
        flag  = check;
    }
    public static void setCountList(int count){
        countList  = count;
    }
    public static boolean getFlag() {
        return flag;
    }
    public static int getCountList() {
        return countList;
    }
}
