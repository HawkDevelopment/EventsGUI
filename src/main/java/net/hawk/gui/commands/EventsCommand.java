package net.hawk.gui.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.hawk.gui.menus.EventsGUI;

public class EventsCommand implements CommandExecutor {
   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player)) {
         return false;
      } else if (!cmd.getName().equalsIgnoreCase("events")) {
         return false;
      } else {
         EventsGUI eventsGUI = new EventsGUI((Player)sender);
         eventsGUI.open();
         return true;
      }
   }
}
