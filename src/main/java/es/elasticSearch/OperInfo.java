package es.elasticSearch;

/**
 * Created by zjf on 2018/4/25.
 */
public class OperInfo {
    private String field;
    private Object value;
    private OperType operType ;

    public OperInfo() {
    }

    public OperInfo(String field, Object value, OperType operType) {
        this.field = field;
        this.value = value;
        this.operType = operType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public OperType getOperType() {
        return operType;
    }

    public void setOperType(OperType operType) {
        this.operType = operType;
    }
}
