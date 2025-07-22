package com.appetiser.core.common.compose.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlin.reflect.KClass

@Composable
inline fun <reified D : Any, reified R : Any> resultBackNavigator(
    navController: NavController
): ResultBackNavigator<D, R> = resultBackNavigator(
    destination = D::class,
    resultNavType = R::class,
    navController = navController,
)

@Composable
inline fun <reified D : Any, reified R : Any> resultRecipient(
    navBackStackEntry: NavBackStackEntry
): ResultRecipient<D, R> = resultRecipient(
    navBackStackEntry = navBackStackEntry,
    originType = D::class,
    resultNavType = R::class
)

@Composable
@PublishedApi
internal fun <D : Any, R : Any> resultBackNavigator(
    destination: KClass<D>,
    resultNavType: KClass<R>,
    navController: NavController
): ResultBackNavigator<D, R> {
    val backNavigator = remember(navController, destination, resultNavType) {
        ResultBackNavigatorImpl(
            navController = navController,
            resultOriginType = destination,
            resultNavType = resultNavType
        )
    }

    backNavigator.HandleCanceledEffect()

    return backNavigator
}

@Composable
@PublishedApi
internal fun <D : Any, R : Any> resultRecipient(
    navBackStackEntry: NavBackStackEntry,
    originType: KClass<D>,
    resultNavType: KClass<R>
): ResultRecipient<D, R> = remember(navBackStackEntry, originType, resultNavType) {
    ResultRecipientImpl(
        navBackStackEntry = navBackStackEntry,
        resultOriginType = originType,
        resultNavType = resultNavType,
    )
}

internal fun <D : Any, R : Any> resultKey(
    resultOriginType: KClass<D>,
    resultNavType: KClass<R>
) = "baseplate-navigation@${resultOriginType.qualifiedName}@${resultNavType.qualifiedName}@result"

internal fun <D : Any, R : Any> canceledKey(
    resultOriginType: KClass<D>,
    resultNavType: KClass<R>
) = "baseplate-navigation@${resultOriginType.qualifiedName}@${resultNavType.qualifiedName}@canceled"
