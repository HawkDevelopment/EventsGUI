package net.hawk.gui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import net.hawk.gui.Main;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemStackUtils {
   private static List<String> replaceColors(List<String> list) {
      ArrayList<String> listTemp = new ArrayList<String>();
      for (String s : list) {
         s = ChatColor.translateAlternateColorCodes('&', s);
         listTemp.add(s);
      }
      return listTemp;
   }

   public static ItemStack load(Map<String, Object> keys) {
      try {
         boolean enchanted;
         ItemStack stack = null;
         String item = "";
         if (keys.containsKey("material")) {
            if (keys.get("material") instanceof List) {
               List list = (List)keys.get("material");
               item = (String)list.get(keys.containsKey("index") ? ((Integer)keys.get("index")).intValue() : ThreadLocalRandom.current().nextInt(list.size()));
            } else {
               item = keys.get("material").toString();
            }
         }
         stack = keys.containsKey("material") && keys.containsKey("amount") ? Main.getInstance().getEss().getItemDb().get(item, Integer.parseInt(keys.get("amount").toString())) : Main.getInstance().getEss().getItemDb().get(item, 1);
         ItemMeta meta = stack.getItemMeta();
         if (keys.containsKey("name")) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', keys.get("name").toString()));
         }
         if (keys.containsKey("playerhead")) {
            ((SkullMeta) meta).setOwner(keys.get("playerhead").toString());
         }
         if (keys.containsKey("lore")) {
            List<String> lore = ItemStackUtils.replaceColors((List)keys.get("lore"));
            meta.setLore(lore);
         }
         if (keys.containsKey("enchants")) {
            List<String> enchants = (List)keys.get("enchants");
            for (String s : enchants) {
               String[] parts = s.split(":");
               if (EnchantUtils.argsToEnchant(parts[0]) == null) continue;
               if (meta instanceof EnchantmentStorageMeta) {
                  ((EnchantmentStorageMeta) meta).addStoredEnchant(EnchantUtils.argsToEnchant(parts[0]), Integer.parseInt(parts[1]), true);
                  continue;
               }
               meta.addEnchant(EnchantUtils.argsToEnchant(parts[0]), Integer.parseInt(parts[1]), true);
            }
         }
         stack.setItemMeta(meta);
         if (keys.containsKey("enchanted") && (enchanted = Boolean.valueOf(keys.get("enchanted").toString()).booleanValue())) {
            EnchantGlow.addGlow(stack);
         }
         return stack;
      }
      catch (Exception ignore) {
         Main.getInstance().getLogger().severe(ChatColor.stripColor(ignore.getMessage()));
         return null;
      }
   }

   public static boolean isSimilar(ItemStack item, ItemStack compare) {
      if (item == null || compare == null) {
         return false;
      }
      if (item == compare) {
         return true;
      }
      if (item.getTypeId() != compare.getTypeId()) {
         return false;
      }
      if (item.getDurability() != compare.getDurability()) {
         return false;
      }
      if (item.hasItemMeta() != compare.hasItemMeta()) {
         return false;
      }
      if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
         if (item.getItemMeta().hasDisplayName() != compare.getItemMeta().hasDisplayName()) {
            return false;
         }
         if (!item.getItemMeta().getDisplayName().equals(compare.getItemMeta().getDisplayName())) {
            return false;
         }
      }
      return true;
   }
}
