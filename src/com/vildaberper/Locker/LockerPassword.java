package com.vildaberper.Locker;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class LockerPassword{
	static List<Password> P = new LinkedList<Password>();

	public static boolean isUnlockedBy(String name, Block block){
		for(int i = 0; i < P.size(); i++){
			if(P.get(i).getName().equals(name)){
				for(int u = 0; u < P.get(i).getUnlocked().size(); u++){
					if(P.get(i).getUnlocked().get(u).equals(block)){
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}

	public static Password getPassword(String name){
		for(int i = 0; i < P.size(); i++){
			if(P.get(i).getName().equals(name)){
				return P.get(i);
			}
		}
		return null;
	}

	public static void removeBlock(int x, int y, int z){
		for(int i = 0; i < P.size(); i++){
			for(int u = 0; u < P.get(i).getUnlocked().size(); u++){
				if(P.get(i).getUnlocked().get(u).getX() == x &&P.get(i).getUnlocked().get(u).getY() == y && P.get(i).getUnlocked().get(u).getZ() == z){
					P.get(i).getUnlocked().remove(u);
					u--;
				}
			}
		}
	}

	public static void removeBlock(Location location){
		removeBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	public static void removeBlock(Block block){
		removeBlock(block.getX(), block.getY(), block.getZ());
	}

	public static void removeName(String name){
		for(int i = 0; i < P.size(); i++){
			if(P.get(i).getName().equals(name)){
				P.remove(i);
				return;
			}
		}
	}

	public static void addName(String name){
		P.add(new Password(name, new LinkedList<Block>()));
	}
}