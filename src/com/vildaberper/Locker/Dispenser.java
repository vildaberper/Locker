package com.vildaberper.Locker;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Dispenser{
	public static boolean touchesDispenser(Block block){
		for(int i1 = 0; i1 < BlockFace.values().length; i1++){
			if(block.getFace(BlockFace.values()[i1]).getTypeId() == 23){
				return true;
			}
			for(int i2 = 0; i2 < BlockFace.values().length; i2++){
				if(block.getFace(BlockFace.values()[i1]).getFace(BlockFace.values()[i2]).getTypeId() == 23){
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean canPlace(Player player, Block block){
		if(Perm.hasPermissionSilent(player, "locker.unlock.other")){
			return true;
		}
		for(int i1 = 0; i1 < BlockFace.values().length; i1++){
			if(block.getFace(BlockFace.values()[i1]).getTypeId() == 23){
				if(LockerDB.getLock(block.getFace(BlockFace.values()[i1])) != null){
					if(!LockerDB.getLock(block.getFace(BlockFace.values()[i1])).getOwner().equals(player.getName())){
						return false;
					}
				}
			}
			for(int i2 = 0; i2 < BlockFace.values().length; i2++){
				if(block.getFace(BlockFace.values()[i1]).getFace(BlockFace.values()[i2]).getTypeId() == 23){
					if(LockerDB.getLock(block.getFace(BlockFace.values()[i1]).getFace(BlockFace.values()[i2])) != null){
						if(!LockerDB.getLock(block.getFace(BlockFace.values()[i1]).getFace(BlockFace.values()[i2])).getOwner().equals(player.getName())){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}