package com.vildaberper.Locker;

import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.vildaberper.Locker.LockerDB;

public class LockerBlockListener extends BlockListener{

	public void onBlockPlace(BlockPlaceEvent event){
		if(Misc.isRedstoneRelated(event.getBlock()) && Config.block_redstone){
			if(Dispenser.touchesDispenser(event.getBlock()) || Dispenser.touchesDispenser(event.getBlockAgainst())){
				if(!Dispenser.canPlace(event.getPlayer(), event.getBlock()) || !Dispenser.canPlace(event.getPlayer(), event.getBlockAgainst())){
					event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_place, event.getBlock()));
					event.setCancelled(true);
					return;
				}
			}
			if(Door.touchesDoor(64, event.getBlock()) || Door.touchesDoor(64, event.getBlockAgainst())){
				if(!Door.canPlace(64, event.getPlayer(), event.getBlock()) || !Door.canPlace(64, event.getPlayer(), event.getBlockAgainst())){
					event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_place, event.getBlock()));
					event.setCancelled(true);
					return;
				}
			}
			if(Door.touchesDoor(71, event.getBlock()) || Door.touchesDoor(71, event.getBlockAgainst())){
				if(!Door.canPlace(71, event.getPlayer(), event.getBlock()) || !Door.canPlace(71, event.getPlayer(), event.getBlockAgainst())){
					event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_place, event.getBlock()));
					event.setCancelled(true);
					return;
				}
			}
		}
		if(Misc.isInteractAble(event.getBlockAgainst())){
			if(LockerDB.getLock(event.getBlock()) != null){
				event.setCancelled(true);
				return;
			}
		}
		if(event.getBlock().getFace(BlockFace.DOWN).getTypeId() == 54 && LockerDB.getLock(event.getBlock().getFace(BlockFace.DOWN)) != null){
			if(!event.getPlayer().getName().equals(LockerDB.getLock(event.getBlock().getFace(BlockFace.DOWN)).getOwner()) && !Perm.hasPermissionSilent(event.getPlayer(), "locker.unlock.other")){
				event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_place, event.getBlock()));
				event.setCancelled(true);
				return;
			}
		}
		if(event.getBlock().getTypeId() == 54){
			if(Chest.isDoubleChest(event.getBlock()) > 2){
				event.setCancelled(true);
				return;
			}
			if(Chest.isDoubleChest(event.getBlock()) == 2){
				if(LockerDB.getLock(Chest.getDoubleChest(event.getBlock())) == null){
					return;
				}
				if(!LockerDB.getLock(Chest.getDoubleChest(event.getBlock())).getOwner().equals(event.getPlayer().getName())){
					if(!Perm.hasPermissionSilent(event.getPlayer(), "locker.lock.other")){
						event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_place, event.getBlock()));
						event.setCancelled(true);
						return;
					}else{
						LockerDB.setLock(
								event.getBlock(),
								new Lock(
										LockerDB.getLock(Chest.getDoubleChest(event.getBlock())).getOwner(),
										LockerDB.getLock(Chest.getDoubleChest(event.getBlock())).getAllowed(),
										LockerDB.getLock(Chest.getDoubleChest(event.getBlock())).getPassword(),
										event.getBlock()
								)
						);
						event.getPlayer().sendMessage(Misc.replaceBlock(Config.lock_one, event.getBlock()));
					}
					return;
				}else{
					if(!Perm.hasPermission(event.getPlayer(), "locker.lock.self")){
						return;
					}
					LockerDB.setLock(
							event.getBlock(),
							new Lock(
									event.getPlayer().getName(),
									LockerDB.getLock(Chest.getDoubleChest(event.getBlock())).getAllowed(),
									LockerDB.getLock(Chest.getDoubleChest(event.getBlock())).getPassword(),
									event.getBlock()
							)
					);
					event.getPlayer().sendMessage(Misc.replaceBlock(Config.lock_one, event.getBlock()));
				}
			}
		}
	}

	public void onBlockBreak(BlockBreakEvent event){
		if(event.isCancelled()){
			return;
		}
		if(LockerDB.getLock(event.getBlock()) != null){
			event.setCancelled(true);
			if(event.getPlayer().getName().equals(LockerDB.getLock(event.getBlock()).getOwner())){
				if(!Perm.hasPermission(event.getPlayer(), "locker.unlock.self")){
					return;
				}
			}
			if(!event.getPlayer().getName().equals(LockerDB.getLock(event.getBlock()).getOwner())){
				if(!Perm.hasPermissionSilent(event.getPlayer(), "locker.unlock.other")){
					event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_break, event.getBlock()));
					return;
				}
			}
			event.setCancelled(false);
			if(event.getBlock().getTypeId() == 64){
				Door.setDoor(64, null, null, null, event.getBlock().getLocation());
				event.getPlayer().sendMessage(Misc.replaceBlock(Config.unlock_one, event.getBlock()));
			}else if(event.getBlock().getTypeId() == 71){
				Door.setDoor(71, null, null, null, event.getBlock().getLocation());
				event.getPlayer().sendMessage(Misc.replaceBlock(Config.unlock_one, event.getBlock()));
			}else{
				LockerDB.setLock(event.getBlock(), null);
				event.getPlayer().sendMessage(Misc.replaceBlock(Config.unlock_one, event.getBlock()));
			}
			return;
		}
		if(LockerDB.getLock(event.getBlock().getFace(BlockFace.UP)) != null){
			event.setCancelled(true);
			if(event.getPlayer().getName().equals(LockerDB.getLock(event.getBlock().getFace(BlockFace.UP)).getOwner())){
				if(!Perm.hasPermission(event.getPlayer(), "locker.unlock.self")){
					return;
				}
			}
			if(!event.getPlayer().getName().equals(LockerDB.getLock(event.getBlock().getFace(BlockFace.UP)).getOwner())){
				if(!Perm.hasPermissionSilent(event.getPlayer(), "locker.unlock.other")){
					event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_break, event.getBlock()));
					return;
				}
			}
			event.setCancelled(false);
			if(event.getBlock().getFace(BlockFace.UP).getTypeId() == 64){
				Door.setDoor(null, null, null, event.getBlock().getFace(BlockFace.UP));
				event.getPlayer().sendMessage(Misc.replaceBlock(Config.unlock_one, event.getBlock().getFace(BlockFace.UP)));
				return;
			}else if(event.getBlock().getFace(BlockFace.UP).getTypeId() == 71){
				Door.setDoor(null, null, null, event.getBlock().getFace(BlockFace.UP));
				event.getPlayer().sendMessage(Misc.replaceBlock(Config.unlock_one, event.getBlock().getFace(BlockFace.UP)));
				return;
			}
		}
	}
}