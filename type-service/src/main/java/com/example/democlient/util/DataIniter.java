package com.example.democlient.util;

import com.example.democlient.model.BookIdList;
import com.example.democlient.model.BookSimpleData;
import com.example.democlient.model.BookType;
import com.example.democlient.redisDb.TbService;
import com.google.gson.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

import static com.example.democlient.util.HttpHandler.getUrlContent;

public class DataIniter  implements ApplicationContextAware {
    static String typeUrl = "http://59.110.160.154:8040/api-category//v1/categories";
    static String bookData = "http://59.110.160.154:8040/api-search/v1/book-category/";

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataIniter.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public DataIniter(){
    }
    //初始化type数据
    private void typeParser(String content, Map<Integer, BookIdList> bookIDMap){
        JsonParser parse = new JsonParser();
        try {
            System.out.println("类型信息解析： "+content);
            JsonArray typeArray=(JsonArray) parse.parse(content);  //创建jsonObject对象

            for(int i=0;i<typeArray.size();i++){
                JsonObject typeItem=typeArray.get(i).getAsJsonObject();
                int type_id = typeItem.get(BookType.TYPEID).getAsInt();
                String type_name  = typeItem.get(BookType.TYPENAME).getAsString();
                System.out.println("type_id="+type_id);
                System.out.println("type_name="+type_name);

                bookIDMap.put(type_id, new BookIdList(new BookType(""+type_id, type_name)));
            }

        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    //将数据存储到bookIdList中
    static private void bookIDParse(String content, BookIdList bookIdList){
        JsonParser parse = new JsonParser();
        try {
            JsonArray typeArray=(JsonArray) parse.parse(content);  //创建jsonObject对象

            for(int i=0;i<typeArray.size();i++){
                JsonObject bookIDItem=typeArray.get(i).getAsJsonObject();
                int book_id = bookIDItem.get(BookSimpleData.BOOKID).getAsInt();

                System.out.println("book_id="+book_id);

                bookIdList.addBookID(book_id);
            }

        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void initTypeBookData(){
        Map<Integer, BookIdList> bookIDMap = new HashMap<>();
        //获取type_id与book_id数据
        //将数据转化为字符串并存储到数据库中
        try {
            String typeContent = getUrlContent(typeUrl);
            typeParser(typeContent, bookIDMap);
            Gson gson = new Gson();
            for (Map.Entry<Integer, BookIdList> entry : bookIDMap.entrySet()) {
                String url = bookData + entry.getKey();
                String idContent = getUrlContent(url);
                bookIDParse(idContent, entry.getValue());
                //String data = gson.toJson(entry.getValue()).toString();
                //System.out.println("将数据转化为json字符串："+data);
                entry.getValue().setBookNum();
                System.out.println("id = "+ entry.getKey() +"储存书籍书目: "+ entry.getValue().getBookNum());
                        //
                TbService.tbService.add(""+entry.getKey(), entry.getValue());

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
