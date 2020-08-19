package com.lhb.config;

import com.lhb.selector.CustomerImportSelector;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: yaya
 * @Description:
 * @Date: Create in 上午 10:47:58 2020/8/14
 */
@Configuration
//@ComponentScan("com.lhb")
@Import(CustomerImportSelector.class)
public class SpringConfiguration {
}
