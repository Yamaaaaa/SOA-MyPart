package com.example.usrservice.controller;

import com.example.usrservice.database.RedisService;
import com.example.usrservice.database.UserDao;
import com.example.usrservice.entry.*;
import com.example.usrservice.util.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@RestController
public class UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    RedisService redisService;

    @RequestMapping(path = "/signin", method = RequestMethod.POST)
    public boolean signin(@RequestBody Map params) throws Exception {
        String name = (String) params.get("name");
        String password = (String) params.get("password");
        SystemUser systemUser = userDao.findByName(name);
        if(systemUser != null){
            return false;
        }
        password = MyPasswordEncoder.getEncoder().encode(password);
        userDao.save(new SystemUser(name, password, "user"));
        return true;
    }

    @PostMapping("/v1/LocInfor")
    public void updateLocInfor(@RequestBody Usr_Loc usr_loc){
        redisService.updateLoc(usr_loc);
    }

    @PostMapping("/v1/BrowseHistory")
    public void addBrowseHistory(@RequestBody BrowseHistory browseHistory){
        redisService.addBrowseHistory(browseHistory);
    }

    @PostMapping("/v1/OrderHistory")
    public void addOrderHistory(@RequestBody OrderHistory orderHistory){
        redisService.addOrderHistory(orderHistory);
    }

    @GetMapping("/UserInfor")
    @ResponseBody
    public UsrInfor UserInfor(Authentication authentication){
        String name = authentication.getName();
        System.out.println("usr name = " + name);
        SystemUser systemUser = userDao.findByName(name);
        int id = systemUser.getUsr_id();
        return new UsrInfor(systemUser, redisService.getBrowseList(id), redisService.getOrderList(id), redisService.getLoc(id));
    }


    // 当前用户信息
    @GetMapping("/info")
    @ResponseBody
    public Object getCurrentUser(Authentication authentication) {
        return authentication;
    }

    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    @PostMapping("/home2")
    public String home2(){
        return "home";
    }

    @RequestMapping("/v1/welcome")
    public String welcome(){
        return "welcome";
    }

    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }

    @RequestMapping("/user")
    public String user(){
        return "user";
    }
}
