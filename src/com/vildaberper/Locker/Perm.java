package com.vildaberper.Locker;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;

public class Perm{
	public static List<Group> getGroups(String player){
		if(Config.plugin.getServer().getPluginManager().getPlugin("PermissionsBukkit") != null && ((PermissionsPlugin) Config.plugin.getServer().getPluginManager().getPlugin("PermissionsBukkit")).getPlayerInfo(player) != null){
			return ((PermissionsPlugin) Config.plugin.getServer().getPluginManager().getPlugin("PermissionsBukkit")).getPlayerInfo(player).getGroups();
		}
		return null;
	}

	public static List<String> getGroupsString(String player){
		List<Group> groups = getGroups(player);

		if(groups != null){
			List<String> groupsstring = new LinkedList<String>();

			for(Group group : groups){
				groupsstring.add(group.getName());
			}
			return groupsstring;
		}
		return null;
	}

	public static boolean hasPermission(Player player, String node){
		boolean permission = hasPermissionSilent(player, node);

		if(!permission){
			player.sendMessage(Misc.replaceColor(Config.not_permission));
		}
		return permission;
	}

	public static boolean hasPermissionSilent(Player player, String node){
		if(player.hasPermission(node)){
			return true;
		}
		if(player.hasPermission("*")){
			return true;
		}
		for(int y = 0; y < node.length(); y++){
			if(node.charAt(y) == '.'){
				if(player.hasPermission(node.substring(0, y) + ".*")){
					return true;
				}
			}
		}
		return false;
	}
}
