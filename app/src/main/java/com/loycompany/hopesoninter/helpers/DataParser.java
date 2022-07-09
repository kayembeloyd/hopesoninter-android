package com.loycompany.hopesoninter.helpers;


import java.util.ArrayList;
import java.util.List;

public class DataParser {
    public static List<String> javaexplode(String delimiter, String content){
        int _lastpos = 0;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < content.length(); i++){
            if(content.substring(i, i + 1).equals(delimiter)){
                list.add(content.substring(_lastpos , i));
                _lastpos = i + 1;
            }
        }

        list.add(content.substring(_lastpos, content.length()));
        return list;
    }

    // Not tested
    public static String javaimpload(List<String> list, String delimeter){
        String _appendto  = "";
        for (int i = 0; i < list.size(); i++){
            if (_appendto.equals("")) {
                _appendto = list.get(i);
            } else {
                _appendto = _appendto + delimeter + list.get(i);
            }
        }
        return _appendto;
    }
}