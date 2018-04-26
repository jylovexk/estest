package es.annotation;

import java.lang.annotation.*;

/**
 * Created by zjf on 2018/4/23.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Index {
    String value();
    String docType();
}
