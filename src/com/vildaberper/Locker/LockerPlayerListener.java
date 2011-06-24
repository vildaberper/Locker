package com.vildaberper.Locker;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LockerPlayerListener extends PlayerListener{
	public void onPlayerQuit(PlayerQuitEvent event){
		LockerAction.setAction(event.getPlayer().getName(), null);
		LockerPassword.removeName(event.getPlayer().getName());
	}

	public void onPlayerJoin(PlayerJoinEvent event){
		LockerPassword.addName(event.getPlayer().getName());
	}

	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.isCancelled()){
			return;
		}
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(LockerDB.getLock(event.getClickedBlock()) != null){
				if(!Misc.isValidBlock(event.getClickedBlock())){
					return;
				}
				event.setCancelled(true);
				if(event.getPlayer().getName().equals(LockerDB.getLock(event.getClickedBlock()).getOwner()) && Perm.hasPermissionSilent(event.getPlayer(), "locker.view.self")){
					event.setCancelled(false);
					return;
				}
				if(Perm.hasPermissionSilent(event.getPlayer(), "locker.view.other")){
					event.setCancelled(false);
					return;
				}
				for(int i = 0; i < LockerDB.getLock(event.getClickedBlock()).getAllowedList().size(); i++){
					if(LockerDB.getLock(event.getClickedBlock()).getAllowedList().get(i).equals("*") || event.getPlayer().getName().equals(LockerDB.getLock(event.getClickedBlock()).getAllowedList().get(i))){
						event.setCancelled(false);
						return;
					}
					if(LockerDB.getLock(event.getClickedBlock()).getAllowedList().get(i).startsWith("g:") || LockerDB.getLock(event.getClickedBlock()).getAllowedList().get(i).startsWith("G:") && !Config.op_permissions){
						if(Perm.PermissionsHandler.inGroup(event.getPlayer().getWorld().getName(), event.getPlayer().getName(), LockerDB.getLock(event.getClickedBlock()).getAllowedList().get(i).substring(2))){
							event.setCancelled(false);
							return;
						}
					}
				}
				if(LockerPassword.isUnlockedBy(event.getPlayer().getName(), event.getClickedBlock())){
					event.setCancelled(false);
					return;
				}
				if(event.getClickedBlock().getTypeId() != 71){
					event.getPlayer().sendMessage(Misc.replaceBlock(Config.locked, event.getClickedBlock()));
				}
			}
			return;
		}else if(event.getAction() == Action.LEFT_CLICK_BLOCK){
			if(LockerAction.getAction(event.getPlayer().getName()) != null){
				event.setCancelled(true);
				if(LockerAction.getAction(event.getPlayer().getName()).getCommand().equals("lock")){
					List<String> allowedlist = new LinkedList<String>();
					String allowed = "";
					String password = "";
					boolean ok = false;

					if(!Misc.isValidBlock(event.getClickedBlock())){
						event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_possible, event.getClickedBlock()));
						LockerAction.setAction(event.getPlayer().getName(), null);
						return;
					}
					if(LockerDB.getLock(event.getClickedBlock()) != null){
						if(event.getPlayer().getName().equals(LockerDB.getLock(event.getClickedBlock()).getOwner())){
							if(!Perm.hasPermission(event.getPlayer(), "locker.lock.self")){
								LockerAction.setAction(event.getPlayer().getName(), null);
								return;
							}
							ok = true;
						}
						if(Perm.hasPermissionSilent(event.getPlayer(), "locker.lock.other")){
							ok = true;
						}
					}else{
						if(LockerDB.getBlocksBy(event.getPlayer().getName()).size() >= Config.max_blocks){
							if(!Perm.hasPermissionSilent(event.getPlayer(), "locker.nolimit")){
								LockerAction.setAction(event.getPlayer().getName(), null);
								event.getPlayer().sendMessage(Misc.replaceColor(Config.limit));
								return;
							}
						}
						if(!Perm.hasPermission(event.getPlayer(), "locker.lock.self")){
							LockerAction.setAction(event.getPlayer().getName(), null);
							return;
						}
						ok = true;
					}
					if(ok){
						boolean add = true, all = false, group = false;

						if(!Misc.isValidBlock(event.getClickedBlock())){
							LockerAction.setAction(event.getPlayer().getName(), null);
							event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_possible, event.getClickedBlock()));
							return;
						}
						for(int i = 0; i < LockerAction.getAction(event.getPlayer().getName()).getArguments().length; i++){
							if(LockerAction.getAction(event.getPlayer().getName()).getArguments()[i].startsWith("p:") || LockerAction.getAction(event.getPlayer().getName()).getArguments()[i].startsWith("P:")){
								allowedlist.clear();
								if(!Misc.isValidPassword(LockerAction.getAction(event.getPlayer().getName()).getArguments()[i].substring(2))){
									LockerAction.setAction(event.getPlayer().getName(), null);
									event.getPlayer().sendMessage(Misc.replacePassword(Config.invalid_password, LockerAction.getAction(event.getPlayer().getName()).getArguments()[i].substring(2)));
									return;
								}
								password = LockerAction.getAction(event.getPlayer().getName()).getArguments()[i].substring(2);
								i = LockerAction.getAction(event.getPlayer().getName()).getArguments().length;
							}else if(LockerAction.getAction(event.getPlayer().getName()).getArguments()[i].equals("*")){
								all = true;
							}else{
								group = false;
								if(LockerAction.getAction(event.getPlayer().getName()).getArguments()[i].startsWith("g:") || LockerAction.getAction(event.getPlayer().getName()).getArguments()[i].startsWith("G:")){
									if(!Misc.isValidGroup(LockerAction.getAction(event.getPlayer().getName()).getArguments()[i].substring(2))){
										LockerAction.setAction(event.getPlayer().getName(), null);
										event.getPlayer().sendMessage(Misc.replaceGroup(Config.invalid_group, LockerAction.getAction(event.getPlayer().getName()).getArguments()[i].substring(2)));
										return;
									}
									group = true;
								}
								if(group && Config.op_permissions){
									LockerAction.setAction(event.getPlayer().getName(), null);
									event.getPlayer().sendMessage(Misc.replaceColor(Config.permissions_disabled));
									return;
								}
								if(allowedlist.size() != 0){
									add = true;
									for(int u = 0; u < allowedlist.size(); u++){
										if(LockerAction.getAction(event.getPlayer().getName()).getArguments()[i].equals(allowedlist.get(u)) || event.getPlayer().getName().equals(LockerAction.getAction(event.getPlayer().getName()).getArguments()[i])){
											u = allowedlist.size();
											add = false;
										}
									}
									if(add){
										if(!Misc.isValidName(LockerAction.getAction(event.getPlayer().getName()).getArguments()[i]) && !group){
											LockerAction.setAction(event.getPlayer().getName(), null);
											event.getPlayer().sendMessage(Misc.replaceName(Config.invalid_name, LockerAction.getAction(event.getPlayer().getName()).getArguments()[i]));
											return;
										}
										allowedlist.add(LockerAction.getAction(event.getPlayer().getName()).getArguments()[i]);
									}
								}else{
									if(!event.getPlayer().getName().equals(LockerAction.getAction(event.getPlayer().getName()).getArguments()[i])){
										allowedlist.add(LockerAction.getAction(event.getPlayer().getName()).getArguments()[i]);
									}
								}
							}
						}
						if(all && password.length() == 0){
							allowedlist.clear();
							allowedlist.add("*");
						}
						for(int i = 0; i < allowedlist.size(); i++){
							if(i != 0){
								allowed += " ";
							}
							allowed += allowedlist.get(i);
						}
						if(!Misc.isInteractAble(event.getClickedBlock())){
							allowed = "";
							password = "";
						}
						if(event.getClickedBlock().getTypeId() == 64 || event.getClickedBlock().getTypeId() == 71){
							if(Misc.isProhibited(Misc.getBlockAt(Door.getUnderBlock(event.getClickedBlock())))){
								event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_lock_one, event.getClickedBlock()));
							}else{
								if(LockerDB.getLock(Door.getUnderBlock(event.getClickedBlock())) != null){
									if(!LockerDB.getLock(Door.getUnderBlock(event.getClickedBlock())).getOwner().equals(event.getPlayer().getName()) && Perm.hasPermissionSilent(event.getPlayer(), "locker.lock.other")){
										LockerAction.setAction(event.getPlayer().getName(), null);
										event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_lock_one, event.getClickedBlock()));
										return;
									}
								}
								if(Door.setDoor(event.getClickedBlock().getTypeId(), event.getPlayer().getName(), allowed, password, event.getClickedBlock().getLocation())){
									event.getPlayer().sendMessage(Misc.replaceBlock(Config.lock_one, event.getClickedBlock()));
								}else{
									event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_lock_one, event.getClickedBlock()));
								}
							}
						}else if(event.getClickedBlock().getTypeId() == 54){
							boolean doublechest = false;

							if(Chest.isDoubleChest(event.getClickedBlock()) > 2){
								event.getPlayer().sendMessage(Misc.replaceBlock(Config.invalid_block, event.getClickedBlock()));
							}else{
								if(Chest.isDoubleChest(event.getClickedBlock()) == 2){
									doublechest = true;
								}
								if(doublechest){
									if(Chest.setDoubleChest(event.getPlayer().getName(), allowed, password, event.getClickedBlock())){
										event.getPlayer().sendMessage(Misc.replaceBlock(Config.lock_two, event.getClickedBlock()));
									}else{
										event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_lock_two, event.getClickedBlock()));
									}
								}else{
									LockerDB.setLock(event.getClickedBlock(), new Lock(event.getPlayer().getName(), allowed, password, event.getClickedBlock()));
									event.getPlayer().sendMessage(Misc.replaceBlock(Config.lock_one, event.getClickedBlock()));
								}
							}
						}else{
							LockerDB.setLock(event.getClickedBlock(), new Lock(event.getPlayer().getName(), allowed, password, event.getClickedBlock()));
							event.getPlayer().sendMessage(Misc.replaceBlock(Config.lock_one, event.getClickedBlock()));
						}
					}else{
						event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_lock_one, event.getClickedBlock()));
					}
				}else if(LockerAction.getAction(event.getPlayer().getName()).getCommand().equals("unlock")){
					if(!Misc.isValidBlock(event.getClickedBlock())){
						event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_possible, event.getClickedBlock()));
						LockerAction.setAction(event.getPlayer().getName(), null);
						return;
					}
					if(LockerAction.getAction(event.getPlayer().getName()).getArguments().length > 0){
						if(!Perm.hasPermission(event.getPlayer(), "locker.unlock.password")){
							LockerAction.setAction(event.getPlayer().getName(), null);
							return;
						}
						if(LockerDB.getLock(event.getClickedBlock()) == null){
							event.getPlayer().sendMessage(Misc.replaceColor(Config.not_locked));
						}
						if(LockerDB.getLock(event.getClickedBlock()).getOwner().equals(event.getPlayer().getName())){
							event.getPlayer().sendMessage(Misc.replaceBlock(Config.owned, event.getClickedBlock()));
							LockerAction.setAction(event.getPlayer().getName(), null);
							return;
						}
						if(LockerDB.getLock(event.getClickedBlock()).getPassword() != ""){
							if(LockerAction.getAction(event.getPlayer().getName()).getArguments().length != 1){
								event.getPlayer().sendMessage(Misc.replaceColor(Config.invalid_password));
								LockerAction.setAction(event.getPlayer().getName(), null);
								return;
							}
							if(!LockerDB.getLock(event.getClickedBlock()).getPassword().equals(LockerAction.getAction(event.getPlayer().getName()).getArguments()[0])){
								event.getPlayer().sendMessage(Misc.replaceColor(Config.wrong_password));
								LockerAction.setAction(event.getPlayer().getName(), null);
								return;
							}
							event.getPlayer().sendMessage(Misc.replaceColor(Config.correct_password));
							LockerPassword.getPassword(event.getPlayer().getName()).addUnlocked(event.getClickedBlock());
							if(event.getClickedBlock().getTypeId() == 64){
								if(Door.getDoubleDoorBlock(64, event.getClickedBlock().getLocation()) != null){
									LockerPassword.getPassword(event.getPlayer().getName()).addUnlocked(Door.getDoubleDoorBlock(64, event.getClickedBlock().getLocation()));
								}
							}else if(event.getClickedBlock().getTypeId() == 71){
								if(Door.getDoubleDoorBlock(71, event.getClickedBlock().getLocation()) != null){
									LockerPassword.getPassword(event.getPlayer().getName()).addUnlocked(Door.getDoubleDoorBlock(71, event.getClickedBlock().getLocation()));
								}
							}else if(event.getClickedBlock().getTypeId() == 54){
								if(Chest.getDoubleChestBlock(event.getClickedBlock()) != null){
									LockerPassword.getPassword(event.getPlayer().getName()).addUnlocked(Chest.getDoubleChestBlock(event.getClickedBlock()));
								}
							}
							LockerAction.setAction(event.getPlayer().getName(), null);
						}else{
							event.getPlayer().sendMessage(Misc.replaceColor(Config.not_password));
						}
						return;
					}
					if(LockerDB.getLock(event.getClickedBlock()) != null){
						if(event.getClickedBlock().getTypeId() == 64 || event.getClickedBlock().getTypeId() == 71){
							if(!event.getPlayer().getName().equals(LockerDB.getLock(event.getClickedBlock()).getOwner()) && !Perm.hasPermissionSilent(event.getPlayer(), "locker.unlock.other")){
								event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_unlock_one, event.getClickedBlock()));
								LockerAction.setAction(event.getPlayer().getName(), null);
								return;
							}else{
								if(!Perm.hasPermission(event.getPlayer(), "locker.unlock.self")){
									LockerAction.setAction(event.getPlayer().getName(), null);
									return;
								}
							}
							if(Door.setDoor(null, null, null, event.getClickedBlock())){
								event.getPlayer().sendMessage(Misc.replaceBlock(Config.unlock_one, event.getClickedBlock()));
							}else{
								event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_unlock_one, event.getClickedBlock()));
							}
						}else if(event.getClickedBlock().getTypeId() == 54){
							boolean doublechest = false;

							if(Chest.isDoubleChest(event.getClickedBlock()) > 2){
								event.getPlayer().sendMessage(Misc.replaceBlock(Config.invalid_block, event.getClickedBlock()));
							}else{
								if(Chest.isDoubleChest(event.getClickedBlock()) == 2){
									doublechest = true;
								}
								if(doublechest){
									if(!event.getPlayer().getName().equals(LockerDB.getLock(event.getClickedBlock()).getOwner()) && !Perm.hasPermissionSilent(event.getPlayer(), "locker.unlock.other")){
										LockerAction.setAction(event.getPlayer().getName(), null);
										return;
									}else{
										if(!Perm.hasPermission(event.getPlayer(), "locker.unlock.self")){
											LockerAction.setAction(event.getPlayer().getName(), null);
											return;
										}
									}
									if(Chest.setDoubleChest(null, null, null, event.getClickedBlock())){
										event.getPlayer().sendMessage(Misc.replaceBlock(Config.unlock_two, event.getClickedBlock()));
									}else{
										event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_unlock_two, event.getClickedBlock()));
									}
								}else{
									if(event.getPlayer().getName().equals(LockerDB.getLock(event.getClickedBlock()).getOwner()) && Perm.hasPermissionSilent(event.getPlayer(), "locker.unlock.self")){
										LockerDB.setLock(event.getClickedBlock(), null);
										event.getPlayer().sendMessage(Misc.replaceBlock(Config.unlock_one, event.getClickedBlock()));
									}else if(Perm.hasPermissionSilent(event.getPlayer(), "locker.unlock.other")){
										LockerDB.setLock(event.getClickedBlock(), null);
										event.getPlayer().sendMessage(Misc.replaceBlock(Config.unlock_one, event.getClickedBlock()));
									}else{
										event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_unlock_one, event.getClickedBlock()));
									}
								}
							}
						}else{
							if(event.getPlayer().getName().equals(LockerDB.getLock(event.getClickedBlock()).getOwner()) && Perm.hasPermissionSilent(event.getPlayer(), "locker.unlock.self")){
								LockerDB.setLock(event.getClickedBlock(), null);
								event.getPlayer().sendMessage(Misc.replaceBlock(Config.unlock_one, event.getClickedBlock()));
							}else if(Perm.hasPermissionSilent(event.getPlayer(), "locker.unlock.other")){
								LockerDB.setLock(event.getClickedBlock(), null);
								event.getPlayer().sendMessage(Misc.replaceBlock(Config.unlock_one, event.getClickedBlock()));
							}else{
								event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_unlock_one, event.getClickedBlock()));
							}
						}
					}else{
						event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_locked, event.getClickedBlock()));
					}
				}else if(LockerAction.getAction(event.getPlayer().getName()).getCommand().equals("lockinfo")){
					if(!Misc.isValidBlock(event.getClickedBlock())){
						event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_possible, event.getClickedBlock()));
						LockerAction.setAction(event.getPlayer().getName(), null);
						return;
					}
					if(!Perm.hasPermission(event.getPlayer(), "locker.lockinfo")){
						LockerAction.setAction(event.getPlayer().getName(), null);
						return;
					}
					if(LockerDB.getLock(event.getClickedBlock()) != null){
						event.getPlayer().sendMessage(Misc.replaceBlock(Config.type, event.getClickedBlock()));
						event.getPlayer().sendMessage(Misc.replaceOwner(Config.owner, event.getClickedBlock()));
						if(LockerDB.getLock(event.getClickedBlock()).getAllowed().length() > 0){
							event.getPlayer().sendMessage(Misc.replaceAllowed(Config.allowed, event.getClickedBlock()));
						}
						if(LockerDB.getLock(event.getClickedBlock()).getPassword().length() != 0){
							event.getPlayer().sendMessage(Misc.replaceColor(Config.password));
						}
					}else{
						event.getPlayer().sendMessage(Misc.replaceBlock(Config.not_locked, event.getClickedBlock()));
					}
				}
				LockerAction.setAction(event.getPlayer().getName(), null);
			}else if(event.getClickedBlock().getTypeId() == 64){
				if(LockerDB.getLock(event.getClickedBlock()) != null){
					event.setCancelled(true);
					if(event.getPlayer().getName().equals(LockerDB.getLock(event.getClickedBlock()).getOwner()) && Perm.hasPermissionSilent(event.getPlayer(), "locker.view.self")){
						event.setCancelled(false);
						return;
					}
					if(Perm.hasPermissionSilent(event.getPlayer(), "locker.view.other")){
						event.setCancelled(false);
						return;
					}
					for(int i = 0; i < LockerDB.getLock(event.getClickedBlock()).getAllowedList().size(); i++){
						if(LockerDB.getLock(event.getClickedBlock()).getAllowedList().get(i).equals("*") || event.getPlayer().getName().equals(LockerDB.getLock(event.getClickedBlock()).getAllowedList().get(i))){
							event.setCancelled(false);
							return;
						}
						if(LockerDB.getLock(event.getClickedBlock()).getAllowedList().get(i).startsWith("g:") || LockerDB.getLock(event.getClickedBlock()).getAllowedList().get(i).startsWith("G:") && !Config.op_permissions){
							if(Perm.PermissionsHandler.inGroup(event.getPlayer().getWorld().getName(), event.getPlayer().getName(), LockerDB.getLock(event.getClickedBlock()).getAllowedList().get(i).substring(2))){
								event.setCancelled(false);
								return;
							}
						}
					}
					if(LockerPassword.isUnlockedBy(event.getPlayer().getName(), event.getClickedBlock())){
						event.setCancelled(false);
						return;
					}
					event.getPlayer().sendMessage(Misc.replaceBlock(Config.locked, event.getClickedBlock()));
				}
			}
		}
	}
}