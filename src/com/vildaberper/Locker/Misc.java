package com.vildaberper.Locker;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class Misc{
	public static boolean isProhibited(Block block){
		if(block.getTypeId() == 12 || block.getTypeId() == 13 || block.getTypeId() == 46 || block.getTypeId() == 81){
			return true;
		}
		return false;
	}

	public static boolean isInteractAble(Block block){
		if(
				block.getTypeId() == 23
				|| block.getTypeId() == 26
				|| block.getTypeId() == 54
				|| block.getTypeId() == 58
				|| block.getTypeId() == 61
				|| block.getTypeId() == 62
				|| block.getTypeId() == 64
				|| block.getTypeId() == 69
				|| block.getTypeId() == 77
				|| block.getTypeId() == 93
				|| block.getTypeId() == 94
		){
			return true;
		}
		return false;
	}

	public static boolean isRedstoneRelated(Block block){
		if(
				block.getTypeId() == 55
				|| block.getTypeId() == 69
				|| block.getTypeId() == 70
				|| block.getTypeId() == 72
				|| block.getTypeId() == 75
				|| block.getTypeId() == 76
				|| block.getTypeId() == 77
				|| block.getTypeId() == 93
				|| block.getTypeId() == 94
		){
			return true;
		}
		return false;
	}

	public static boolean isValidBlock(Block block){
		if(
				block.getTypeId() != 23
				&& block.getTypeId() != 41
				&& block.getTypeId() != 42
				&& block.getTypeId() != 45
				&& block.getTypeId() != 47
				&& block.getTypeId() != 48
				&& block.getTypeId() != 54
				&& block.getTypeId() != 57
				&& block.getTypeId() != 58
				&& block.getTypeId() != 61
				&& block.getTypeId() != 62
				&& block.getTypeId() != 64
				&& block.getTypeId() != 71
				&& block.getTypeId() != 86
				&& block.getTypeId() != 89
				&& block.getTypeId() != 91
		){
			return false;
		}
		return true;
	}

	public static Block getBlockAt(Location location){
		return location.getWorld().getBlockAt(location);
	}

	public static String getBlockName(Block block){
		if(block == null){
			return "Unknown block";
		}
		if(block.getType().name().length() > 1){
			if(block.getTypeId() == 71){
				return "Iron door";
			}
			return block.getType().name().substring(0, 1).toUpperCase() + block.getType().name().substring(1).toLowerCase().replace("_", " ");
		}
		return block.getType().name().substring(0, 1).toUpperCase();
	}

	public static boolean isValidPassword(String string){
		if(string.length() < 1){
			return false;
		}
		for(int i = 0; i < string.length(); i++){
			if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".indexOf(string.charAt(i)) == -1){
				return false;
			}
		}
		return true;
	}

	public static boolean isValidGroup(String group){
		if(group.length() < 1){
			return false;
		}
		return true;
	}

	public static boolean isValidName(String string){
		for(int i = 0; i < string.length(); i++){
			if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".indexOf(string.charAt(i)) == -1){
				return false;
			}
		}
		return true;
	}

	public static boolean isNumeric(String string){
		for(int i = 0; i < string.length(); i++){
			if("0123456789".indexOf(string.charAt(i)) == -1){
				return false;
			}
		}
		return true;
	}

	public static String replaceBlock(String string, Block block){
		return replaceColor(string.replace("<block>", getBlockName(block)));
	}

	public static String replaceColor(String string){
		return string
		.replace("&4", ChatColor.DARK_RED.toString())
		.replace("&c", ChatColor.RED.toString())
		.replace("&6", ChatColor.GOLD.toString())
		.replace("&e", ChatColor.YELLOW.toString())
		.replace("&2", ChatColor.DARK_GREEN.toString())
		.replace("&a", ChatColor.GREEN.toString())
		.replace("&b", ChatColor.AQUA.toString())
		.replace("&3", ChatColor.DARK_AQUA.toString())
		.replace("&1", ChatColor.DARK_BLUE.toString())
		.replace("&9", ChatColor.BLUE.toString())
		.replace("&d", ChatColor.LIGHT_PURPLE.toString())
		.replace("&5", ChatColor.DARK_PURPLE.toString())
		.replace("&f", ChatColor.WHITE.toString())
		.replace("&7", ChatColor.GRAY.toString())
		.replace("&8", ChatColor.DARK_GRAY.toString())
		.replace("&0", ChatColor.BLACK.toString());
	}

	public static String replacePassword(String string, String password){
		return replaceColor(string.replace("<password>", password));
	}

	public static String replaceName(String string, String name){
		return replaceColor(string.replace("<name>", name));
	}

	public static String replaceGroup(String string, String group){
		return replaceColor(string.replace("<group>", group));
	}

	public static String replaceType(String string, Block block){
		return replaceColor(string.replace("<type>", getBlockName(block)));
	}

	public static String replaceOwner(String string, Block block){
		return replaceColor(string.replace("<owner>", LockerDB.getLock(block).getOwner()));
	}

	public static String replaceAllowed(String string, Block block){
		String allowed = "";

		for(int i = 0; i < LockerDB.getLock(block).getAllowedList().toString().substring(1, LockerDB.getLock(block).getAllowedList().toString().length() - 1).replace("g:", "[").replace("G:", "[").split(" ").length; i++){
			if(i != 0){
				allowed += " ";
			}
			allowed += LockerDB.getLock(block).getAllowedList().toString().substring(1, LockerDB.getLock(block).getAllowedList().toString().length() - 1).replace("g:", "[").replace("G:", "[").split(" ")[i];
			if(LockerDB.getLock(block).getAllowedList().toString().substring(1, LockerDB.getLock(block).getAllowedList().toString().length() - 1).replace("g:", "[").replace("G:", "[").split(" ")[i].startsWith("[")){
				allowed += "]";
			}
		}
		return replaceColor(string.replace("<allowed>", allowed.replace(",]", "],")));
	}
}