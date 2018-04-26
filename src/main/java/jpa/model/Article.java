package jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zjf on 2018/4/23.
 */
@Entity
@Table(name = "tb_ecnApp_article",schema = "dbo")
public class Article implements Serializable{
    @GeneratedValue
    @Id
    private Integer id;

    @Column(name = "level1_id")
    private Integer level1Id;
    @Column(name = "level2_id")
    private Integer level2Id;

    private String title;
    @Column(name = "H5_url")
    private String h5Url;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "top_status")
    private int topStatus;

    private String content;
    @Column(name = "release_time")
    private String releaseTime;
    @Column(name = "release_status")
    private Integer releaseStatus;
    @Column(name = "article_url")
    private String articleUrl;
    @Column(name = "end_edit_time")
    private String endEditTime;
    @Column(name = "image_status")
    private int imageStatus;

    private String describes;
    @Column(name = "time_point")
    private String timePoint;

    @Transient
    //获取一级、二级栏目名称字段
    private String level1_name;
    @Transient
    private String level2_name;
    @Transient
    private String imageUrl2;
    @Transient
    private String imageUrl3;

    private Integer update_time_status;

    private String operator;

    private String source;

    private int increment;

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
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

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3;
    }

    public String getLevel1_name() {
        return level1_name;
    }

    public void setLevel1_name(String level1_name) {
        this.level1_name = level1_name;
    }

    public String getLevel2_name() {
        return level2_name;
    }

    public void setLevel2_name(String level2_name) {
        this.level2_name = level2_name;
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
