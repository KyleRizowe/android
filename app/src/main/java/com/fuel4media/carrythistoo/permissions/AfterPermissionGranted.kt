package com.fuel4media.carrythistoo.permissions

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by craterzone on 2/4/16.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class AfterPermissionGranted(val value: Int)
