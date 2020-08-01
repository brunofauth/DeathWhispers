package ideias.mano.deathWhispers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathWhispers extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
        this.getLogger().info("Hi!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Bye!");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!event.getEntity().hasPermission("deathwhispers.getWhispered"))
            return;

        Location coords = event.getEntity().getLocation();

        TextComponent msg = new TextComponent(
            String.format(
                this.getConfig().getString("on-death-message"),
                coords.getBlockX(),
                coords.getBlockY(),
                coords.getBlockZ()
            )
        );

        msg.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(this.getConfig().getString("on-hover-message")).create()
            )
        );

        msg.setClickEvent(
            new ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                String.format(
                    this.getConfig().getString("on-click-pattern"),
                    coords.getBlockX(),
                    coords.getBlockY(),
                    coords.getBlockZ()
                )
            )
        );

        event.getEntity().spigot().sendMessage(msg);

        if (this.getConfig().getBoolean("log-death-coords"))
            this.getLogger().info(event.getEntity().getName() + "died at" + coords);
    }
}
