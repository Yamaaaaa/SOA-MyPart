package com.example.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BrowseHistory implements Serializable {
    public int getUser_id() {
        return user_id;
    }

    public List<BrowseData> getBrowseDataList() {
        return browseDataList;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setBrowseDataList(List<BrowseData> browseDataList) {
        this.browseDataList = browseDataList;
    }

    int user_id;
    List<BrowseData> browseDataList = new ArrayList<>();

    public BrowseHistory(int id){
        user_id = id;
    }

    public int getHistoryNum(){
        return browseDataList.size();
    }
    public void addBrowseData(BrowseData browseData){
        browseDataList.add(browseData);
    }
}
