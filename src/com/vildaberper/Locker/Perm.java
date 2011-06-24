package com.vildaberper.Locker;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import com.nijiko.permissions.PermissionHandler;

public class Perm {
	public static PermissionHandler PermissionsHandler = null;
	public static List<String> Permissions = new LinkedList<String>();

	public static void clearPermissions(){
		Permissions.clear();
	}

	public static void resetPermissions(){
		Permissions.clear();
		Permissions.add("locker.lockinfo");
		Permissions.add("locker.lock.self");
		Permissions.add("locker.unlock.self");
		Permissions.add("locker.unlock.password");
		Permissions.add("locker.view.self");
	}

	public static List<String> getPermissions(){
		return Permissions;
	}

	public static void setPermissions(List<String> permissions){
		Permissions = permissions;
	}

	public static String getPermission(int index){
		return Permissions.get(index);
	}

	public static void setPermission(int index, String permission){
		Permissions.add(index, permission);
	}

	public static void setPermission(String permission){
		Permissions.add(permission);
	}

	public static int getSize(){
		return Permissions.size();
	}

	public static boolean hasPermission(Player player, String node){
		if(Config.op_permissions){
			if(player.isOp()){
				return true;
			}
			for(int i = 0; i < getSize(); i++){
				if(getPermission(i).equalsIgnoreCase(node)){
					return true;
				}
			}
			player.sendMessage(Misc.replaceColor(Config.not_permission));
			return false;
		}
		if(!PermissionsHandler.has(player, node)){
			player.sendMessage(Misc.replaceColor(Config.not_permission));
			return false;
		}
		return true;
	}

	public static boolean hasPermissionSilent(Player player, String node){
		if(Config.op_permissions){
			if(player.isOp()){
				return true;
			}
			for(int i = 0; i < getSize(); i++){
				if(getPermission(i).equalsIgnoreCase(node)){
					return true;
				}
			}
			return false;
		}
		if(!PermissionsHandler.has(player, node)){
			return false;
		}
		return true;
	}
}