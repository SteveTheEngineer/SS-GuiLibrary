package me.ste.stevesseries.inventoryguilibrary.widget.builtin

import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

@Deprecated("For backwards compatibility only.")
class StaticButton(
    x: Int, y: Int, width: Int, height: Int, item: ItemStack,
    clickConsumer: Consumer<ClickType>
) : DynamicButton(
    x, y, width, height,
    { item }, clickConsumer
)