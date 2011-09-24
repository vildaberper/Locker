package com.vildaberper.Locker;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

public class LockerEntityListener extends EntityListener{
	public void onEntityExplode(EntityExplodeEvent event){
		if(event.isCancelled()){
			return;
		}
		for(Block block:event.blockList()){
			if(LockerDB.getLock(block) != null){
				if(Config.block_explosions){
					event.setCancelled(true);
					return;
				}else{
					LockerDB.setLock(block, null);
				}
			}
			if(LockerDB.getLock(block.getRelative(BlockFace.UP)) != null && block.getRelative(BlockFace.UP).getTypeId() == 64 || block.getRelative(BlockFace.UP).getTypeId() == 71){
				if(Config.block_explosions){
					event.setCancelled(true);
					return;
				}else{
					Door.setDoor(block.getRelative(BlockFace.UP).getTypeId(), null, null, null, block.getRelative(BlockFace.UP).getLocation());
				}
			}
		}
	}
}