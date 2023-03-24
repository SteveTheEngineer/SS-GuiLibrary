package me.ste.stevesseries.inventoryguilibrary

import me.ste.stevesseries.guilib.api.extensions.closeGui
import me.ste.stevesseries.guilib.api.extensions.getGui
import me.ste.stevesseries.guilib.api.extensions.openGui
import me.ste.stevesseries.guilib.api.gui.inventory.InventoryGuiController
import me.ste.stevesseries.guilib.compat.ControllerWidgetWrapper
import me.ste.stevesseries.guilib.compat.WidgetAdapter
import me.ste.stevesseries.inventoryguilibrary.widget.Widget
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

@Deprecated("For backwards compatibility only.")
object PlayerGUIS : Iterable<MutableMap.MutableEntry<UUID, Widget>> {
    fun Player.openGui(gui: Widget) {
        this.gui = gui
    }

    fun Player.rerenderGui() {
        rerender(this)
    }

    var Player.gui: Widget?
        get() = PlayerGUIS[this]
        set(value) {
            if (value != null) {
                PlayerGUIS[this] = value
            } else {
                if (PlayerGUIS[this] != null) {
                    this.closeGui()
                }
            }
        }

    val GUIS: MutableMap<UUID, Widget> get() {
        val map = mutableMapOf<UUID, Widget>()

        for (player in Bukkit.getOnlinePlayers()) {
            map[player.uniqueId] = this[player] ?: continue
        }

        return map
    }

    operator fun set(player: Player, gui: Widget) {
        player.openGui(WidgetAdapter(gui))
    }

    operator fun get(player: Player): Widget? {
        val gui = player.getGui() ?: return null

        if (gui is WidgetAdapter) {
            return gui.widget
        }

        val wrapper = ControllerWidgetWrapper(gui)
        wrapper.player = player
        return wrapper
    }

    fun rerender(player: Player) {
        val gui = player.getGui()
        (gui as? InventoryGuiController)?.update()
    }

    override fun iterator(): Iterator<MutableMap.MutableEntry<UUID, Widget>> = this.GUIS.iterator()
}