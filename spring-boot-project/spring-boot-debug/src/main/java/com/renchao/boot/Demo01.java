package com.renchao.boot;

import com.renchao.boot.service.MyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author ren_chao
 * @since 2024-08-15
 */
@SpringBootApplication
public class Demo01 {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Demo01.class, args);
		MyService bean = context.getBean(MyService.class);
		System.out.println("=================::" + bean.getName());
	}
}
