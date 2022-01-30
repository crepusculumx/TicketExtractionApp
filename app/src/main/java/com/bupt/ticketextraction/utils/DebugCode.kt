package com.bupt.ticketextraction.utils

/**
 * Debug用代码，Release版本把带有此注解的全部删除
 */
@Target(AnnotationTarget.EXPRESSION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class DebugCode
