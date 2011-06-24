package com.vildaberper.Locker;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Chest {
	public static int isDoubleChest(World world, int x, int y, int z){
		int chest = 1;

		if(world.getBlockAt(x + 1, y, z).getTypeId() == 54){
			chest++;
			if(world.getBlockAt(x + 2, y, z).getTypeId() == 54){
				chest++;
			}
			if(world.getBlockAt(x + 1, y, z + 1).getTypeId() == 54){
				chest++;
			}
			if(world.getBlockAt(x + 1, y, z - 1).getTypeId() == 54){
				chest++;
			}
		}
		if(world.getBlockAt(x - 1, y, z).getTypeId() == 54){
			chest++;
			if(world.getBlockAt(x - 2, y, z).getTypeId() == 54){
				chest++;
			}
			if(world.getBlockAt(x - 1, y, z + 1).getTypeId() == 54){
				chest++;
			}
			if(world.getBlockAt(x - 1, y, z - 1).getTypeId() == 54){
				chest++;
			}
		}
		if(world.getBlockAt(x, y, z + 1).getTypeId() == 54){
			chest++;
			if(world.getBlockAt(x + 1, y, z + 1).getTypeId() == 54){
				chest++;
			}
			if(world.getBlockAt(x - 1, y, z + 1).getTypeId() == 54){
				chest++;
			}
			if(world.getBlockAt(x, y, z + 2).getTypeId() == 54){
				chest++;
			}
		}
		if(world.getBlockAt(x, y, z - 1).getTypeId() == 54){
			chest++;
			if(world.getBlockAt(x + 1, y, z - 1).getTypeId() == 54){
				chest++;
			}
			if(world.getBlockAt(x - 1, y, z - 1).getTypeId() == 54){
				chest++;
			}
			if(world.getBlockAt(x, y, z - 2).getTypeId() == 54){
				chest++;
			}
		}
		return chest;
	}

	public static int isDoubleChest(Block block){
		return isDoubleChest(block.getWorld(), block.getX(), block.getY(), block.getZ());
	}

	public static boolean setDoubleChest(String name, String allowed, String password, Location location){
		boolean ok = true;

		if(location.getWorld().getBlockAt(location.getBlockX() + 1, location.getBlockY(), location.getBlockZ()).getTypeId() == 54){
			if(ok){
				ok = false;
			}else{
				return false;
			}
		}
		if(location.getWorld().getBlockAt(location.getBlockX() - 1, location.getBlockY(), location.getBlockZ()).getTypeId() == 54){
			if(ok){
				ok = false;
			}else{
				return false;
			}
		}
		if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() + 1).getTypeId() == 54){
			if(ok){
				ok = false;
			}else{
				return false;
			}
		}
		if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() - 1).getTypeId() == 54){
			if(ok){
				ok = false;
			}else{
				return false;
			}
		}
		if(name == null){
			if(location.getWorld().getBlockAt(location.getBlockX() + 1, location.getBlockY(), location.getBlockZ()).getTypeId() == 54){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX() + 1, location.getBlockY(), location.getBlockZ(), null);
			}
			if(location.getWorld().getBlockAt(location.getBlockX() - 1, location.getBlockY(), location.getBlockZ()).getTypeId() == 54){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX() - 1, location.getBlockY(), location.getBlockZ(), null);
			}
			if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() + 1).getTypeId() == 54){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ() + 1, null);
			}
			if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() - 1).getTypeId() == 54){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ() - 1, null);
			}
			LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), null);
		}else{
			if(location.getWorld().getBlockAt(location.getBlockX() + 1, location.getBlockY(), location.getBlockZ()).getTypeId() == 54){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX() + 1, location.getBlockY(), location.getBlockZ(), new Lock(name, allowed, password, location.getWorld().getName(), location.getBlockX() + 1, location.getBlockY(), location.getBlockZ()));
			}
			if(location.getWorld().getBlockAt(location.getBlockX() - 1, location.getBlockY(), location.getBlockZ()).getTypeId() == 54){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX() - 1, location.getBlockY(), location.getBlockZ(), new Lock(name, allowed, password, location.getWorld().getName(), location.getBlockX() - 1, location.getBlockY(), location.getBlockZ()));
			}
			if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() + 1).getTypeId() == 54){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ() + 1, new Lock(name, allowed, password, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ() + 1));
			}
			if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() - 1).getTypeId() == 54){
				LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ() - 1, new Lock(name, allowed, password, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ() - 1));
			}
			LockerDB.setLock(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), new Lock(name, allowed, password, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
		}
		return true;
	}

	public static boolean setDoubleChest(String owner, String allowed, String password, Block block){
		return setDoubleChest(owner, allowed, password, block.getLocation());
	}

	public static Location getDoubleChest(World world, int x, int y, int z){
		if(world.getBlockAt(x + 1, y, z).getTypeId() == 54){
			return new Location(world, x + 1, y, z);
		}
		if(world.getBlockAt(x - 1, y, z).getTypeId() == 54){
			return new Location(world, x - 1, y, z);
		}
		if(world.getBlockAt(x, y, z + 1).getTypeId() == 54){
			return new Location(world, x, y, z + 1);
		}
		if(world.getBlockAt(x, y, z - 1).getTypeId() == 54){
			return new Location(world, x, y, z - 1);
		}
		return null;
	}

	public static Location getDoubleChest(Block block){
		return getDoubleChest(block.getWorld(), block.getX(), block.getY(), block.getZ());
	}

	public static Block getDoubleChestBlock(Block block){
		if(getDoubleChest(block) == null){
			return null;
		}
		return block.getWorld().getBlockAt(getDoubleChest(block).getBlockX(), getDoubleChest(block).getBlockY(), getDoubleChest(block).getBlockZ());
	}
}