package es.elasticSearch;

import es.annotation.Fields;
import es.annotation.Index;
import es.modelDoc.ArticleDoc;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.mapper.ParseContext;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.ScrollableHitSource;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.fetch.subphase.highlight.Highlighter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zjf on 2018/4/23.
 */
@Component
@ConfigurationProperties
public class ESDao<T> {
    private Logger logger = LoggerFactory.getLogger(ESDao.class);
    @Value("${spring.elasticsearch.number_of_shards}")
    private String shardsNum;
    @Value("${spring.elasticsearch.number_of_replicas}")
    private String replicasNum;
    @Value("${spring.elasticsearch.requestTimeOut}")
    private int requestTimeOut;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public ESDao() {
        System.out.println("###ES init...");
    }

    public boolean existsIndex(Class<T> cla) throws IOException {
        Index index = cla.getAnnotation(Index.class);
        if(index==null){
            return true;
        }
        String indexNmae = index.value();
        if(indexNmae==null||"".equals(indexNmae)){
            indexNmae = cla.getSimpleName().toLowerCase();
        }
        Response response = restHighLevelClient.getLowLevelClient().performRequest("HEAD", "/"+indexNmae);
        if(response.getStatusLine().getStatusCode()==200){
            return true;
        }
        return  false;
    }

    /**
     * 新建索引库
     * @throws IOException
     */
    public void createIndexAll(Class<T> cla) throws IOException {
        Index index = cla.getAnnotation(Index.class);
        if(index==null){
            return;
        }
        String indexNmae = index.value();
        String docType = index.docType();
        if(indexNmae==null||"".equals(indexNmae)){
            indexNmae = cla.getSimpleName().toLowerCase();
        }
        if(docType==null||"".equals(docType)){
            docType = cla.getSimpleName().toLowerCase();
        }
        CreateIndexRequest request = new CreateIndexRequest(indexNmae);
        request.settings(Settings.builder().put("number_of_shards",shardsNum).put("number_of_replicas",replicasNum));
        XContentBuilder mapping = XContentFactory.jsonBuilder();
        mapping.startObject().startObject(docType).startObject("properties");
        Field[] fields = cla.getDeclaredFields();
        for(Field field:fields){
            Fields annotationField = field.getAnnotation(Fields.class);
            if(annotationField==null){
                continue;
            }
            String type = annotationField.filedType().name().toLowerCase();
            String store = annotationField.filedStore().name().toLowerCase();
            String indexType = annotationField.fieldIndex().name().toLowerCase();
            mapping.startObject(field.getName()).field("type",type).field("store",Boolean.valueOf(store)).field("index",Boolean.valueOf(indexType));
            if(annotationField.filedType()== Fields.Type.DATE&&(!"".equals(annotationField.dataFormate()))){
                mapping.field("format",annotationField.dataFormate());
            }
            mapping.endObject();
        }

        mapping.endObject().endObject().endObject();
        request.mapping(docType,mapping);
        request.timeout(TimeValue.timeValueSeconds(requestTimeOut));
        restHighLevelClient.indices().create(request);
    }
    public void insertData(Class<T> cla, List<T> datas) throws NoSuchFieldException, IllegalAccessException, ParseException, IOException {
        Index index = cla.getAnnotation(Index.class);
        if(index==null){
            return;
        }
        String indexNmae = index.value();
        String docType = index.docType();
        if(indexNmae==null||"".equals(indexNmae)){
            indexNmae = cla.getSimpleName().toLowerCase();
        }
        if(docType==null||"".equals(docType)){
            docType = cla.getSimpleName().toLowerCase();
        }
        BulkRequest bulkRequest = new BulkRequest();
        List<Field> fields = matchField(cla);
        Field id = cla.getDeclaredField("id");
        if(id==null){
            return;
        }
        for(T data:datas){
            if(!id.isAccessible()){
                id.setAccessible(true);
            }
            int idValue = (Integer) id.get(data);
            if(id.isAccessible()){
                id.setAccessible(false);
            }
            IndexRequest request = new IndexRequest(indexNmae,docType,String.valueOf(idValue) );
            Map<String,Object> source = new HashMap<String,Object>(fields.size());
            for(Field field:fields){
                if(!field.isAccessible()){
                    field.setAccessible(true);
                }
                source.put(field.getName(),getData(field,data));
                if(field.isAccessible()){
                    field.setAccessible(false);
                }
            }
            request.source(source);
            request.timeout(TimeValue.timeValueSeconds(requestTimeOut));
            bulkRequest.add(request);
        }
        bulkRequest.timeout(TimeValue.timeValueSeconds(requestTimeOut));
        restHighLevelClient.bulk(bulkRequest);

    }

    public void updateData(Class<T> cla, List<T> datas) throws NoSuchFieldException, IllegalAccessException, ParseException, IOException {
        Index index = cla.getAnnotation(Index.class);
        if(index==null){
            return;
        }
        String indexNmae = index.value();
        String docType = index.docType();
        if(indexNmae==null||"".equals(indexNmae)){
            indexNmae = cla.getSimpleName().toLowerCase();
        }
        if(docType==null||"".equals(docType)){
            docType = cla.getSimpleName().toLowerCase();
        }
        BulkRequest bulkRequest = new BulkRequest();
        List<Field> fields = matchField(cla);
        Field id = cla.getDeclaredField("id");
        if(id==null){
            return;
        }
        for(T data:datas){
            if(!id.isAccessible()){
                id.setAccessible(true);
            }
            int idValue = (Integer) id.get(data);
            if(id.isAccessible()){
                id.setAccessible(false);
            }
            UpdateRequest request = new UpdateRequest(indexNmae,docType,String.valueOf(idValue) );
            Map<String,Object> source = new HashMap<String,Object>(fields.size());
            for(Field field:fields){
                if(!field.isAccessible()){
                    field.setAccessible(true);
                }
                source.put(field.getName(),getData(field,data));
                if(field.isAccessible()){
                    field.setAccessible(false);
                }
            }
            request.doc(source);
            request.timeout(TimeValue.timeValueSeconds(requestTimeOut));
            request.upsert(source);
            bulkRequest.add(request);
        }
        bulkRequest.timeout(TimeValue.timeValueSeconds(requestTimeOut));
        restHighLevelClient.bulk(bulkRequest);
    }
    public void delData(Class<T> cla, List<T> datas) throws NoSuchFieldException, IllegalAccessException, IOException {
        Index index = cla.getAnnotation(Index.class);
        if(index==null){
            return;
        }
        String indexNmae = index.value();
        String docType = index.docType();
        if(indexNmae==null||"".equals(indexNmae)){
            indexNmae = cla.getSimpleName().toLowerCase();
        }
        if(docType==null||"".equals(docType)){
            docType = cla.getSimpleName().toLowerCase();
        }
        BulkRequest bulkRequest = new BulkRequest();
        List<Field> fields = matchField(cla);
        Field id = cla.getDeclaredField("id");
        if(id==null){
            return;
        }
        for(T data:datas){
            if(!id.isAccessible()){
                id.setAccessible(true);
            }
            int idValue = (Integer) id.get(data);
            if(id.isAccessible()){
                id.setAccessible(false);
            }
            DeleteRequest request = new DeleteRequest(indexNmae,docType,String.valueOf(idValue) );
            request.timeout(TimeValue.timeValueSeconds(requestTimeOut));
            bulkRequest.add(request);
        }
        bulkRequest.timeout(TimeValue.timeValueSeconds(requestTimeOut));
        restHighLevelClient.bulk(bulkRequest);

    }
    public ESPagination search(Class<T> cla,ESPagination pagination) throws IOException {
        Index index = cla.getAnnotation(Index.class);
        if(index==null){
            return pagination;
        }
        String indexNmae = index.value();
        String docType = index.docType();
        if(indexNmae==null||"".equals(indexNmae)){
            indexNmae = cla.getSimpleName().toLowerCase();
        }
        if(docType==null||"".equals(docType)){
            docType = cla.getSimpleName().toLowerCase();
        }
        SearchRequest request = new SearchRequest(indexNmae);
        request.types(docType);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        searchSource.from(pagination.getOffset());
        searchSource.size(pagination.getSize());
        searchSource.query(initBuild(boolQueryBuilder,pagination));
        List<String> highLightFields = pagination.getHighLightFields();
        if(highLightFields!=null&&(!highLightFields.isEmpty())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<font color='red'>").postTags("</font>");
            for(String field:highLightFields){
                HighlightBuilder.Field hightField = new HighlightBuilder.Field(field);
                highlightBuilder.field(hightField);
            }
            searchSource.highlighter(highlightBuilder);
        }

        request.source(searchSource);

        searchSource.timeout(TimeValue.timeValueSeconds(requestTimeOut));
        SearchResponse response = restHighLevelClient.search(request);
        SearchHit[] hits = response.getHits().getHits();
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>(hits.length);
        for(SearchHit hit:hits){
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Map<String, Object> data = hit.getSourceAsMap();
            for(String key:highlightFields.keySet()){
                data.put(key,highlightFields.get(key).fragments()[0].toString());
            }
            datas.add(data);
        }
        pagination.setData(datas);
        return pagination;
    }
    private Object getData(Field field,T data) throws IllegalAccessException, ParseException, UnsupportedEncodingException {
        Fields annotationField = field.getAnnotation(Fields.class);
        if(annotationField==null){
            return null;
        }
        switch (annotationField.filedType()){
            case TEXT:
            case KEYWORD:
                if(field.get(data)==null){
                    return null;
                }
                return new String(field.get(data).toString().getBytes(),"utf-8");
            case LONG:
                return field.getLong(data);
            case INTEGER:
                return (Integer)field.get(data);
            case SHORT:
                return field.getShort(data);
            case BYTE:
                return field.getByte(data);
            case DOUBLE:
                return field.getDouble(data);
            case FLOAD:
                return field.getFloat(data);
            case DATE:
                Object value = field.get(data);
                if(value==null){
                    return null;
                }else{
                    return value;
                }
            case BOOLEAN:
                return field.getBoolean(data);
        }
        return null;
    }
    private List<Field> matchField(Class<T> cla){
        Field[] fields = cla.getDeclaredFields();
        List<Field> list = new ArrayList<Field>();
        for(Field field:fields){
            Fields annotation = field.getAnnotation(Fields.class);
            if(annotation!=null){
                list.add(field);
            }
        }
        return list;
    }
    private BoolQueryBuilder initBuild(BoolQueryBuilder boolQueryBuilder,ESPagination pagination) {
        List<OperInfo> op = pagination.getOp();
        if(pagination==null|| op ==null|| op.isEmpty()){
            return boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }
        for(OperInfo operInfo:op){
            switch (operInfo.getOperType()){
                case eq:
                    boolQueryBuilder.must(QueryBuilders.termQuery(operInfo.getField(),operInfo.getValue()));
                    break;
                case notEq:
                    boolQueryBuilder.mustNot(QueryBuilders.termQuery(operInfo.getField(),operInfo.getValue()));
                    break;
                case like:
                    boolQueryBuilder.must(QueryBuilders.matchQuery(operInfo.getField(),operInfo.getValue()));
                    break;
                case notLike:
                    boolQueryBuilder.mustNot(QueryBuilders.matchQuery(operInfo.getField(),operInfo.getValue()));
                    break;
                case lt:
                    boolQueryBuilder.must(QueryBuilders.rangeQuery(operInfo.getField()).lt(operInfo.getValue()));
                    break;
                case gt:
                    boolQueryBuilder.must(QueryBuilders.rangeQuery(operInfo.getField()).gt(operInfo.getValue()));
                    break;
                case lte:
                    boolQueryBuilder.must(QueryBuilders.rangeQuery(operInfo.getField()).lte(operInfo.getValue()));
                    break;
                case gte:
                    boolQueryBuilder.must(QueryBuilders.rangeQuery(operInfo.getField()).gte(operInfo.getValue()));
                    break;
                case range:
                    Object value = operInfo.getValue();
                    if(value instanceof  String[]){
                        boolQueryBuilder.must(QueryBuilders.rangeQuery(operInfo.getField()).from(((String[]) value)[0]).to(((String[]) value)[1]));
                    }
                    break;
                default:
                    break;
            }
        }
        return boolQueryBuilder;
    }
}
