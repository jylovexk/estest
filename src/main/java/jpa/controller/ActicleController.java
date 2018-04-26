package jpa.controller;

import jpa.model.Article;
import jpa.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zjf on 2018/4/23.
 */
@SpringBootApplication
@RestController
@ComponentScan(basePackages = {"jpa.service","es"})
@EnableJpaRepositories(basePackages = "jpa.dao")
@EntityScan(basePackages = "jpa.model")
public class ActicleController {
    @Autowired
    private ArticleService articleService;
    @RequestMapping("/list")
    public List<Article> list(){
        List<Article> list =  articleService.findAll();
        System.out.println("###"+list);
        return list;
    }

    public static void main(String[] args) {
        SpringApplication.run(ActicleController.class,args);
    }
}
