package com.lukasstancikas.amrotestvenues.extensions

import android.app.Activity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ExtraNotNull<T>(private val key: String, private val default: T) :
    ReadOnlyProperty<Activity, T> {
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return try {
            thisRef.intent?.extras?.get(key) as? T ?: default
        } catch (e: ClassCastException) {
            default
        }
    }
}