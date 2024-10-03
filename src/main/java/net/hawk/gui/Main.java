package net.hawk.gui;

import com.earth2me.essentials.Essentials;
import java.io.File;

import net.hawk.gui.commands.EventsCommand;
import net.hawk.gui.menus.MenuAPI;
import net.hawk.gui.util.EnchantGlow;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private Essentials ess;
    private File messages;
    private FileConfiguration messagesConfig;

    public void onEnable() {
        this.setup();
        this.ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new MenuAPI(), this);
        this.getCommand("events").setExecutor(new EventsCommand());
    }

    private void setup() {
        try {
            this.saveDefaultConfig();
            this.messages = new File(this.getDataFolder(), "messages.yml");
            if (!this.messages.exists()) {
                this.messages.createNewFile();
                this.saveResource("messages.yml", true);
            }
            this.messagesConfig = YamlConfiguration.loadConfiguration(this.messages);
        }
        catch (Exception exception) {
        }
    }

    public void onLoad() {
        EnchantGlow.getGlow();
    }

    public Essentials getEss() {
        return this.ess;
    }

    public FileConfiguration getMessages() {
        return this.messagesConfig;
    }

    public void saveMessages() {
        try {
            this.messagesConfig.save(this.messages);
        }
        catch (Exception exception) {
        }
    }

    public void reloadMessages() {
        try {
            this.messagesConfig.load(this.messages);
            this.messagesConfig.save(this.messages);
        }
        catch (Exception exception) {
        }
    }

    public String getPrefix() {
        return String.valueOf(ChatColor.translateAlternateColorCodes('&', this.messagesConfig.getString("Prefix"))) + " ";
    }

    public static Main getInstance() {
        return JavaPlugin.getPlugin(Main.class);
    }
}
