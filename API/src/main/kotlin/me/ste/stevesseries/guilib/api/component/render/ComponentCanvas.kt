package me.ste.stevesseries.guilib.api.component.render

import me.ste.stevesseries.guilib.api.canvas.ItemCanvas
import me.ste.stevesseries.guilib.api.component.GuiComponent
import java.util.function.Supplier

class ComponentCanvas(width: Int, height: Int) : ItemCanvas(width, height) {
    val components = mutableListOf<ComponentWithPosition>()
    val watchedValues = mutableListOf<Supplier<Int>>()

    /**
     * Inserts the [component] with the start position at the specified [startX] and [startY] and the provided [width] and [height].
     */
    fun <T : GuiComponent> component(startX: Int, startY: Int, width: Int, height: Int, component: T) {
        // Find positions
        if (!this.isInBounds(startX, startY)) {
            throw IllegalArgumentException("Component start position out of bounds: x $startX, y $startY.")
        }

        if (width == 0 || height == 0) {
            return
        }

        // Validate width and height
        val minWidth = component.getMinWidth()
        val minHeight = component.getMinHeight()

        if (minWidth < 0) {
            throw IllegalArgumentException("The minimum width of a component cannot be negative.")
        }
        if (minHeight < 0 ){
            throw IllegalArgumentException("The minimum height of a component cannot be negative.")
        }

        if (width < minWidth) {
            throw IllegalArgumentException("The width ($width) cannot be less than the component's minimum width ($minWidth).")
        }
        if (height < minHeight) {
            throw IllegalArgumentException("The height ($height) cannot be less than the component's minimum height ($minHeight).")
        }

        val endX = startX + width - 1
        val endY = startY + height - 1

        if (!this.isInBounds(endX, endY)) {
            throw IllegalArgumentException("Component end position out of bounds: x $endX (startX $startX + width $width), y $endY (startY $startY + height $height).")
        }

        // Add component
        this.components += ComponentWithPosition(startX, startY, width, height, component)
    }

    /**
     * Adds an int to the watches.
     */
    fun watchInt(supplier: Supplier<Int>): Int {
        this.watchedValues += supplier
        return supplier.get()
    }

    /**
     * Adds the hash code of the provided value to the watches.
     */
    fun <T> watch(supplier: Supplier<T>): T {
        this.watchInt { supplier.get().hashCode() }
        return supplier.get()
    }
}