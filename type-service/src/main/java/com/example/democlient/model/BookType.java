package com.example.democlient.model;

public class BookType {
    public static String TYPEID = "id";
    public static String TYPENAME = "name";

    public String type_id;
    public String type_name;

    public BookType(String id, String name){
        type_id = id;
        type_name = name;
    }
}
