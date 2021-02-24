package me.ste.stevesseries.inventoryguilibrary.widget.builtin

import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

/**
 * This widget represents a static rectangle with a click handler
 */
class StaticButton(
    x: Int, y: Int, width: Int, height: Int, item: ItemStack,
    clickConsumer: Consumer<ClickType>
) : DynamicButton(
    x, y, width, height,
    { item }, clickConsumer
)