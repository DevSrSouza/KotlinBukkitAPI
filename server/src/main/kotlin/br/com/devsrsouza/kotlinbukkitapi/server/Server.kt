package br.com.devsrsouza.kotlinbukkitapi.server

import br.com.devsrsouza.kotlinbukkitapi.server.api.chat.ServerChat
import br.com.devsrsouza.kotlinbukkitapi.server.api.item.ServerItem
import br.com.devsrsouza.kotlinbukkitapi.server.api.nbt.ServerNBT
import br.com.devsrsouza.kotlinbukkitapi.server.api.chat.ServerTitle
import br.com.devsrsouza.kotlinbukkitapi.server.api.player.ServerTabList
import br.com.devsrsouza.kotlinbukkitapi.server.bukkit.v1_10.chat.ServerChatImpl as ServerChatImplBukkit
import br.com.devsrsouza.kotlinbukkitapi.server.bukkit.v1_11.chat.ServerTitleImpl as ServerTitleImplBukkit
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R3.nbt.ServerNBTImpl as ServerNBTImpl1_8_R3
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R3.player.ServerTabListImpl as ServerTabListImpl1_8_R3
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R3.chat.ServerTitleImpl as ServerTitleImpl1_8_R3
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R3.chat.ServerChatImpl as ServerChatImpl1_8_R3
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R3.item.ServerItemImpl as ServerItemImpl1_8_R3
import br.com.devsrsouza.kotlinbukkitapi.utils.server.ServerImplementation
import br.com.devsrsouza.kotlinbukkitapi.utils.server.apiVersion
import br.com.devsrsouza.kotlinbukkitapi.utils.server.implementation
import org.bukkit.Bukkit

object Server {
    private val server = Bukkit.getServer()
    private val impl = server.implementation
    private val api = server.apiVersion

    // chat
    object Chat {
        val chat: ServerChat by lazy {
            if (api.minor >= 10) {
                ServerChatImplBukkit
            } else {
                when (impl) {
                    is ServerImplementation.BukkitImplementation -> {
                        when (impl.nms) {
                            "v1_8_R1" -> TODO("${impl.nms} not implemented yet.")
                            "v1_8_R2" -> TODO("${impl.nms} not implemented yet.")
                            "v1_8_R3" -> ServerChatImpl1_8_R3
                            "v1_9_R1" -> TODO("${impl.nms} not implemented yet.")
                            "v1_10_R1" -> TODO("${impl.nms} not implemented yet.")
                            else -> throw NotImplementedError("${impl.nms} not implemented.")
                        }
                    }
                    is ServerImplementation.Glowstone ->
                        TODO("Glowstone not implemented yet.")
                    is ServerImplementation.UnknownImplementation ->
                        TODO("${impl.name} not implemented yet.")
                }
            }
        }

        val title: ServerTitle by lazy {
            if (api.minor >= 11) {
                ServerTitleImplBukkit
            } else {
                when (impl) {
                    is ServerImplementation.BukkitImplementation -> {
                        when (impl.nms) {
                            "v1_8_R1" -> TODO("${impl.nms} not implemented yet.")
                            "v1_8_R2" -> TODO("${impl.nms} not implemented yet.")
                            "v1_8_R3" -> ServerTitleImpl1_8_R3
                            "v1_9_R1" -> TODO("${impl.nms} not implemented yet.")
                            "v1_10_R1" -> TODO("${impl.nms} not implemented yet.")
                            else -> throw NotImplementedError("${impl.nms} not implemented.")
                        }
                    }
                    is ServerImplementation.Glowstone ->
                        TODO("Glowstone not implemented yet.")
                    is ServerImplementation.UnknownImplementation ->
                        TODO("${impl.name} not implemented yet.")
                }
            }
        }
    }

    // tab list
    val tabList: ServerTabList by lazy {
        when (impl) {
            is ServerImplementation.BukkitImplementation -> {
                when (impl.nms) {
                    "v1_8_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_8_R2" -> TODO("${impl.nms} not implemented yet.")
                    "v1_8_R3" -> ServerTabListImpl1_8_R3
                    "v1_9_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_10_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_11_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_12_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_13_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_13_R2" -> TODO("${impl.nms} not implemented yet.")
                    else -> throw NotImplementedError("${impl.nms} not implemented.")
                }
            }
            is ServerImplementation.Glowstone ->
                TODO("Glowstone not implemented yet.")
            is ServerImplementation.UnknownImplementation ->
                TODO("${impl.name} not implemented yet.")
        }
    }

    // item
    val item: ServerItem by lazy {
        when (impl) {
            is ServerImplementation.BukkitImplementation -> {
                when (impl.nms) {
                    "v1_8_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_8_R2" -> TODO("${impl.nms} not implemented yet.")
                    "v1_8_R3" -> ServerItemImpl1_8_R3
                    "v1_9_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_10_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_11_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_12_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_13_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_13_R2" -> TODO("${impl.nms} not implemented yet.")
                    else -> throw NotImplementedError("${impl.nms} not implemented.")
                }
            }
            is ServerImplementation.Glowstone ->
                TODO("Glowstone not implemented yet.")
            is ServerImplementation.UnknownImplementation ->
                TODO("${impl.name} not implemented yet.")
        }
    }

    // nbt
    val nbt: ServerNBT<*, *> by lazy {
        when (impl) {
            is ServerImplementation.BukkitImplementation -> {
                when (impl.nms) {
                    "v1_8_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_8_R2" -> TODO("${impl.nms} not implemented yet.")
                    "v1_8_R3" -> ServerNBTImpl1_8_R3
                    "v1_9_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_10_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_11_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_12_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_13_R1" -> TODO("${impl.nms} not implemented yet.")
                    "v1_13_R2" -> TODO("${impl.nms} not implemented yet.")
                    else -> throw NotImplementedError("${impl.nms} not implemented.")
                }
            }
            is ServerImplementation.Glowstone ->
                TODO("Glowstone not implemented yet.")
            is ServerImplementation.UnknownImplementation ->
                TODO("${impl.name} not implemented yet.")
        }
    }

}