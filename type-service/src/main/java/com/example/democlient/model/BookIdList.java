package com.example.democlient.model;

import java.util.ArrayList;
import java.util.List;

public class BookIdList {
    private BookType bookType;
    private int bookNum;
    private List<Integer> bookIDList = new ArrayList<>();

    public BookIdList(BookType bookType){
        this.bookType = bookType;
    }

    public void addBookID(int id){
        bookIDList.add(id);
    }

    public void setBookNum(){
        bookNum = bookIDList.size();
    }

    public void setBookIDList(List<Integer> idList){
        this.bookIDList = idList;
        setBookNum();
    }

    public String getTypeID(){
        return bookType.type_id;
    }

    public String getTypeName(){
        return bookType.type_name;
    }

    public List<Integer> getBookIDList(){
        return bookIDList;
    }

    public int getBookNum() {
        return bookIDList.size();
    }
}
