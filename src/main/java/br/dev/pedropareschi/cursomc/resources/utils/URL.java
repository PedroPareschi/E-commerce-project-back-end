package br.dev.pedropareschi.cursomc.resources.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class URL {

    public static String decodeParam(String s){
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8);
        }catch (RuntimeException e){
            return "";
        }
    }

    public static List<Integer> decodeIntList(String s){
        String[] vet = s.split(",");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < vet.length; i++){
            list.add(Integer.parseInt(vet[i]));
        }
        return list;
    }
}
