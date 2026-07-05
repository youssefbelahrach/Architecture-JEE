package ma.enset.miniioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Indique qu'une classe doit être gérée par AnnotationApplicationContext. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
    /** Nom facultatif du bean. Si vide, le nom de classe commence par une minuscule. */
    String value() default "";
}
