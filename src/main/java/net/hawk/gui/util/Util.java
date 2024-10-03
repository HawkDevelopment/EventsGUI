package net.hawk.gui.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Util {
   public static ItemStack createItem(Material mat, int amt, int durability, String name, List<String> lore) {
      ItemStack item = new ItemStack(mat, amt);
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(name);
      meta.setLore(lore);
      if (durability != 0) {
         item.setDurability((short)durability);
      }
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createItem(Material mat, int amt, String name, List<String> lore) {
      return Util.createItem(mat, amt, 0, name, lore);
   }

   public static ItemStack createItem(Material mat, int amt, int durability, String name, String ... lore) {
      ArrayList<String> l = new ArrayList<String>();
      String[] arrayOfString = lore;
      int j = lore.length;
      for (int i = 0; i < j; ++i) {
         String s = arrayOfString[i];
         l.add(s);
      }
      return Util.createItem(mat, amt, durability, name, l);
   }

   public static ItemStack createBorder(int color) {
      return Util.createItem(Material.STAINED_GLASS_PANE, 1, color, Util.color("&c&l&1&c&k&r"), new String[0]);
   }

   public static String color(String s) {
      return ChatColor.translateAlternateColorCodes('&', s);
   }
}
