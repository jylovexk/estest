package es.modelDoc;

import es.annotation.*;
import es.annotation.Index;
import jpa.model.Article;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zjf on 2018/4/23.
 */
@Index(value = "article",docType = "article")
public class ArticleDoc implements Serializable{

    @Fields(filedType = Fields.Type.INTEGER,filedStore = Fields.Store.TRUE)
    private Integer id;
    @Fields(filedType = Fields.Type.INTEGER,filedStore = Fields.Store.TRUE)
    private Integer level1Id;
    @Fields(filedType = Fields.Type.INTEGER,filedStore = Fields.Store.TRUE)
    private Integer level2Id;
    @Fields(filedType = Fields.Type.TEXT,filedStore = Fields.Store.TRUE)
    private String title;
    @Fields(filedType = Fields.Type.KEYWORD,fieldIndex = Fields.Index.FALSE)
    private String h5Url;
    @Fields(filedType = Fields.Type.KEYWORD,fieldIndex = Fields.Index.FALSE)
    private String imageUrl;
    @Fields(filedType = Fields.Type.INTEGER,filedStore = Fields.Store.TRUE)
    private int topStatus;
    @Fields(filedType = Fields.Type.TEXT)
    private String content;
    @Fields(filedType = Fields.Type.DATE,dataFormate = "YYYY-MM-DD  HH:mm:ss.SSS")
    private String releaseTime;

    private Integer releaseStatus;

    private String articleUrl;

    private String endEditTime;

    private int imageStatus;

    private String describes;

    private String timePoint;

    private Integer update_time_status;

    private String operator;

    private String source;

    public ArticleDoc() {
    }
    public ArticleDoc(Article article) {
        this.id = article.getId();
        this.level1Id = article.getLevel1Id();
        this.level2Id = article.getLevel2Id();
        this.title = article.getTitle();
        this.h5Url = article.getH5Url();
        this.imageUrl = article.getImageUrl();
        this.topStatus = article.getTopStatus();
        this.content = article.getContent();
        this.releaseTime = article.getReleaseTime();
    }
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getUpdate_time_status() {
        return update_time_status;
    }

    public void setUpdate_time_status(Integer update_time_status) {
        this.update_time_status = update_time_status;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevel1Id() {
        return level1Id;
    }

    public void setLevel1Id(Integer level1Id) {
        this.level1Id = level1Id;
    }

    public Integer getLevel2Id() {
        return level2Id;
    }

    public void setLevel2Id(Integer level2Id) {
        this.level2Id = level2Id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url == null ? null : h5Url.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public int getTopStatus() {
        return topStatus;
    }

    public void setTopStatus(int topStatus) {
        this.topStatus = topStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime == null ? null : releaseTime.trim();
    }


    public Integer getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(Integer releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl == null ? null : articleUrl.trim();
    }

    public String getEndEditTime() {
        return endEditTime;
    }

    public void setEndEditTime(String endEditTime) {
        this.endEditTime = endEditTime;
    }

    public int getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(int imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }
}
