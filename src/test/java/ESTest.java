import com.sun.glass.ui.Application;
import es.elasticSearch.ESDao;
import es.elasticSearch.ESPagination;
import es.elasticSearch.OperInfo;
import es.elasticSearch.OperType;
import es.modelDoc.ArticleDoc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjf on 2018/4/24.
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = {"classpath:application.properties","classpath:application-dev.properties"})
@Profile("dev")
@ComponentScan(basePackages = "es")
public class ESTest {
    @Autowired
    private ESDao<ArticleDoc> esDao;
    @Test
    public void createIndex() throws IOException {
        if(!esDao.existsIndex(ArticleDoc.class)){
            esDao.createIndexAll(ArticleDoc.class);
        }
    }
    @Test
    public void insertData() throws IllegalAccessException, NoSuchFieldException, ParseException, IOException {
        List<ArticleDoc> list = new ArrayList<ArticleDoc>();
        ArticleDoc articleDoc = new ArticleDoc();
        articleDoc.setId(0);
        articleDoc.setArticleUrl("setArticleUrl");
        articleDoc.setContent("setContent");
        articleDoc.setEndEditTime("2017-08-03 11:22:33");
        articleDoc.setImageUrl("setImageUrl");
        list.add(articleDoc);
        esDao.insertData(ArticleDoc.class,list);
    }
    @Test
    public void search() throws IOException {
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
        System.out.println(pagination.getData());
    }
}
