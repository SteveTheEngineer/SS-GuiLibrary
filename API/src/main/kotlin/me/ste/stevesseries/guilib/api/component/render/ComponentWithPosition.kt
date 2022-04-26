package me.ste.stevesseries.guilib.api.component.render

import me.ste.stevesseries.guilib.api.component.GuiComponent

data class ComponentWithPosition(
    val x: Int, val y: Int,
    val width: Int, val height: Int,
    val component: GuiComponent
)
