package br.com.devsrsouza.kotlinbukkitapi.plugins.protocollib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.plugin.Plugin

// packet adapter DSL

inline fun packetAdapter(
        plugin: Plugin,
        priority: ListenerPriority = ListenerPriority.NORMAL,
        block: PacketAdapterDSL.() -> Unit
) = PacketAdapterDSL(plugin, priority).apply(block).also {
    ProtocolLibrary.getProtocolManager().addPacketListener(it)
}

class PacketAdapterIO(val type: PacketType? = null, val ignoreCancelled: Boolean = false, val event: PacketAdapterIOEvent)
typealias PacketAdapterIOEvent = PacketEvent.() -> Unit

class PacketAdapterDSL(
        plugin: Plugin,
        priority: ListenerPriority = ListenerPriority.NORMAL
) : PacketAdapter(plugin, priority) {

    val protocol = ProtocolLibrary.getProtocolManager()

    val recevingAdapters = mutableListOf<PacketAdapterIO>()
    val sedingAdapters = mutableListOf<PacketAdapterIO>()

    fun receiving(
            packetType: PacketType? = null,
            ignoreCancelled: Boolean = false,
            adapter: PacketAdapterIOEvent
    ) {
        recevingAdapters.add(PacketAdapterIO(packetType, ignoreCancelled, adapter))
    }

    fun sending(
            packetType: PacketType? = null,
            ignoreCancelled: Boolean = false,
            adapter: PacketAdapterIOEvent
    ) {
        sedingAdapters.add(PacketAdapterIO(packetType, ignoreCancelled, adapter))
    }

    fun unregister() = protocol.removePacketListener(this)

    override fun onPacketReceiving(event: PacketEvent) {
        for (adapter in recevingAdapters) {
            if(adapter.ignoreCancelled && event.isCancelled)
                continue
            if(adapter.type != null && event.packetType != adapter.type)
                continue

            adapter.event(event)
        }
    }

    override fun onPacketSending(event: PacketEvent) {
        for (adapter in sedingAdapters) {
            if(adapter.ignoreCancelled && event.isCancelled)
                continue
            if(adapter.type != null && event.packetType != adapter.type)
                continue

            adapter.event(event)
        }
    }
}
