package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;

@SpringBootApplication
@EnableEurekaServer
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        //test();
    }

    public static void test(){
        Gson gson = new Gson();
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "tb_1");
        BrowseHistory browseHistory = new BrowseHistory(1);
        //Date date = new Date("Dec 11, 2019, 8:50:35 PM");
        browseHistory.addBrowseData(new BrowseData(1, "pictureUrl", "book_name", "10.0", new Date()));

        Usr_Loc usr_loc = new Usr_Loc(1);
        usr_loc.addLoc(new LocInfor("receiver", "phone", "province", "city", "area", "town", "loc"));

        OrderHistory orderHistory = new OrderHistory(4);
        orderHistory.addOrder(6);
        orderHistory.addOrder(7);
        orderHistory.addOrder(8);
        System.out.println(gson.toJson(orderHistory));
    }

}
