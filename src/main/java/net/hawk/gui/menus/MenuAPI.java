package net.hawk.gui.menus;

import java.util.Iterator;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.hawk.gui.Main;
import net.hawk.gui.util.ItemStackUtils;
import net.hawk.gui.util.Util;

public class MenuAPI implements Listener {
   private static MenuAPI instance;

   public static MenuAPI getInstance() {
      if (instance == null) {
         Class var0 = MenuAPI.class;
         synchronized(MenuAPI.class) {
            if (instance == null) {
               instance = new MenuAPI();
            }
         }
      }

      return instance;
   }

   public void putBorder(Menu menu, int slot) {
      final ItemStack grayPane = Util.createBorder(15);
      MenuItem pane = new MenuItem.UnclickableMenuItem(grayPane) {
         @Override
         public ItemStack getItemStack() {
            return grayPane;
         }
      };
      menu.addMenuItem(pane, slot);
   }


   public MenuItem getBorder() {
      return new MenuItem.UnclickableMenuItem(ItemStackUtils.load(Main.getInstance().getConfig().getConfigurationSection("Options.Border").getValues(true))) {
         @Override
         public ItemStack getItemStack() {
            return ItemStackUtils.load(Main.getInstance().getConfig().getConfigurationSection("Options.Border").getValues(true));
         }
      };
   }


   public int getSize(int highestSlot) {
      if (highestSlot < 10) {
         return 1;
      } else if (highestSlot < 19) {
         return 2;
      } else if (highestSlot < 28) {
         return 3;
      } else if (highestSlot < 37) {
         return 4;
      } else {
         return highestSlot < 46 ? 5 : 6;
      }
   }

   public void putBorder(Menu menu, int x, int y) {
      final ItemStack grayPane = Util.createBorder(15);
      MenuItem pane = new MenuItem.UnclickableMenuItem(grayPane) {
         @Override
         public ItemStack getItemStack() {
            return grayPane;
         }
      };
      menu.addMenuItem(pane, x, y);
   }


   public Menu createMenu(String title, int rows) {
      return new Menu(title, rows);
   }

   public Menu cloneMenu(Menu menu) {
      return menu.clone();
   }

   public void removeMenu(Menu menu) {
      Iterator var2 = menu.getInventory().getViewers().iterator();

      while(var2.hasNext()) {
         HumanEntity viewer = (HumanEntity)var2.next();
         if (viewer instanceof Player) {
            menu.closeMenu((Player)viewer);
         } else {
            viewer.closeInventory();
         }
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onMenuItemClicked(InventoryClickEvent event) {
      Inventory inventory = event.getInventory();
      if (inventory.getHolder() instanceof Menu && inventory.getTitle().equals(((Menu)inventory.getHolder()).title)) {
         event.setCancelled(true);
         ((Player)event.getWhoClicked()).updateInventory();
         switch(event.getAction()) {
         default:
            Menu menu = (Menu)inventory.getHolder();
            if (event.getWhoClicked() instanceof Player) {
               Player player = (Player)event.getWhoClicked();
               if (event.getSlotType() == SlotType.OUTSIDE) {
                  if (menu.exitOnClickOutside()) {
                     menu.closeMenu(player);
                  }
               } else {
                  int index = event.getRawSlot();
                  if (index < inventory.getSize()) {
                     if (event.getAction() != InventoryAction.NOTHING) {
                        menu.selectMenuItem(player, index, InventoryClickType.fromInventoryAction(event.getAction()));
                     }
                  } else if (menu.exitOnClickOutside()) {
                     menu.closeMenu(player);
                  }
               }
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onMenuClosed(InventoryCloseEvent event) {
      if (event.getPlayer() instanceof Player) {
         Inventory inventory = event.getInventory();
         if (inventory.getHolder() instanceof Menu) {
            Menu menu = (Menu)inventory.getHolder();
            MenuAPI.MenuCloseBehaviour menuCloseBehaviour = menu.getMenuCloseBehaviour();
            if (menuCloseBehaviour != null) {
               menuCloseBehaviour.onClose((Player)event.getPlayer(), menu, menu.bypassMenuCloseBehaviour());
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onPlayerLogoutCloseMenu(PlayerQuitEvent event) {
      if (event.getPlayer().getOpenInventory() != null && event.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof Menu) {
         Menu menu = (Menu)event.getPlayer().getOpenInventory().getTopInventory().getHolder();
         menu.setBypassMenuCloseBehaviour(true);
         menu.setMenuCloseBehaviour(null);
         event.getPlayer().closeInventory();
      }
   }

   public interface MenuCloseBehaviour {
      void onClose(Player var1, Menu var2, boolean var3);
   }
}
