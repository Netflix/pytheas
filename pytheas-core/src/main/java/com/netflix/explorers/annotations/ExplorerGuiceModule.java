package com.netflix.explorers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExplorerGuiceModule {
    String disableProperty() default "";
    String jerseyPackagePath() default "";
}
