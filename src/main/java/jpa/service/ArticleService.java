package jpa.service;

import es.elasticSearch.ESDao;
import es.modelDoc.ArticleDoc;
import jpa.dao.ArticleDao;
import jpa.model.Article;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by zjf on 2018/4/23.
 */
@Service
public class ArticleService implements InitializingBean {
    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ESDao<ArticleDoc> esDao;

    public void afterPropertiesSet() throws Exception {
        if(!esDao.existsIndex(ArticleDoc.class)){
            esDao.createIndexAll(ArticleDoc.class);
        }
    }

    public List<Article> findAll(){
        Iterable<Article> articles = articleDao.findAll();
        Iterator<Article> iterator = articles.iterator();
        if(articles==null||(!iterator.hasNext())){
            return null;
        }
        List<Article> list = new ArrayList<Article>();
        while (iterator.hasNext()){
            Article article= iterator.next();
            list.add(article);
        }
        return list;
    }
    public void insertData() throws IllegalAccessException, NoSuchFieldException, ParseException, IOException {
        List<Article> articles =  findAll();
        esDao.insertData(ArticleDoc.class,transferDoc(articles));
    }
    private List<ArticleDoc> transferDoc(List<Article> articles){
        List<ArticleDoc> list = new ArrayList<ArticleDoc>(articles.size());
        for(Article article:articles){
            ArticleDoc doc = new ArticleDoc(article);
            list.add(doc);
        }
        return list;
    }
    public Article getById(Integer id){
        Optional<Article> article = articleDao.findById(id);
        return article.get();
    }
}
