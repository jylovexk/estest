
import com.sun.glass.ui.Application;
import es.annotation.Fields;
import es.modelDoc.ArticleDoc;
import jpa.model.Article;
import jpa.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by zjf on 2018/4/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"jpa.service","es"})
@EnableJpaRepositories(basePackages = "jpa.dao")
@EntityScan(basePackages = "jpa.model")
public class JpaTest {
    @Autowired
    private DataSource dataSource;
   @Autowired
    private ArticleService articleService;
    private Logger logger = LoggerFactory.getLogger(JpaTest.class);
    @Test
    public void testFindAll(){
        List<Article> list = articleService.findAll();
    }
    @Test
    public void dataSource()throws Exception{
        System.out.println(dataSource.getConnection().toString());
    }
    @Test
    public void testenum(){
        String name = Fields.Type.TEXT.name();
        System.out.println("###"+name.toLowerCase());
    }
    @Test
    public void inserData() throws IllegalAccessException, NoSuchFieldException, ParseException, IOException {
        articleService.insertData();
    }

}
