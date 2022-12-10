package top.flzjkl.fluency.simple.annotation;

import org.springframework.context.annotation.Import;
import top.flzjkl.fluency.simple.spring.CustomScannerRegistrar;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomScannerRegistrar.class)
@Documented
public @interface RpcScan {
    String[] basePackage();
}
