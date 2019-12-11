package com.example.democlient.redisDb;

import com.example.democlient.model.BookDataList;
import com.example.democlient.model.BookIdList;
import com.example.democlient.model.BookSimpleData;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class BdService {

    public static BdService bdService;
    public static String BOOK_DATA;
    static int showBookNum = 10;

    @PostConstruct
    public void init(){
        bdService = this;
        bdService.stringRedisTemplate = this.stringRedisTemplate;
        BOOK_DATA = "bd_";
    }

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void add(String key, BookDataList bookDataList) {
        Gson gson = new Gson();
        String value = gson.toJson(bookDataList);
        System.out.println("Redis BookData Operation, add key:" + key+ "value: "+value);
//        if(!key.substring(0,3).equals(BOOK_DATA)){
//            key = BOOK_DATA + key;
//        }
        stringRedisTemplate.opsForValue().set(BOOK_DATA + key, value);
    }

    public void delete(String key){
            stringRedisTemplate.opsForValue().getOperations().delete(BOOK_DATA+key);
    }

    //key为typeID,以bd_开头
    public BookDataList get(String key){
        Gson gson = new Gson();
        return gson.fromJson(stringRedisTemplate.opsForValue().get(key), BookDataList.class);
    }

    //key为typeID,以bd_开头
    public List<BookDataList> getAll(){
        List<BookDataList> list = new ArrayList<>();
        for(String key: getKeys()){
            list.add(getBookData(key));
        }
        return list;
    }

    public Set<String> getKeys(){
        return stringRedisTemplate.keys(BOOK_DATA+"*");
    }

    //根据type_id获取随机书籍信息，并返回
    public BookDataList getBookData(String key){
        BookDataList bookDataList = bdService.get(key);
        List<BookSimpleData> simpleDataList = new ArrayList<>();
        System.out.println("已有书籍书目" + bookDataList.getBookNum());
        if(bookDataList.getBookNum()> showBookNum){
            Random random = new Random();
            HashSet<Integer> IDNumber = new HashSet<>();
            for (int i = 0; i < showBookNum; ++i) {
                IDNumber.add(random.nextInt(bookDataList.getBookNum()));
            }
            for (Integer i : IDNumber) {
                simpleDataList.add(bookDataList.getBookDataList().get(i));
            }
            bookDataList.setBookDataList(simpleDataList);
        }

        return bookDataList;
    }
}
