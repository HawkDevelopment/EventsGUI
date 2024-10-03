package net.hawk.gui.menus;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import net.hawk.gui.Main;

public class Menu implements InventoryHolder {
   protected ConcurrentMap<Integer, MenuItem> items;
   private Inventory inventory;
   protected String title;
   protected int rows;
   protected boolean exitOnClickOutside;
   protected MenuAPI.MenuCloseBehaviour menuCloseBehaviour;
   protected boolean bypassMenuCloseBehaviour;
   protected Menu parentMenu;

   public Menu(String title, int rows) {
      this(title, rows, null);
   }

   public Menu(String title, int rows, Menu parentMenu) {
      this.items = Maps.newConcurrentMap();
      this.exitOnClickOutside = false;
      this.bypassMenuCloseBehaviour = false;
      this.title = title;
      this.rows = rows;
      this.parentMenu = parentMenu;
   }

   public void setMenuCloseBehaviour(MenuAPI.MenuCloseBehaviour menuCloseBehaviour) {
      this.menuCloseBehaviour = menuCloseBehaviour;
   }

   public MenuAPI.MenuCloseBehaviour getMenuCloseBehaviour() {
      return this.menuCloseBehaviour;
   }

   public void setBypassMenuCloseBehaviour(boolean bypassMenuCloseBehaviour) {
      this.bypassMenuCloseBehaviour = bypassMenuCloseBehaviour;
   }

   public boolean bypassMenuCloseBehaviour() {
      return this.bypassMenuCloseBehaviour;
   }

   public void setExitOnClickOutside(boolean exit) {
      this.exitOnClickOutside = exit;
   }

   public Map<Integer, MenuItem> getMenuItems() {
      return this.items;
   }

   public boolean addMenuItem(MenuItem item, int x, int y) {
      return this.addMenuItem(item, y * 9 + x);
   }

   public MenuItem getMenuItem(int index) {
      return this.items.get(index);
   }

   public boolean addMenuItem(MenuItem item, int index) {
      ItemStack slot = this.getInventory().getItem(index);
      if (slot != null && slot.getType() != Material.AIR) {
         this.removeMenuItem(index);
      }

      item.setSlot(index);
      this.getInventory().setItem(index, item.getItemStack());
      this.items.put(index, item);
      item.addToMenu(this);
      return true;
   }

   public boolean removeMenuItem(int x, int y) {
      return this.removeMenuItem(y * 9 + x);
   }

   public boolean removeMenuItem(int index) {
      ItemStack slot = this.getInventory().getItem(index);
      if (slot != null && !slot.getType().equals(Material.AIR)) {
         this.getInventory().clear(index);
         this.items.remove(index).removeFromMenu(this);
         return true;
      } else {
         return false;
      }
   }

   protected void selectMenuItem(Player player, int index, InventoryClickType clickType) {
      if (this.items.containsKey(index)) {
         MenuItem item = this.items.get(index);
         item.onClick(player, clickType);
      }

   }

   public void openMenu(Player player) {
      if (!this.getInventory().getViewers().contains(player)) {
         player.openInventory(this.getInventory());
      }

   }

   public void closeMenu(Player player) {
      if (this.getInventory().getViewers().contains(player)) {
         this.getInventory().getViewers().remove(player);
         player.closeInventory();
      }

   }

   public void scheduleUpdateTask(final Player player, int ticks) {
      (new BukkitRunnable() {
         public void run() {
            if (player != null && Bukkit.getPlayer(player.getName()) != null) {
               if (player.getOpenInventory() != null && player.getOpenInventory().getTopInventory() != null && player.getOpenInventory().getTopInventory().getHolder() != null) {
                  if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof Menu)) {
                     this.cancel();
                  } else {
                     Menu menu = (Menu)player.getOpenInventory().getTopInventory().getHolder();
                     if (!menu.inventory.equals(Menu.this.inventory)) {
                        this.cancel();
                     } else {
                        Iterator var2 = menu.items.entrySet().iterator();

                        while(var2.hasNext()) {
                           Entry<Integer, MenuItem> entry = (Entry)var2.next();
                           Menu.this.getInventory().setItem(entry.getKey(), entry.getValue().getItemStack());
                        }

                     }
                  }
               } else {
                  this.cancel();
               }
            } else {
               this.cancel();
            }
         }
      }).runTaskTimer(Main.getInstance(), ticks, ticks);
   }

   public Menu getParent() {
      return this.parentMenu;
   }

   public void setParent(Menu menu) {
      this.parentMenu = menu;
   }

   public Inventory getInventory() {
      if (this.inventory == null) {
         this.inventory = Bukkit.createInventory(this, this.rows * 9, this.title);
      }

      return this.inventory;
   }

   public boolean exitOnClickOutside() {
      return this.exitOnClickOutside;
   }

   protected Menu clone() {
      Menu clone = new Menu(this.title, this.rows);
      clone.setExitOnClickOutside(this.exitOnClickOutside);
      clone.setMenuCloseBehaviour(this.menuCloseBehaviour);
      Iterator var2 = this.items.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<Integer, MenuItem> entry = (Entry)var2.next();
         clone.addMenuItem(entry.getValue(), entry.getKey());
      }

      return clone;
   }

   public void updateMenu() {
      Iterator var1 = this.getInventory().getViewers().iterator();

      while(var1.hasNext()) {
         HumanEntity entity = (HumanEntity)var1.next();
         ((Player)entity).updateInventory();
      }

   }
}
