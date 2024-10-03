package net.hawk.gui.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import net.hawk.gui.Main;

public class Message {
   public static void sendMessage(CommandSender p, Message.MessagePart str) {
      if (!str.getPart().isEmpty()) {
         p.sendMessage(Main.getInstance().getPrefix() + str.getPart());
      }
   }

   public static void sendMessage(CommandSender p, String str) {
      Message.MessagePart strPart = generate(str);
      if (!strPart.getPart().isEmpty()) {
         p.sendMessage(Main.getInstance().getPrefix() + strPart.getPart());
      }
   }

   public static Message.MessagePart generate(String str) {
      return new Message.MessagePart(str);
   }

   public static class MessagePart {
      public String str;

      public MessagePart(String str) {
         this.str = Main.getInstance().getMessages().isString(str.toUpperCase()) ? ChatColor.translateAlternateColorCodes('&', Main.getInstance().getMessages().getString(str.toUpperCase())) : "";
      }

      public Message.MessagePart replace(Object placeholder, Object key) {
         this.str = this.str.replace(placeholder.toString(), key.toString());
         return this;
      }

      public String getPart() {
         return this.str;
      }
   }
}
