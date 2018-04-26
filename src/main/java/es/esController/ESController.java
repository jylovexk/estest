package es.esController;

import es.elasticSearch.ESDao;
import es.elasticSearch.ESPagination;
import es.elasticSearch.OperInfo;
import es.elasticSearch.OperType;
import es.modelDoc.ArticleDoc;
import jpa.model.Article;
import jpa.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjf on 2018/4/24.
 */
@RestController
@EnableAutoConfiguration
@ComponentScan(basePackages = {"es","jpa"})
public class ESController {
    @Autowired
    private ESDao<ArticleDoc> esDao;
    @Autowired
    private ArticleService articleService;
    @RequestMapping("/create")
    public String createIndex(){
        try {
            esDao.createIndexAll(ArticleDoc.class);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    @RequestMapping("/search")
    public ESPagination search(){
        try {
            ESPagination pagination = new ESPagination();
            OperInfo op = new OperInfo();
            op.setField("title");
            op.setValue("标题");
            op.setOperType(OperType.like);
            pagination.setPage(1);
            pagination.setSize(10);
            List<OperInfo> ops = new ArrayList<OperInfo>();
            ops.add(op);
            ops.add(new OperInfo("id",1422,OperType.gte));
            ops.add(new OperInfo("content","我们",OperType.notLike));
            pagination.setOp(ops);
            List<String> highLightFields = new ArrayList<String>();
            highLightFields.add("title");
            pagination.setHighLightFields(highLightFields);
            pagination = esDao.search(ArticleDoc.class, pagination);
            return pagination;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @RequestMapping("/update")
    public String update() throws IOException, IllegalAccessException, ParseException, NoSuchFieldException {

        Article articleDoc = articleService.getById(1423);
        articleDoc.setTitle("修改了标题");
        List<ArticleDoc> list = new ArrayList<ArticleDoc>();
        list.add(new ArticleDoc(articleDoc));
        esDao.updateData(ArticleDoc.class,list);
        return "ok";
    }
    @RequestMapping("/del")
    public String del() throws IllegalAccessException, NoSuchFieldException, IOException {
        Article articleDoc = articleService.getById(1423);
        List<ArticleDoc> list = new ArrayList<ArticleDoc>();
        list.add(new ArticleDoc(articleDoc));
        esDao.delData(ArticleDoc.class,list);
        return "ok";
    }
    public static void main(String[] args) {
        SpringApplication.run(ESController.class,args);
    }
}
