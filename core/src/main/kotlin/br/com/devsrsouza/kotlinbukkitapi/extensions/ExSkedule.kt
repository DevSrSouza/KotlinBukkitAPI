package br.com.devsrsouza.kotlinbukkitapi.extensions.skedule

import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import com.okkero.skedule.BukkitSchedulerController
import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule

fun WithPlugin<*>.schedule(
        initialContext: SynchronizationContext = SynchronizationContext.SYNC,
        co: suspend BukkitSchedulerController.() -> Unit
) = plugin.schedule(initialContext, co)