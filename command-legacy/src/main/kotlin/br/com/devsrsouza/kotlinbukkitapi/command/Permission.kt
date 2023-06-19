package br.com.devsrsouza.kotlinbukkitapi.command

import br.com.devsrsouza.kotlinbukkitapi.extensions.allPermission
import br.com.devsrsouza.kotlinbukkitapi.extensions.anyPermission
import br.com.devsrsouza.kotlinbukkitapi.extensions.hasPermissionOrStar

public inline fun <reified T> Executor<*>.permission(
    permission: String,
    builder: () -> T,
): T = permission({ sender.hasPermission(permission) }, builder)

public inline fun <reified T> Executor<*>.permissionOrStar(
    permission: String,
    builder: () -> T,
): T = permission({ sender.hasPermissionOrStar(permission) }, builder)

public inline fun <reified T> Executor<*>.anyPermission(
    vararg permissions: String,
    builder: () -> T,
): T = permission({ sender.anyPermission(*permissions) }, builder)

public inline fun <reified T> Executor<*>.allPermission(
    vararg permissions: String,
    builder: () -> T,
): T = permission({ sender.allPermission(*permissions) }, builder)

public inline fun <reified T> Executor<*>.permission(
    permissionChecker: () -> Boolean,
    builder: () -> T,
): T = if (permissionChecker()) builder() else fail(command.permissionMessage ?: "")
