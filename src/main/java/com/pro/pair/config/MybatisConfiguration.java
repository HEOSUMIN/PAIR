package com.pro.pair.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.pro.pair", annotationClass = Mapper.class)
public class MybatisConfiguration {
	

}