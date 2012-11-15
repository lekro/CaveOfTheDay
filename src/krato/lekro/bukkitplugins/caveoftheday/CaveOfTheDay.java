package krato.lekro.bukkitplugins.caveoftheday;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CaveOfTheDay extends JavaPlugin {
	Location cotdlocation;
	File cotdfile;
	Map<String, Location> pois;
	//String timestring;
	public void onEnable() {
		if (this.getConfig().getString("world-name") != null) {
			cotdlocation = new Location(
				Bukkit.getServer().getWorld(this.getConfig().getString("world-name")),
				this.getConfig().getDouble("x-coordinate"),
				this.getConfig().getDouble("y-coordinate"),
				this.getConfig().getDouble("z-coordinate"),
				(float) this.getConfig().getDouble("yaw-coordinate"),
				(float) this.getConfig().getDouble("pitch-coordinate")
				); 
		}
		pois = new HashMap<String,Location>();
		pois.put("test1",new Location(Bukkit.getServer().getWorlds().get(0),0d,64d,0d));
		pois.put("test2",new Location(Bukkit.getServer().getWorlds().get(0),20d,64d,20d));
	}
	public void onDisable() {
		
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("cotd")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (cotdlocation != null) {
					player.sendMessage(ChatColor.GRAY+"Teleporting to the Cave of the Day, set by "+this.getConfig().getString("player-set")+" on "+getTimeString()+"...");
					player.teleport(cotdlocation);
				}
				else {
					sender.sendMessage(ChatColor.RED+"The Cave of the Day has not been set! Ask an administrator to set it!");
				}
			}
			else {
				sender.sendMessage("You can't invoke this command through the console :P");
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("cotdset")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				
				cotdlocation = player.getLocation();
				sender.sendMessage(ChatColor.GREEN+"Cave of the Day set!");
				Bukkit.getServer().broadcastMessage(ChatColor.BLUE+player.getDisplayName()+" has set the Cave of the Day! Use /cotd to go there!");
				Calendar now = Calendar.getInstance();
				this.getConfig().set("x-coordinate", new Double(cotdlocation.getX()));
				this.getConfig().set("y-coordinate", new Double(cotdlocation.getY()));
				this.getConfig().set("z-coordinate", new Double(cotdlocation.getZ()));
				this.getConfig().set("yaw-coordinate", new Float(cotdlocation.getYaw()));
				this.getConfig().set("pitch-coordinate", new Float(cotdlocation.getPitch()));
				this.getConfig().set("world-name", cotdlocation.getWorld().getName());
				this.getConfig().set("month-set", new Integer(now.get(Calendar.MONTH)));
				this.getConfig().set("year-set", new Integer(now.get(Calendar.YEAR)));
				this.getConfig().set("date-set", new Integer(now.get(Calendar.DAY_OF_MONTH)));
				this.getConfig().set("hour-set", new Integer(now.get(Calendar.HOUR_OF_DAY)));
				this.getConfig().set("minute-set", new Integer(now.get(Calendar.MINUTE)));
				this.getConfig().set("second-set", new Integer(now.get(Calendar.SECOND)));
				this.getConfig().set("player-set", player.getName());
				this.saveConfig();
				
			}
			else {
				sender.sendMessage("You can't invoke this command through the console :P");
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("poi")) {
			
			if (args.length == 0) {
				listPois(sender);
			}
			else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("tp")) {
					sender.sendMessage("Tell me where to tp to! /poi tp <place>");
				}
				if (args[0].equalsIgnoreCase("set")) {
					sender.sendMessage("Tell me what to set! /poi set <place> [x] [y] [z] [yaw] [pitch]");
				}
				else {
					sender.sendMessage("I have no idea what you are talking about with your first argument of "+args[0]+"!");
				}
			}
			else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("tp")) {
					if (sender instanceof Player) {
						sender.sendMessage("Ok, Teleporting where you want to go...");
						Player player = (Player) sender;
						if (pois.get(args[1]) != null) {
							player.teleport(pois.get(args[1]));
						}
						else {
							sender.sendMessage("That POI doesn't exist!");
						}
					}
				}
				if (args[0].equalsIgnoreCase("set")) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						pois.put(args[1], player.getLocation());
					}
				}
				else {
					sender.sendMessage("I have no idea what you are talking about with your first argument of "+args[0]+"!");
				}
			}
			else {
				sender.sendMessage("Too many (or too less...) arguments!!");
			}
			return true;
		}
		else {
			return false;
		}
		
		
	}
	private String getTimeString() {
		return this.getConfig().getInt("month-set")+"/"+this.getConfig().getInt("date-set")+"/"+this.getConfig().getInt("year-set")+" "+this.getConfig().getInt("hour-set")+":"+this.getConfig().getInt("minute-set")+":"+this.getConfig().getInt("second-set");
	}
	private void listPois(CommandSender sender) {
		for (String poiString : pois.keySet()) {
			sender.sendMessage(ChatColor.AQUA+poiString+" at ("+pois.get(poiString).getBlockX()+", "+pois.get(poiString).getBlockY()+", "+pois.get(poiString).getBlockZ()+")"+ChatColor.RESET);
		}
	}
}
