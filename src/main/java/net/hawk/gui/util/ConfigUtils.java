package net.hawk.gui.util;

import org.bukkit.entity.Player;
import net.hawk.gui.Main;

public class ConfigUtils {
   public static ConfigUtils instance;

   public static ConfigUtils getInstance() {
      if (instance == null) {
         instance = new ConfigUtils();
      }

      return instance;
   }

   public void playSound(Player p, String path) {
      String str = "Options.Sound." + path;
      float volume = (float)Main.getInstance().getConfig().getDouble(str + ".volume");
      float pitch = (float)Main.getInstance().getConfig().getDouble(str + ".pitch");
      p.playSound(p.getLocation(), Sounds.valueOf(Main.getInstance().getConfig().getString(str + ".name").toUpperCase()).bukkitSound(), volume, pitch);
   }

   public int getSlot(String path) {
      return Main.getInstance().getConfig().getInt(path);
   }
}
