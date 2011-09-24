package com.vildaberper.Locker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public final class Locker extends JavaPlugin{
	private final LockerBlockListener blockListener = new LockerBlockListener();
	private final LockerEntityListener entityListener = new LockerEntityListener();
	private final LockerPlayerListener playerListener = new LockerPlayerListener();

	@Override
	public void onLoad(){
		File file = new File("ebean.properties");

		if(!file.exists()){
			try{
				FileWriter fstream = new FileWriter("ebean.properties");
				BufferedWriter out = new BufferedWriter(fstream);
				out.write("ebean.ddl.generate=false\n");
				out.write("ebean.ddl.run=false\n");
				out.write("ebean.debug.sql=true\n");
				out.write("ebean.debug.lazyload=false\n");
				out.write("ebean.logging=explicit\n");
				out.write("ebean.logging.logfilesharing=all\n");
				out.write("ebean.logging.directory=logs\n");
				out.write("ebean.logging.iud=sql\n");
				out.write("ebean.logging.query=sql\n");
				out.write("ebean.logging.sqlquery=sql\n");
				out.write("ebean.logging.txnCommit=none\n");
				out.write("datasource.default=ora\n");
				out.write("datasource.ora.username=bukkit\n");
				out.write("datasource.ora.password=walrus\n");
				out.write("datasource.ora.databaseUrl=jdbc:sqlite:{DIR}{NAME}.db\n");
				out.write("datasource.ora.databaseDriver=org.sqlite.JDBC\n");
				out.write("datasource.ora.minConnections=1\n");
				out.write("datasource.ora.maxConnections=25\n");
				out.write("datasource.ora.heartbeatsql=select count(*) from dual\n");
				out.write("datasource.ora.isolationlevel=read_committed\n");
				out.close();
			}catch(Exception e){
				System.err.println(e.getMessage());
			}
		}
	}

	@Override
	public void onDisable(){
		saveConfig();
		System.out.println(this.getDescription().getName() + " " + this.getDescription().getVersion() + " is disabled.");
	}

	@Override
	public void onEnable(){
		Config.plugin = this;
		setupDatabase();
		loadConfig();
		/*
		 * Convert old DB
		 */
		String allowed = "";
		Configuration db = new Configuration(new File(this.getDataFolder(), "LockerDB" + File.separator + Config.default_db + ".yml"));
		db.load();
		if(db.getKeys("DB") != null){
			for(int i = 0; i < db.getKeys("DB").size(); i++){
				allowed = "";
				for(int u = 0; u  < db.getStringList("DB." + db.getKeys("DB").get(i) + ".A", new LinkedList<String>()).size(); u++){
					if(allowed.length() != 0){
						allowed += " ";
					}
					allowed += db.getStringList("DB." + db.getKeys("DB").get(i) + ".A", new LinkedList<String>()).get(u);
				}
				LockerDB.setLock(db.getKeys("DB").get(i).split("_")[0], Integer.parseInt(db.getKeys("DB").get(i).split("_")[1]), Integer.parseInt(db.getKeys("DB").get(i).split("_")[2]), Integer.parseInt(db.getKeys("DB").get(i).split("_")[3]), new Lock(db.getString("DB." + db.getKeys("DB").get(i) + ".O", ""), allowed, db.getString("DB." + db.getKeys("DB").get(i) + ".P", ""), db.getKeys("DB").get(i).split("_")[0], Integer.parseInt(db.getKeys("DB").get(i).split("_")[1]), Integer.parseInt(db.getKeys("DB").get(i).split("_")[2]), Integer.parseInt(db.getKeys("DB").get(i).split("_")[3])));
			}
			new Configuration(new File(this.getDataFolder(), "LockerDB" + File.separator + Config.default_db + ".yml")).save();
			System.out.println("Default database for " + this.getDescription().getName() + " converted and cleared. You can delete the old databases manually (plugins/Locker/LockerDB).");
		}
		/*
		 * Done
		 */
		this.getServer().getPluginManager().registerEvent(Type.ENTITY_EXPLODE, entityListener, Priority.Highest, this);
		this.getServer().getPluginManager().registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Low, this);
		this.getServer().getPluginManager().registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Low, this);
		this.getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.High, this);
		this.getServer().getPluginManager().registerEvent(Type.PLAYER_ANIMATION, playerListener, Priority.High, this);
		this.getServer().getPluginManager().registerEvent(Type.BLOCK_BREAK, blockListener, Priority.High, this);
		this.getServer().getPluginManager().registerEvent(Type.BLOCK_PLACE, blockListener, Priority.High, this);
		System.out.println(this.getDescription().getName() + " " + this.getDescription().getVersion() + " is enabled. There are " + LockerDB.getSize() + " blocks in the database.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		Player s;

		if(command.getName().equalsIgnoreCase("lock") && sender instanceof Player){
			s = (Player) sender;
			sender.sendMessage(Misc.replaceColor(Config.lock));
			LockerAction.setAction(s.getName(), new Action(s.getName(), "lock", args));
			return true;
		}else if(command.getName().equalsIgnoreCase("unlock") && sender instanceof Player){
			s = (Player) sender;
			if(args.length != 0 && args.length != 1){
				return false;
			}
			sender.sendMessage(Misc.replaceColor(Config.unlock));
			LockerAction.setAction(s.getName(), new Action(s.getName(), "unlock", args));
			return true;
		}else if(command.getName().equalsIgnoreCase("lockinfo") && sender instanceof Player){
			s = (Player) sender;
			if(args.length != 0){
				return false;
			}
			sender.sendMessage(Misc.replaceColor(Config.info));
			LockerAction.setAction(s.getName(), new Action(s.getName(), "lockinfo", args));
			return true;
		}else if(command.getName().equalsIgnoreCase("locker")){
			if(args.length == 1 || args.length == 2 || args.length == 3){
				if(args[0].equalsIgnoreCase("clear")){
					List<Lock> locks;
					String bs = "s";

					if(sender instanceof Player){
						if(!Perm.hasPermission((Player) sender, "locker.clear")){
							return false;
						}
					}
					if(args.length != 1 && args.length != 2){
						return false;
					}
					if(args.length > 1){
						locks = this.getDatabase().find(Lock.class).where().ieq("owner", args[1]).findList();
						for(int i = 0; i < locks.size(); i++){
							LockerDB.setLock(locks.get(i).getWorld(), locks.get(i).getX(), locks.get(i).getY(), locks.get(i).getZ(), null);
						}
						if((locks.size() + LockerDB.getSize()) == 1){
							bs = "";
						}
						sender.sendMessage("Cleared " + locks.size() + " of " + (locks.size() + LockerDB.getSize()) + " block" + bs + ".");
					}else{
						locks = this.getDatabase().find(Lock.class).findList();
						for(int i = 0; i < locks.size(); i++){
							LockerDB.setLock(locks.get(i).getWorld(), locks.get(i).getX(), locks.get(i).getY(), locks.get(i).getZ(), null);
						}
						if(locks.size() == 1){
							bs = "";
						}
						sender.sendMessage("Cleared " + locks.size() + " block" + bs + ".");
					}
				}else if(args[0].equalsIgnoreCase("validate")){
					List<Lock> locks;
					int cleared = 0;
					String bs = "s";

					if(sender instanceof Player){
						if(!Perm.hasPermission((Player) sender, "locker.validate")){
							return false;
						}
					}
					if(args.length != 1){
						return false;
					}
					locks = this.getDatabase().find(Lock.class).findList();
					for(int i = 0; i < locks.size(); i++){
						if(this.getServer().getWorld(locks.get(i).getWorld()) == null){
							cleared++;
							LockerDB.setLock(locks.get(i).getWorld(), locks.get(i).getX(), locks.get(i).getY(), locks.get(i).getZ(), null);
							i--;
						}else{
							if(!Misc.isValidBlock(this.getServer().getWorld(locks.get(i).getWorld()).getBlockAt(locks.get(i).getX(), locks.get(i).getY(), locks.get(i).getZ()))){
								cleared++;
								LockerDB.setLock(locks.get(i).getWorld(), locks.get(i).getX(), locks.get(i).getY(), locks.get(i).getZ(), null);
								i--;
							}
						}
					}
					if((cleared + LockerDB.getSize()) == 1){
						bs = "";
					}
					sender.sendMessage("Cleared " + cleared + " of " + (cleared + LockerDB.getSize()) + " block" + bs + ".");
				}else if(args[0].equalsIgnoreCase("info")){
					String bs = "s", ps = "s";

					if(sender instanceof Player){
						if(!Perm.hasPermission((Player) sender, "locker.info")){
							return false;
						}
					}
					if(args.length != 1){
						return false;
					}
					if(LockerDB.getOwners().size() == 1){
						ps = "";
					}
					if(this.getDatabase().find(Lock.class).findRowCount() == 1){
						bs = "";
					}
					sender.sendMessage("There are " + this.getDatabase().find(Lock.class).findRowCount() + " block" + bs + " locked by " + LockerDB.getOwners().size() + " player" + ps + ".");
				}else if(args[0].equalsIgnoreCase("set")){
					String value = "";

					if(sender instanceof Player){
						if(!Perm.hasPermission((Player) sender, "locker.set")){
							return false;
						}
					}
					if(args.length != 3){
						return false;
					}
					for(int i = 2; i < args.length; i++){
						value += args[i];
					}
					if(setConfig(args[1], value)){
						sender.sendMessage("Set property '" + args[1] +  "' to '" + value + "'.");
					}else{
						sender.sendMessage("Failed to set property '" + args[1] +  "' to '" + value + "'.");
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
			return true;
		}
		return false;
	}

	private void setupDatabase(){
		try{
			getDatabase().find(Lock.class).findRowCount();
		}catch(PersistenceException ex){
			System.out.println("Installing database for " + getDescription().getName() + " due to first time usage.");
			installDDL();
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses(){
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(Lock.class);
		return list;
	}

	public boolean setConfig(String property, String value){
		Configuration config = new Configuration(new File(this.getDataFolder(), "Locker.yml"));

		if(property.equals("op_permissions") || property.equals("block_explosions") || property.equals("block_redstone")){
			if(value.equals("true")){
				config.load();
				config.setProperty(property, true);
				config.save();
				loadConfig();
				return true;
			}else if(value.equals("false")){
				config.load();
				config.setProperty(property, false);
				config.save();
				loadConfig();
				return true;
			}
		}else if(property.equals("max_blocks")){
			if(Misc.isNumeric(value)){
				config.load();
				config.setProperty(property, Integer.parseInt(value));
				config.save();
				loadConfig();
				return true;
			}
		}else if(
				property.equals("default_db")
				|| property.equals("permissions_disabled")
				|| property.equals("not_permission")
				|| property.equals("not_possible")
				|| property.equals("not_locked")
				|| property.equals("not_password")
				|| property.equals("not_place")
				|| property.equals("not_break")
				|| property.equals("not_lock_one")
				|| property.equals("not_lock_two")
				|| property.equals("not_unlock_one")
				|| property.equals("not_unlock_two")
				|| property.equals("unlock_one")
				|| property.equals("unlock_two")
				|| property.equals("lock_one")
				|| property.equals("lock_two")
				|| property.equals("invalid_block")
				|| property.equals("invalid_password")
				|| property.equals("invalid_name")
				|| property.equals("invalid_group")
				|| property.equals("wrong_password")
				|| property.equals("correct_password")
				|| property.equals("owned")
				|| property.equals("locked")
				|| property.equals("lock")
				|| property.equals("unlock")
				|| property.equals("info")
				|| property.equals("type")
				|| property.equals("owner")
				|| property.equals("allowed")
				|| property.equals("password")
				|| property.equals("limit")
		){
			config.load();
			config.setProperty(property, value);
			config.save();
			loadConfig();
			return true;
		}
		return false;
	}

	public void saveConfig(){
		Configuration config = new Configuration(new File(this.getDataFolder(), "Locker.yml"));

		config.setProperty("op_permissions", Config.op_permissions);
		config.save();
		config.load();
		config.setProperty("block_explosions", Config.block_explosions);
		config.save();
		config.load();
		config.setProperty("block_redstone", Config.block_redstone);
		config.save();
		config.load();
		config.setProperty("max_blocks", Config.max_blocks);
		config.save();
		config.load();
		config.setProperty("default_db", Config.default_db);
		config.save();
		config.load();
		config.setProperty("permissions_disabled", Config.permissions_disabled);
		config.save();
		config.load();
		config.setProperty("not_permission", Config.not_permission);
		config.save();
		config.load();
		config.setProperty("not_possible", Config.not_possible);
		config.save();
		config.load();
		config.setProperty("not_locked", Config.not_locked);
		config.save();
		config.load();
		config.setProperty("not_password", Config.not_password);
		config.save();
		config.load();
		config.setProperty("not_place", Config.not_place);
		config.save();
		config.load();
		config.setProperty("not_break", Config.not_break);
		config.save();
		config.load();
		config.setProperty("not_lock_one", Config.not_lock_one);
		config.save();
		config.load();
		config.setProperty("not_lock_two", Config.not_lock_two);
		config.save();
		config.load();
		config.setProperty("not_unlock_one", Config.not_unlock_one);
		config.save();
		config.load();
		config.setProperty("not_unlock_two", Config.not_unlock_two);
		config.save();
		config.load();
		config.setProperty("unlock_one", Config.unlock_one);
		config.save();
		config.load();
		config.setProperty("unlock_two", Config.unlock_two);
		config.save();
		config.load();
		config.setProperty("lock_one", Config.lock_one);
		config.save();
		config.load();
		config.setProperty("lock_two", Config.lock_two);
		config.save();
		config.load();
		config.setProperty("invalid_block", Config.invalid_block);
		config.save();
		config.load();
		config.setProperty("invalid_password", Config.invalid_password);
		config.save();
		config.load();
		config.setProperty("invalid_name", Config.invalid_name);
		config.save();
		config.load();
		config.setProperty("invalid_group", Config.invalid_group);
		config.save();
		config.load();
		config.setProperty("wrong_password", Config.wrong_password);
		config.save();
		config.load();
		config.setProperty("correct_password", Config.correct_password);
		config.save();
		config.load();
		config.setProperty("owned", Config.owned);
		config.save();
		config.load();
		config.setProperty("locked", Config.locked);
		config.save();
		config.load();
		config.setProperty("lock", Config.lock);
		config.save();
		config.load();
		config.setProperty("unlock", Config.unlock);
		config.save();
		config.load();
		config.setProperty("info", Config.info);
		config.save();
		config.load();
		config.setProperty("type", Config.type);
		config.save();
		config.load();
		config.setProperty("owner", Config.owner);
		config.save();
		config.load();
		config.setProperty("allowed", Config.allowed);
		config.save();
		config.load();
		config.setProperty("limit", Config.limit);
		config.save();
		config.load();
		config.setProperty("password", Config.password);
		config.save();
	}

	public void loadConfig(){
		Configuration config = new Configuration(new File(this.getDataFolder(), "Locker.yml"));

		config.load();
		Config.block_explosions = config.getBoolean("block_explosions", Config.block_explosions);
		Config.block_redstone = config.getBoolean("block_redstone", Config.block_redstone);
		Config.max_blocks = config.getInt("max_blocks", Config.max_blocks);
		Config.default_db = config.getString("default_db", Config.default_db);
		Config.permissions_disabled = config.getString("permissions_disabled", Config.permissions_disabled);
		Config.not_permission = config.getString("not_permission", Config.not_permission);
		Config.not_possible = config.getString("not_possible", Config.not_possible);
		Config.not_locked = config.getString("not_locked", Config.not_locked);
		Config.not_password = config.getString("not_password", Config.not_password);
		Config.not_place = config.getString("not_place", Config.not_place);
		Config.not_break = config.getString("not_break", Config.not_break);
		Config.not_lock_one = config.getString("not_lock_one", Config.not_lock_one);
		Config.not_lock_two = config.getString("not_lock_two", Config.not_lock_two);
		Config.not_unlock_one = config.getString("not_unlock_one", Config.not_unlock_one);
		Config.not_unlock_two = config.getString("not_unlock_two", Config.not_unlock_two);
		Config.unlock_one = config.getString("unlock_one", Config.unlock_one);
		Config.unlock_two = config.getString("unlock_two", Config.unlock_two);
		Config.lock_one = config.getString("lock_one", Config.lock_one);
		Config.lock_two = config.getString("lock_two", Config.lock_two);
		Config.invalid_block = config.getString("invalid_block", Config.invalid_block);
		Config.invalid_password = config.getString("invalid_password", Config.invalid_password);
		Config.invalid_name = config.getString("invalid_name", Config.invalid_name);
		Config.invalid_group = config.getString("invalid_group", Config.invalid_group);
		Config.wrong_password = config.getString("wrong_password", Config.wrong_password);
		Config.correct_password = config.getString("correct_password", Config.correct_password);
		Config.owned = config.getString("owned", Config.owned);
		Config.locked = config.getString("locked", Config.locked);
		Config.lock = config.getString("lock", Config.lock);
		Config.unlock = config.getString("unlock", Config.unlock);
		Config.info = config.getString("info", Config.info);
		Config.type = config.getString("type", Config.type);
		Config.owner = config.getString("owner", Config.owner);
		Config.allowed = config.getString("allowed", Config.allowed);
		Config.password = config.getString("password", Config.password);
		Config.limit = config.getString("limit", Config.limit);
	}
}