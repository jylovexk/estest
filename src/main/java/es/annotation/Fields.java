package es.annotation;

import java.lang.annotation.*;

/**
 * Created by zjf on 2018/4/23.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Fields {

    enum Type{TEXT,KEYWORD,LONG,INTEGER,SHORT,BYTE,DOUBLE,FLOAD,DATE,BOOLEAN/*,BINARY*/}
    enum Store{TRUE,FALSE}
    enum Index{TRUE,FALSE}
    Type filedType() default Type.TEXT;
    Store filedStore() default Store.FALSE;
    Index fieldIndex() default  Index.TRUE;
    String dataFormate() default "";

}
