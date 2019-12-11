package com.example.democlient.model;

import java.util.ArrayList;
import java.util.List;

public class BookDataList {
    public BookType bookType;
    public int bookNum;
    public List<BookSimpleData> bookDataList = new ArrayList<>();

    public BookDataList(BookType bookType){
        this.bookType = bookType;
    }

    public void addBook(BookSimpleData data){
        bookDataList.add(data);
    }

    public void setBookNum(){
        bookNum = bookDataList.size();
    }

    public String getTypeID(){
        return bookType.type_id;
    }

    public int getBookNum(){
        return bookDataList.size();
    }

    public void setBookDataList(List<BookSimpleData> bookDataList){
        this.bookDataList = bookDataList;
        setBookNum();
    }

    public List<BookSimpleData> getBookDataList(){
        return bookDataList;
    }
}
