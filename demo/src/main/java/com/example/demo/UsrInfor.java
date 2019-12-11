package com.example.demo;

import java.util.List;

public class UsrInfor {
    int usr_id;
    String name;
    String avatar;
    String locList;
    List<Integer> orderList;
    List<BrowseData> browseDataList;

    public int getUsr_id() {
        return usr_id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLocList() {
        return locList;
    }

    public List<Integer> getOrderList() {
        return orderList;
    }

    public List<BrowseData> getBrowseDataList() {
        return browseDataList;
    }

    public UsrInfor(SystemUser systemUser, List<BrowseData> bdl, List<Integer> ol, String lli){
        usr_id = systemUser.getUsr_id(); name = systemUser.getName(); avatar = systemUser.getAvatar();
        locList = lli;
        orderList = ol;
        browseDataList = bdl;
    }
}
