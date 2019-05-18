package com.nhom6.mediaplayer.activity;

public class SaveData {
    public static boolean flag = false;
    public static int countList;
    public static int songIDNowPlay;


    public static void setFlag(Boolean check){
        flag  = check;
    }
    public static void setCountList(int count){
        countList  = count;
    }
    public static void setSongId(int songId){
        songIDNowPlay  = songId;
    }

    public static boolean getFlag() {
        return flag;
    }
    public static int getCountList() {
        return countList;
    }
    public static int getSongIDNowPlay() {
        return songIDNowPlay;
    }
}
