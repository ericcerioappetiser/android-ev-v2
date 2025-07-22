package com.appetiser.baseplate.common.util

/**
 * Finds an enum constant by a given value using a mapping function.
 *
 * This function allows searching for an enum value based on a provided function
 * that maps an enum constant to a specific value.
 *
 * @param value The value to search for in the enum constants.
 * @return The matching enum constant, or null if no match is found.
 */
inline infix fun <reified E : Enum<E>, V> ((E) -> V).findBy(value: V): E? {
    return enumValues<E>().firstOrNull { this(it) == value }
}
