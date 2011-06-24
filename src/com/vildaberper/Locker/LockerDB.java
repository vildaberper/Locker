package com.vildaberper.Locker;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class LockerDB {
	public static Lock getLock(String world, int x, int y, int z){
		return Config.plugin.getDatabase().find(Lock.class).where().ieq("world", world).eq("x", x).eq("y", y).eq("z", z).findUnique();
	}

	public static Lock getLock(Block block){
		return getLock(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
	}

	public static Lock getLock(Location location){
		return getLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	public static void setLock(String world, int x, int y, int z, Lock lock){
		if(getLock(world, x, y, z) != null){
			Config.plugin.getDatabase().delete(getLock(world, x, y, z));
		}
		if(lock == null){
			return;
		}
		Config.plugin.getDatabase().save(lock);
	}

	public static void setLock(Block block, Lock lock){
		setLock(block.getWorld().getName(), block.getX(), block.getY(), block.getZ(), lock);
	}

	public static void setLock(Location location, Lock lock){
		setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), lock);
	}

	public static List<Lock> getBlocksBy(String owner){
		return Config.plugin.getDatabase().find(Lock.class).where().ieq("owner", owner).findList();
	}

	public static List<String> getOwners(){
		boolean add = true;
		List<String> owners = new LinkedList<String>();

		for(int i = 0; i  < Config.plugin.getDatabase().find(Lock.class).findList().size(); i++){
			add = true;
			for(int u = 0; u < owners.size(); u++){
				if(Config.plugin.getDatabase().find(Lock.class).findList().get(i).getOwner().equals(owners.get(u))){
					add = false;
				}
			}
			if(add){
				owners.add(Config.plugin.getDatabase().find(Lock.class).findList().get(i).getOwner());
			}
		}
		return owners;
	}
	
	public static int getSize(){
		return Config.plugin.getDatabase().find(Lock.class).findRowCount();
	}
}