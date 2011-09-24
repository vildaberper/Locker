package com.vildaberper.Locker;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Door{
	public static boolean setDoor(int id, String name, String allowed, String password, Location location){
		if(name == null || allowed == null || password == null){
			if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()).getTypeId() == id){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ(), null);
			}
			if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()).getTypeId() == id){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ(), null);
			}
			LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), null);
		}else{
			if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()).getTypeId() == id){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ(), new Lock(name, allowed, password, location.getWorld().getName(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()));
			}
			if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()).getTypeId() == id){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ(), new Lock(name, allowed, password, location.getWorld().getName(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()));
			}
			LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), new Lock(name, allowed, password, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
		}
		return true;
	}

	public static boolean setDoor(String name, String allowed, String password, Block block){
		return setDoor(block.getTypeId(), name, allowed, password, block.getLocation());
	}

	public static Location getUnderBlock(int id, World world, int x, int y, int z){
		if(world.getBlockAt(x, y - 1, z).getTypeId() == id){
			return new Location(world, x, y - 2, z);
		}
		return new Location(world, x, y - 1, z);
	}

	public static Location getUnderBlock(Block block){
		return getUnderBlock(block.getTypeId(), block.getWorld(), block.getX(), block.getY(), block.getZ());
	}

	public static Location getDoubleDoor(int id, World world, int x, int y, int z){
		if(world.getBlockAt(x, y + 1, z).getTypeId() == id){
			return new Location(world, x, y + 1, z);
		}
		if(world.getBlockAt(x, y - 1, z).getTypeId() == id){
			return new Location(world, x, y - 1, z);
		}
		return null;
	}

	public static Location getDoubleDoor(int id, Location location){
		return getDoubleDoor(id, location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	public static Block getDoubleDoorBlock(int id, Location location){
		if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()).getTypeId() == id){
			return location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ());
		}
		if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()).getTypeId() == id){
			return location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
		}
		return null;
	}

	public static boolean touchesDoor(int id, Block block){
		for(int i1 = 0; i1 < BlockFace.values().length; i1++){
			if(block.getRelative(BlockFace.values()[i1]).getTypeId() == id){
				return true;
			}
			for(int i2 = 0; i2 < BlockFace.values().length; i2++){
				if(block.getRelative(BlockFace.values()[i1]).getRelative(BlockFace.values()[i2]).getTypeId() == id){
					return true;
				}
			}
		}
		return false;
	}

	public static boolean canPlace(int id, Player player, Block block){
		if(Perm.hasPermissionSilent(player, "locker.unlock.other")){
			return true;
		}
		for(int i1 = 0; i1 < BlockFace.values().length; i1++){
			if(block.getRelative(BlockFace.values()[i1]).getTypeId() == id){
				if(LockerDB.getLock(block.getRelative(BlockFace.values()[i1])) != null){
					if(!LockerDB.getLock(block.getRelative(BlockFace.values()[i1])).getOwner().equals(player.getName())){
						return false;
					}
				}
			}
			for(int i2 = 0; i2 < BlockFace.values().length; i2++){
				if(block.getRelative(BlockFace.values()[i1]).getRelative(BlockFace.values()[i2]).getTypeId() == id){
					if(LockerDB.getLock(block.getRelative(BlockFace.values()[i1]).getRelative(BlockFace.values()[i2])) != null){
						if(!LockerDB.getLock(block.getRelative(BlockFace.values()[i1]).getRelative(BlockFace.values()[i2])).getOwner().equals(player.getName())){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}