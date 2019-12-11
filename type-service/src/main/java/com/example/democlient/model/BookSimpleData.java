package com.example.democlient.model;

public class BookSimpleData {
    public static String BOOKID = "id";
    public static String PICTURE = "picurl";
    public static String BOOKNAME  = "name";
    public static String PIRCE = "price";

    public int book_id;
    public String picture;
    public String book_name;
    public String price;

    public BookSimpleData(int id, String picture, String book_name, String price){
        book_id = id;
        this.picture = picture;
        this.book_name = book_name;
        this.price = price;
    }
}
