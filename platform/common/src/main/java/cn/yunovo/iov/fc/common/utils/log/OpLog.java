package cn.yunovo.iov.fc.common.utils.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpLog {

	OpTypeEnum opType() ;
	
	String opName() default "";
	
	String opDesc() default "";

}
