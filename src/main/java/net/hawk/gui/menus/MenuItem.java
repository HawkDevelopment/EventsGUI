package net.hawk.gui.menus;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class MenuItem {
   private Menu menu;
   private int slot;
   private String event;
   private ItemStack icon;

   public MenuItem(String event, ItemStack icon) {
      this.event = event;
      this.icon = icon;
   }

   protected void addToMenu(Menu menu) {
      this.menu = menu;
   }

   protected void removeFromMenu(Menu menu) {
      if (this.menu == menu) {
         this.menu = null;
      }
   }

   public Menu getMenu() {
      return this.menu;
   }

   public int getSlot() {
      return this.slot;
   }

   public void setSlot(int slot) {
      this.slot = slot;
   }

   public abstract void onClick(Player player, InventoryClickType clickType);

   public ItemStack getItemStack() {
      return this.icon;
   }

   public abstract static class UnclickableMenuItem extends MenuItem {
      public UnclickableMenuItem(ItemStack icon) {
         super(null, icon);
      }

      @Override
      public void onClick(Player player, InventoryClickType clickType) {
      }
   }
}
