package com.connorlinfoot.armorholoapi;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HoloCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command must be ran as a player");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0 || (args.length >= 1 && args[0].equalsIgnoreCase("help"))) {
            showCommandHelp(player);
            return false;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            String name = args[1];
            if (ArmorHoloAPI.currentHolograms.containsKey(name.toLowerCase())) {
                player.sendMessage(ChatColor.RED + "A hologram with that name already exists");
                return false;
            }

            Location location = player.getLocation();
            List<String> text = new ArrayList<String>();
            text.add("I am a hologram called " + name);
            Hologram hologram = new Hologram(name, location, text);
            hologram.create();
            ArmorHoloAPI.currentHolograms.put(name.toLowerCase(), hologram);

            FileConfiguration config = ArmorHoloAPI.plugin.getConfig();
            double locationX = location.getX();
            config.set("Holograms." + name.toLowerCase() + ".X",locationX);
            double locationY = location.getY();
            config.set("Holograms." + name.toLowerCase() + ".Y",locationY);
            double locationZ = location.getZ();
            config.set("Holograms." + name.toLowerCase() + ".Z",locationZ);
            String locationWorld = location.getWorld().getName();
            config.set("Holograms." + name.toLowerCase() + ".World",locationWorld);

            config.set("Holograms." + name.toLowerCase() + ".Text",text);
            ArmorHoloAPI.plugin.saveConfig();
            return true;
        }

        if( args.length == 2 && args[0].equalsIgnoreCase("delete") ){
            String name = args[1];
            if (ArmorHoloAPI.currentHolograms.containsKey(name.toLowerCase())) {
                player.sendMessage(ChatColor.GREEN + "The hologram has been deleted");
                ArmorHoloAPI.currentHolograms.get(name.toLowerCase()).delete();
                ArmorHoloAPI.currentHolograms.remove(name.toLowerCase());
                ArmorHoloAPI.plugin.getConfig().set("Holograms." + name.toLowerCase(),null);
                ArmorHoloAPI.plugin.saveConfig();
                return true;
            }

            player.sendMessage(ChatColor.RED + "No hologram with that name was found");
            return false;
        }

        return true;
    }

    public void showCommandHelp(Player player) {
        player.sendMessage(ChatColor.AQUA + "------ ArmorHoloAPI ------");
        player.sendMessage(ChatColor.GREEN + "/holo create <name> - Creates a hologram at your current location");
        player.sendMessage(ChatColor.GREEN + "/holo delete <name> - Remove a hologram using the name you specify");
        player.sendMessage(ChatColor.GREEN + "/holo addline <name> <text> - Add a line of text to a hologram");
        player.sendMessage(ChatColor.GREEN + "/holo removeline <name> <line> - Remove a line using the line number");
    }

}
