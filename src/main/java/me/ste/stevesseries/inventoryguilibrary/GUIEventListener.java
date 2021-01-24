package me.ste.stevesseries.inventoryguilibrary;

import me.ste.stevesseries.inventoryguilibrary.inventory.GridInventory;
import me.ste.stevesseries.inventoryguilibrary.widget.Widget;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class GUIEventListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Widget gui = GUIManager.getGui(player);
        if(gui != null) {
            GridInventory grid = new GridInventory(player.getOpenInventory().getTopInventory(), gui.getWidth(), gui.getHeight());
            gui.recurse(widget -> widget.onDestruction(grid));
            GUIManager.GUIS.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity entity = event.getPlayer();
        if(entity instanceof Player) {
            Player player = (Player) entity;
            Widget gui = GUIManager.getGui(player);
            if(gui != null) {
                GridInventory grid = new GridInventory(player.getOpenInventory().getTopInventory(), gui.getWidth(), gui.getHeight());
                gui.recurse(widget -> widget.onDestruction(grid));
                GUIManager.GUIS.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        if(entity instanceof Player) {
            Player player = (Player) entity;
            Widget gui = GUIManager.getGui(player);
            if(gui != null && (!(event.getClickedInventory() instanceof PlayerInventory) || event.isShiftClick())) {
                int[] position = GridInventory.getPositionByIndex(event.getSlot(), gui.getWidth());
                event.setCancelled(true);
                ClickType click = event.getClick();
                Bukkit.getScheduler().runTask(InventoryGUILibrary.getPlugin(InventoryGUILibrary.class), () -> gui.recurse(widget -> {
                    GridInventory grid = new GridInventory(player.getOpenInventory().getTopInventory(), gui.getWidth(), gui.getHeight());
                    if (GridInventory.isInside(position[0], position[1], widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight())) {
                        if(click == ClickType.NUMBER_KEY) {
                            widget.onNumberKey(position[0], position[1], event.getHotbarButton(), grid);
                        } else {
                            widget.onClick(position[0], position[1], click, grid);
                        }
                    }
                }));
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        HumanEntity entity = event.getWhoClicked();
        if(entity instanceof Player) {
            Player player = (Player) entity;
            Widget gui = GUIManager.getGui(player);
            if(gui != null) {
                GridInventory grid = new GridInventory(player.getOpenInventory().getTopInventory(), gui.getWidth(), gui.getHeight());
                for(Map.Entry<Integer, ItemStack> entry : event.getNewItems().entrySet()) {
                    if(entry.getKey() < grid.getHandle().getSize() - 1) {
                        event.setCancelled(true);
                        Bukkit.getScheduler().runTask(InventoryGUILibrary.getPlugin(InventoryGUILibrary.class), () -> gui.recurse(widget -> {
                            Map<int[], ItemStack> newItems = new HashMap<>();
                            for(Map.Entry<Integer, ItemStack> entry2 : event.getNewItems().entrySet()) {
                                int[] position = GridInventory.getPositionByIndex(entry2.getKey(), gui.getWidth());
                                if(GridInventory.isInside(position[0], position[1], widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight())) {
                                    newItems.put(position, entry2.getValue());
                                }
                            }
                            if(newItems.size() > 0) {
                                widget.onDrag(newItems, event.getType(), event.getCursor(), grid);
                            }
                        }));
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Bukkit.getScheduler().runTask(InventoryGUILibrary.getPlugin(InventoryGUILibrary.class), () -> {
            GUIManager.open(event.getPlayer(), new TestGUI());
        });
    }
}