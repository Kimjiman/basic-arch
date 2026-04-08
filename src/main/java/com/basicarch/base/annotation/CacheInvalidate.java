package com.basicarch.base.annotation;

import com.basicarch.base.constants.CacheType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * packageName    : com.basicarch.base.annotation
 * fileName       : Facade
 * author         : KIM JIMAN
 * date           : 26. 3. 5. 화요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 5.     KIM JIMAN      First Commit
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheInvalidate {
    CacheType value();
}
