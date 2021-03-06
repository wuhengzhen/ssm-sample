package com.zhen.base.web.mybatisplus;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zhen.base.domain.mybatisplus.User;
import com.zhen.base.service.mybatisplus.MybatisPlusUserService;
import com.zhen.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created with IntelliJ IDEA
 *
 * @ClassName：UserController
 * @Description：mybatis plus - web
 * @Author：wuhengzhen
 * @Date：2019-07-24 17:00
 */
@Controller
@RequestMapping("/mp/user")
public class UserController {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private MybatisPlusUserService mybatisPlusUserService;

    @RequestMapping("/list")
    @ResponseBody
    public String list(Model model) {
        List<User> users = mybatisPlusUserService.findUsers();
        // model.addAttribute("users", users);
        // return "list";
        logger.info("user list to json :{}", JsonUtil.obj2Json(users));
        return "list";
    }

    @RequestMapping("/add")
    public String add(User user) {
        int count = mybatisPlusUserService.addUser(user);
        if (count > 0) {
            // return "forward:/user/list";
            return "insert success!";
        } else {
            // return "ridrect:/user/list";
            return "insert faild!";
        }
    }

    @RequestMapping("/mpList")
    @ResponseBody
    public String mpList() {
        logger.info("mp test start.");
        List<User> userList = mybatisPlusUserService.selectList(new EntityWrapper<>());
        for (User user : userList) {
            System.out.println(user.toString());
        }
        return "mpList";
    }
}
