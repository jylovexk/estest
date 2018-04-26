package es.elasticSearch;

import java.util.List;

/**
 * Created by zjf on 2018/4/25.
 */
public class ESPagination {
    private int offset;
    private int size;
    private int page;
    private List<OperInfo> op;
    private Object data;
    private List<String> highLightFields;

    public int getOffset() {
        return (page-1)*page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<OperInfo> getOp() {
        return op;
    }

    public void setOp(List<OperInfo> op) {
        this.op = op;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<String> getHighLightFields() {
        return highLightFields;
    }

    public void setHighLightFields(List<String> highLightFields) {
        this.highLightFields = highLightFields;
    }
}
