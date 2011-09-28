package com.eschewobfuscation.util.grails;

import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.transform.GroovyASTTransformationClass;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 *
 * Marker interface
 *
 * Created by RobertElsner
 * Date: 7/11/11
 * Time: 2:12 PM
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@GroovyASTTransformationClass("com.eschewobfuscation.util.grails.ActiveFilterableASTTransformer")
public @interface ActiveFilterable {
}
