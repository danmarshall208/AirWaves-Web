package com.airwaves.airwavesweb.util

/**
 *  Prevents unchecked casts by checking if the object is a list and each element
 *  is the specified type, returning null otherwise.
 */
inline fun <reified T : Any> Any.takeIfListOf() =
        when (this) {
            is List<*> -> filterIsInstance<T>().takeIf { it.size == this.size }
            else -> null
        }

/**
 *  Prevents unchecked casts by checking if the object is a list and each element
 *  is the specified type, throwing [IllegalArgumentException] otherwise.
 */
inline fun <reified T : Any> Any.requireListOf() =
        requireNotNull(takeIfListOf<T>()) { "All list elements must be of the specified type" }