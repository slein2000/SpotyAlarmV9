package com.skiz.spoalarm.spotyalarmv9;

public class StringUtility {

    public int count(String search,String input){
        int lenght = input.length();
        int replaced =  input.replaceAll(search,"").length();
        return (lenght - replaced);
    }

}
