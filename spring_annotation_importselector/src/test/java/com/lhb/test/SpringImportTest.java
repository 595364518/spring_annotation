package com.lhb.test;

import com.lhb.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: yaya
 * @Description:
 * @Date: Create in 上午 11:18:47 2020/8/14
 */
public class SpringImportTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext("com.lhb.config");

        UserService bean = ac.getBean("com.lhb.service.impl.UserServiceImpl", UserService.class);

        bean.save();
    }
}
