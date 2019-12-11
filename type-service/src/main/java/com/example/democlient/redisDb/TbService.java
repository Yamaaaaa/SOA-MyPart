package com.example.democlient.redisDb;

import com.example.democlient.model.BookID;
import com.example.democlient.model.BookIdList;
import com.example.democlient.model.BookType;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class TbService {

    public static TbService tbService;

    @PostConstruct
    public void init(){
        tbService = this;
        tbService.redisTemplate = this.redisTemplate;
        TYPE_BOOK = "tb_";
    }
    public static String TYPE_BOOK;
    public static String TYPE_DATA = "ty_";
    @Autowired
    RedisTemplate redisTemplate;

    //用于初始化数据
    public void add(String type_id, BookIdList bookIdList) {
        Gson gson = new Gson();
        String value = gson.toJson(bookIdList);
        System.out.println("Redis Oper, add value: "+value);

        redisTemplate.opsForValue().set(TYPE_DATA+type_id, bookIdList.getTypeName());
        redisTemplate.opsForList().leftPushAll(TYPE_BOOK+type_id, bookIdList.getBookIDList());
        System.out.println("数据数量： "+redisTemplate.opsForList().size(TYPE_BOOK+type_id));
    }

    public void add(String type_id, List<Integer> IDList){
        System.out.println("添加type:"+type_id+" idList:"+IDList);
        try {
            redisTemplate.opsForList().leftPushAll(type_id, IDList);
            //Integer lala = 3000;
            //redisTemplate.opsForList().remove(type_id, 4, lala);
            //redisTemplate.opsForList().remove(type_id, 0, "IDList");
            System.out.println("全部数据： "+redisTemplate.opsForList().range(type_id, 0, -1));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void delete(String type_id, Integer book_id){
        System.out.println("删除 type: "+type_id+" 的书籍："+book_id);
        redisTemplate.opsForList().remove(type_id, 0, book_id);
        //System.out.println("全部数据： "+redisTemplate.opsForList().range(type_id, 0, -1));
    }

    public void delete(Map<Integer,String> map){
        for(Map.Entry<Integer, String> item: map.entrySet()){
            delete(item.getValue(), item.getKey());
        }
    }

    public BookIdList get(String type_id){
        String type_name = (String)redisTemplate.opsForValue().get(TYPE_DATA+type_id);

        List<Integer> IDList = redisTemplate.opsForList().range(TYPE_BOOK+type_id, 0, -1);
        BookIdList bil = new BookIdList(new BookType(type_id, type_name));
        bil.setBookIDList(IDList);
        return bil;
    }

    public Set<String> getKeys(){
        return redisTemplate.keys(TYPE_BOOK+"*");
    }



    public String test(){
        redisTemplate.opsForValue().set("type", "dididi");
        return (String) redisTemplate.opsForValue().get("type");
    }

    public static String test2(){
        tbService.redisTemplate.opsForValue().set("type", "test2");
        return (String) tbService.redisTemplate.opsForValue().get("type");
    }
}