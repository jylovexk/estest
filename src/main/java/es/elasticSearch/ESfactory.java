package es.elasticSearch;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by zjf on 2018/4/23.
 */
@Component
@ConfigurationProperties
public class ESfactory {
    private Logger logger = LoggerFactory.getLogger(ESfactory.class);
    @Value("${spring.elasticsearch.host}")
    private String host;
    @Value("${spring.elasticsearch.port}")
    private Integer port;
    @Value("${spring.elasticsearch.scheme}")
    private String scheme;
    @Value("${spring.elasticsearch.maxConNum}")
    private Integer maxConNum;
    @Value("${spring.elasticsearch.connectTimeout}")
    private int connectTimeout;
    @Value("${spring.elasticsearch.socketTimeout}")
    private int socketTimeout;
    @Value("${spring.elasticsearch.connectionRequestTimeout}")
    private int connectionRequestTimeout;
    @Value("${spring.elasticsearch.maxRetryTimeoutMillis}")
    private int maxRetryTimeoutMillis;

    public ESfactory() {
        System.out.println("##ESfactory init...");
    }

    @Bean(autowire = Autowire.BY_TYPE)
    public RestHighLevelClient factory(){
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, scheme));
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                builder.setConnectTimeout(connectTimeout);
                builder.setSocketTimeout(socketTimeout);
                builder.setConnectionRequestTimeout(connectionRequestTimeout);
                return builder;
            }
        }).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                httpAsyncClientBuilder.setMaxConnTotal(maxConNum);
                return httpAsyncClientBuilder;
            }
        }).setMaxRetryTimeoutMillis(maxRetryTimeoutMillis);
        return new RestHighLevelClient(builder);
    }
}
