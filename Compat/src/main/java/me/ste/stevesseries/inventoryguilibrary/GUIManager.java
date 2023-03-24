package me.ste.stevesseries.inventoryguilibrary;

import me.ste.stevesseries.inventoryguilibrary.widget.Widget;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @deprecated For backwards compatibility only.
 */
@Deprecated
public class GUIManager {
    public static final Map<UUID, Widget> GUIS = new AbstractMap<UUID, Widget>() {
        @NotNull
        @Override
        public Set<Entry<UUID, Widget>> entrySet() {
            return PlayerGUIS.INSTANCE.getGUIS().entrySet();
        }
    };

    public static void open(Player player, Widget gui) {
        PlayerGUIS.INSTANCE.setGui(player, gui);
    }

    public static boolean isGuiOpen(Player player) {
        return PlayerGUIS.INSTANCE.getGui(player) != null;
    }

    public static Widget getGui(Player player) {
        return PlayerGUIS.INSTANCE.getGui(player);
    }

    public static void rerenderGui(Player player) {
        PlayerGUIS.INSTANCE.rerenderGui(player);
    }
}
