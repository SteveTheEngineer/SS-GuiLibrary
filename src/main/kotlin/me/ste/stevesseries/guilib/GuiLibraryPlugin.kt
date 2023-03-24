package me.ste.stevesseries.guilib

import me.ste.stevesseries.guilib.api.GuiManager
import me.ste.stevesseries.guilib.api.extensions.openGui
import me.ste.stevesseries.guilib.listener.InventoryListener
import me.ste.stevesseries.guilib.listener.PlayerListener
import me.ste.stevesseries.guilib.task.GuiTickerTask
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginLoader
import org.bukkit.plugin.RegisteredListener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import java.util.regex.Pattern
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.outputStream

class GuiLibraryPlugin : JavaPlugin() {
    private lateinit var manager: GuiManager

    override fun onLoad() {
        val path = this.file.toPath().resolveSibling("${this.file.nameWithoutExtension}-COMPAT.jar")

        var shouldRestart = !path.isRegularFile()

        val output = JarOutputStream(path.outputStream())

        output.putNextEntry(JarEntry("plugin.yml"))
        this.getResource("compat-plugin.yml")!!.copyTo(output)
        output.closeEntry()

        output.putNextEntry(JarEntry("me/ste/stevesseries/guilib/compat/CompatPlugin.class"))
        this.getResource("me/ste/stevesseries/guilib/compat/CompatPlugin.class")!!.copyTo(output)
        output.closeEntry()

        output.close()

        if (shouldRestart) {
            this.logger.warning("!!! IMPORTANT !!!")
            this.logger.warning("")
            this.logger.warning("The legacy support plugin has been unpacked, and the server will shutdown now to apply the changes! Please start the server yourself if it doesn't do so automatically! You may also ignore all of the plugin loading errors related to SS-InventoryGUILibrary if any appeared above!")
            this.logger.warning("")
            this.logger.warning("!!! IMPORTANT !!!")

            this.server.shutdown()
        }
    }

    override fun onEnable() {
        this.manager = GuiManagerImpl(this)
        GuiManager.setInstance(this.manager)

        this.server.pluginManager.registerEvents(InventoryListener(this.manager), this)
        this.server.pluginManager.registerEvents(PlayerListener(this.manager), this)

        this.server.scheduler.runTaskTimer(this, GuiTickerTask(this.manager), 0L,  0L)
    }

    override fun onDisable() {
        // Close all open GUIs. We are not able to save their state.
        for (uuid in this.manager.getOpenGuis().keys) {
            val player = this.server.getPlayer(uuid) ?: continue
            this.manager.closeGui(player)
        }
    }
}