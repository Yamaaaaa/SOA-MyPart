package com.example.democlient.util;

import com.example.democlient.model.BookDataList;
import com.example.democlient.model.BookIdList;
import com.example.democlient.model.BookSimpleData;
import com.example.democlient.model.BookType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

import static com.example.democlient.redisDb.BdService.bdService;
import static com.example.democlient.redisDb.TbService.tbService;

public class DataGetter implements Runnable{
    static int bookDataNum = 30;
    String bookDataUrl = "http://59.110.160.154:8081/v1/book-simple-data";

    public DataGetter(){}

    @Override
    public void run(){
        Gson gson = new Gson();
        //根据类别分组的bookIDList
        List<BookIdList> list = new ArrayList<>();
        //所有需要查询信息的bookIDList
        Map<String, BookDataList> idDataMap = new HashMap<>();
        Map<Integer, BookDataList> bookIDDataMap = new HashMap<>();
        String result = null;
        //int count = 0;
        try {
            System.out.println("获取所有typeId: "+ tbService.getKeys());
            for (String type_id : tbService.getKeys()) {
                list.add(getDataIDList(type_id.substring(3,4), idDataMap, bookIDDataMap));
            }
            System.out.println("发送书籍信息查询请求: "+gson.toJson(bookIDDataMap.keySet()));
            System.out.println("请求数目: "+bookIDDataMap.keySet().size());
            result = HttpHandler.doPost(bookDataUrl, gson.toJson(bookIDDataMap.keySet()));
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("查询结果: "+ result);
        Type dataListType = new TypeToken<ArrayList<BookSimpleData>>(){}.getType();
        List<BookSimpleData> bsd = gson.fromJson(result, dataListType);
        //BookSimpleData simpleData1 = bsd.get(0);
        //System.out.println("第一项："+simpleData1.book_name);
        for(BookSimpleData simpleData: bsd){
            System.out.println("书名："+simpleData.book_name);
            Integer key = simpleData.book_id;
            try {
                System.out.println("entity: "+gson.toJson(bookIDDataMap.get(key)));
                bookIDDataMap.get(key).addBook(simpleData);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        for(BookDataList bookDataList: idDataMap.values()){
            bdService.delete(bookDataList.getTypeID());
            bdService.add(bookDataList.getTypeID(), bookDataList);
        }
    }

    //存储书籍基本信息

    //根据type_id获取随机书籍id，并向api查询数据
    //idDataMap记录类型对应的BookDataList
    //bookIDData记录书籍在哪个BookDataList之中
    public BookIdList getDataIDList(String type_id, Map<String, BookDataList> idDataMap, Map<Integer, BookDataList> bookIDDataMap) throws Exception {
        List<Integer> bookIDList = new ArrayList<>();
        BookIdList bookIdList = tbService.get(type_id);
        //每中类型对应一个DataList
        idDataMap.put(type_id, new BookDataList(new BookType(bookIdList.getTypeID(), bookIdList.getTypeName())));
        if(bookIdList.getBookNum()< bookDataNum){
            bookIDList = bookIdList.getBookIDList();
        } else {
            Random random = new Random();
            HashSet<Integer> IDNumber = new HashSet<>();
            for (int i=0; i<bookDataNum; ++i){
                IDNumber.add(random.nextInt(bookIdList.getBookNum()));
            }

            for(Integer i: IDNumber){
                bookIDList.add(bookIdList.getBookIDList().get(i));
                //有问题
                bookIDDataMap.put(bookIdList.getBookIDList().get(i), idDataMap.get(type_id));
            }
        }
        bookIdList.setBookIDList(bookIDList);
        return bookIdList;
    }
}
