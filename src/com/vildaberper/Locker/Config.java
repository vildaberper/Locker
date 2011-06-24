package com.vildaberper.Locker;

import org.bukkit.plugin.Plugin;

public class Config {
	public static Plugin plugin = null;

	public static boolean
	op_permissions = false,
	block_explosions = true,
	block_redstone = true;
	public static int
	max_blocks = 100;
	public static String
	default_db = "LockerDB",
	permissions_disabled = "&4Permissions is currently disabled.",
	not_permission = "&4You do not have permission to do that.",
	not_possible = "&4It is not possible to lock or unlock &7<block>&4.",
	not_locked = "&4That &7<block>&4 is not locked.",
	not_password = "&4That &7<block>&4 is not password protected.",
	not_place = "&4You cannot place that &7<block>&4.",
	not_break = "&4You cannot break that &7<block>&4.",
	not_lock_one = "&4You cannot lock that &7<block>&4.",
	not_lock_two = "&4You cannot lock those &7<block>s&4.",
	not_unlock_one = "&4You cannot unlock that &7<block>&4.",
	not_unlock_two = "&4You cannot unlock those &7<block>s&4.",
	unlock_one = "&2Unlocked one &7<block>&2.",
	unlock_two = "&2Unlocked two &7<block>s&2.",
	lock_one = "&2Locked one &7<block>&2.",
	lock_two = "&2Locked two &7<block>s&2.",
	invalid_block = "&4Invalid &7<block>&4.",
	invalid_password = "&4<password> is not a valid password.",
	invalid_name = "&4<name> is not a valid name.",
	invalid_group = "&4<group> is not a valid group.",
	wrong_password = "&4Wrong password.",
	correct_password = "&2Correct password.",
	owned = "&4You own that &7<block>&4.",
	locked = "&4That &7<block>&4 is locked.",
	lock = "&2Left-click a block to lock it.",
	unlock = "&2Left-click a block to unlock it.",
	info = "&2Left click a block to view info about it.",
	type = "&2Type: &7<block>",
	owner = "&2Owner: &7<owner>",
	allowed = "&2Allowed: &7<allowed>",
	password = "&2Password protected",
	limit = "&4You cannot lock any more blocks.";
}