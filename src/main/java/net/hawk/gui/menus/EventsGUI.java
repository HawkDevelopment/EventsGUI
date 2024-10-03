package net.hawk.gui.menus;

import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.hawk.gui.Main;
import net.hawk.gui.util.ConfigUtils;
import net.hawk.gui.util.ItemStackUtils;
import net.hawk.gui.util.Util;

public class EventsGUI {
   private final Player player;
   private Menu eventsMenu;
   private Integer inventorySize;

   public EventsGUI(Player player) {
      this.player = player;
      this.init();
   }

   private void init() {
      List<MenuItem> events = this.getEvents();
      this.inventorySize = MenuAPI.getInstance().getSize(Main.getInstance().getConfig()
              .getConfigurationSection("Events")
              .getKeys(false).stream()
              .map(event -> Main.getInstance().getConfig().getInt("Events." + event + ".slot"))
              .mapToInt(Integer::intValue)
              .max().orElse(0));

      this.eventsMenu = new Menu(Util.color(Main.getInstance().getConfig()
              .getString("Options.MainMenu.title")
              .replaceAll("%amount%", NumberFormat.getInstance().format(events.size()))), this.inventorySize);

      events.forEach(event -> this.eventsMenu.addMenuItem(event, event.getSlot()));

      for (int free = 0; free < this.eventsMenu.getInventory().getSize(); ++free) {
         if (this.eventsMenu.getInventory().getItem(free) != null) continue;
         this.eventsMenu.addMenuItem(MenuAPI.getInstance().getBorder(), free);
      }

      this.eventsMenu.setMenuCloseBehaviour(new MenuAPI.MenuCloseBehaviour() {
         @Override
         public void onClose(Player p0, Menu p1, boolean p2) {
            if (!p2) {
               ConfigUtils.getInstance().playSound(EventsGUI.this.player, "mainMenuClose");
            }
         }
      });
   }

   public void open() {
      this.eventsMenu.openMenu(this.player);
      ConfigUtils.getInstance().playSound(this.player, "mainMenuOpen");
   }

   private List<MenuItem> getEvents() {
      return Main.getInstance().getConfig().getConfigurationSection("Events")
              .getKeys(false).stream().map(event -> {
                 MenuItem item;
                 final ItemStack icon = ItemStackUtils.load(Main.getInstance().getConfig()
                         .getConfigurationSection("Events." + event)
                         .getValues(true));
                 if (icon != null && icon.hasItemMeta()) {
                    ItemMeta meta = icon.getItemMeta();
                    if (meta.hasDisplayName()) {
                       meta.setDisplayName(meta.getDisplayName().replaceAll("%name%", event));
                    }
                    icon.setItemMeta(meta);
                 }
                 if (!Main.getInstance().getConfig().contains("Events." + event + ".command")
                         || Main.getInstance().getConfig().getString("Events." + event + ".command").isEmpty()) {

                    item = new MenuItem.UnclickableMenuItem(icon) {
                       @Override
                       public ItemStack getItemStack() {
                          return icon;
                       }
                    };
                    item.setSlot(Main.getInstance().getConfig().getInt("Events." + event + ".slot"));

                    return item;
                 }
                 item = new MenuItem(event, icon) {
                    final String eventName = event;
                    final ItemStack itemIcon = icon;

                    @Override
                    public void onClick(Player p0, InventoryClickType p1) {
                       this.getMenu().setBypassMenuCloseBehaviour(true);
                       this.getMenu().closeMenu(p0);
                       p0.chat(Main.getInstance().getConfig().getString("Events." + this.eventName + ".command"));
                       ConfigUtils.getInstance().playSound(p0, "clickEvent");
                       this.getMenu().setBypassMenuCloseBehaviour(false);
                    }

                    @Override
                    public ItemStack getItemStack() {
                       return this.itemIcon;
                    }
                 };
                 item.setSlot(Main.getInstance().getConfig().getInt("Events." + event + ".slot"));
                 return item;
              }).collect(Collectors.toList());
   }
}
