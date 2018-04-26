package jpa.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import jpa.model.Article;
import org.springframework.stereotype.Repository;

/**
 * Created by zjf on 2018/4/23.
 */
@Repository
public interface ArticleDao extends CrudRepository<Article,Integer> {
}
