package com.fuel4media.carrythistoo.permissions


/**
 * Created by craterzone on 4/4/16.
 */
@Retention()
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class AfterPermissionDenied(val value: Int)