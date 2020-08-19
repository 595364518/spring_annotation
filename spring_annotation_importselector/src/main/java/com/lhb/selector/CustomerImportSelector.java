package com.lhb.selector;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @Author: yaya
 * @Description: 自定义导入器
 * @Date: Create in 上午 10:48:46 2020/8/14
 */
public class CustomerImportSelector implements ImportSelector {

    //AspectJ表达式
    private String expression;

    //不加ComponentScan时配置文件中自定义的包名
    private String customerPackages;

    public CustomerImportSelector(){

        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("customerimport.properties");

            this.expression = properties.getProperty("customer.import.expression");

            this.customerPackages = properties.getProperty("customer.import.package");

            System.out.println(this.expression);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String[] selectImports(AnnotationMetadata annotationMetadata) {

        List<String> basePackages = null;


        //判断是否加了@ComponentScan注解
        if(annotationMetadata.hasAnnotation(ComponentScan.class.getName())){
            //获取注解的所有属性
            Map<String, Object> attr = annotationMetadata.getAnnotationAttributes(ComponentScan.class.getName());

            //直接使用ComponentScan中指定的路径
            basePackages = new ArrayList<>(Arrays.asList((String[])attr.get("basePackages")));
        }

        //没有加@ComponentScan注解则为null；有注解，没有指定basePages则长度为0
        if(basePackages == null || basePackages.size() == 0){
            String basePackage = null;

            try {
                //取出@Import注解所在的包名称
                basePackage = Class.forName(annotationMetadata.getClassName()).getPackage().getName();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            basePackages = new ArrayList<>();

            basePackages.add(basePackage);

        }

        //判断是否自定义包名
        if(!StringUtils.isEmpty(customerPackages)){
            basePackages.add(customerPackages);
        }

        //创建类路径扫描器  不使用默认过滤器
        ClassPathScanningCandidateComponentProvider
                scanner = new ClassPathScanningCandidateComponentProvider(false);

        //创建Aspectj表达式类型过滤器
        TypeFilter typeFilter = new AspectJTypeFilter(expression,CustomerImportSelector.class.getClassLoader());

        scanner.addIncludeFilter(typeFilter);

        //定于需要扫描的类全限定名集合
        final Set<String> classes = new HashSet<String>();

        for (String basePackage : basePackages) {
            scanner.findCandidateComponents(basePackage).forEach(
                    beanDefinition -> classes.add(beanDefinition.getBeanClassName())
            );
        }

        return classes.toArray(new String[classes.size()]);

    }
}
