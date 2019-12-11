package com.example.democlient;

import com.example.democlient.model.BookIdList;
import com.example.democlient.redisDb.BdService;
import com.example.democlient.redisDb.TbService;
import com.example.democlient.util.DataGetter;
import com.example.democlient.util.DataIniter;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class DemoclientApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(DemoclientApplication.class, args);
        System.out.println("初始化数据");
        //DataIniter di = new DataIniter();
        //di.setApplicationContext(ctx);
        //di.initTypeBookData();

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        DataGetter dataGetter = new DataGetter();
        //dataGetter.test();
        //第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(dataGetter, 0, 60, TimeUnit.MINUTES);
    }

    @Autowired
    TbService tbService;

    @Value("${server.port}")
    String port;

    @GetMapping("/v1/type-book-data")
    public String typebookData(@RequestParam(value="type", defaultValue = "all") String key){
        String result = "";
        Gson gson = new Gson();
        if(key.equals("all")){
            System.out.println("查询所有类别");
            result = gson.toJson(BdService.bdService.getAll());
        }else{
            System.out.println("查询"+key);
            result = gson.toJson(BdService.bdService.getBookData(key));
        }
        System.out.println("书籍信息返回结果： "+result);
        return result;
    }

    @PostMapping("/v1/type-data")
    public void addBook(@RequestBody Map map){
        String type_id = (String)map.get("type_id");
        List<Integer> idList = (List<Integer>) map.get("IDList");
        tbService.add(type_id, idList);
    }

    @DeleteMapping("/v1/type-data")
    public void deleteBook(@RequestBody HashMap<Integer, String> map){
        TbService.tbService.delete(map);
    }

    @RequestMapping("/test1")
    public String test(@RequestParam(value="key", defaultValue = "type") String key){
        return tbService.test();
    }

    @RequestMapping("/test2")
    public String test2(@RequestParam(value="key", defaultValue = "type") String key){
        return TbService.test2();
    }
}
