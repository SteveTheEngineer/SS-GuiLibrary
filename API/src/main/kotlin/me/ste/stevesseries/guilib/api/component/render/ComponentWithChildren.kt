package me.ste.stevesseries.guilib.api.component.render

import me.ste.stevesseries.guilib.api.canvas.ItemCanvas

data class ComponentWithChildren(
    val component: ComponentWithPosition,
    var children: List<ComponentWithChildren>
) {
    /**
     * Renders ONLY the items and ignores the components.
     */
    fun renderItems(canvas: ItemCanvas) {
        val componentCanvas = ComponentCanvas(this.component.width, this.component.height)
        this.component.component.renderTo(componentCanvas)

        for (child in this.children) {
            val childComponent = child.component

            val childCanvas = ItemCanvas(childComponent.width, childComponent.height)
            child.renderItems(childCanvas)

            componentCanvas.copyFrom(childComponent.x, childComponent.y, childCanvas)
        }

        canvas.copyFrom(0, 0, componentCanvas)
    }

    /**
     * Checks if the state has been changed, and re-renders the child components if it has. Also calls the [processChanges] method of the children recursively.
     * @return true, if the items should be re-rendered.
     */
    fun processChanges(): Boolean {
        val changed = this.component.component.hasChanged()

        if (!changed) {
            var childrenChanged = false

            for (child in this.children) {
                val changes = child.processChanges()

                if (changes) {
                    childrenChanged = true
                }
            }

            return childrenChanged
        }

        for (child in this.children) {
            child.destroyHierarchy()
        }

        var rendered: ComponentWithChildren
        try {
            rendered =
                this.component.component.renderComponents(this.component.x, this.component.y, this.component.width, this.component.height)
        } catch (t: Throwable) {
            rendered = ComponentWithChildren(this.component, emptyList())
            t.printStackTrace()
        }

        this.children = rendered.children

        return true
    }

    /**
     * Calls the [me.ste.stevesseries.guilib.api.component.GuiComponent.destroy] on all the component hierarchy.
     */
    fun destroyHierarchy() {
        for (child in this.children) {
            child.destroyHierarchy()
        }

        this.component.component.destroy()
    }
}
