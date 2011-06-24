package com.vildaberper.Locker;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name="locker")
public class Lock{
	@Id
	@Column(unique=true)
	private int id;

	@NotEmpty
	private String owner;

	@NotEmpty
	private String world;

	@NotNull
	private String password;

	@NotNull
	private String allowed;

	@NotNull
	private int x;

	@NotNull
	private int y;

	@NotNull
	private int z;

	public Lock(){
		this.owner = "";
		this.allowed = "";
		this.password = "";
		this.world = "";
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Lock(String owner, String allowed, String password, String world, int x, int y, int z){
		this.owner = owner;
		this.allowed = allowed;
		this.password = password;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Lock(String owner, String allowed, String password, Block block){
		this.owner = owner;
		this.allowed = allowed;
		this.password = password;
		this.world = block.getWorld().getName();
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
	}

	public Lock(String owner, String allowed, String password, Location location){
		this.owner = owner;
		this.allowed = allowed;
		this.password = password;
		this.world = location.getWorld().getName();
		this.x = location.getBlockX();
		this.y = location.getBlockY();
		this.z = location.getBlockZ();
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public String getOwner(){
		return this.owner;
	}

	public void setOwner(String owner){
		this.owner = owner;
	}

	public List<String> getAllowedList(){
		List<String> allowed = new LinkedList<String>();

		for(int i = 0; i < this.allowed.split(" ").length; i++){
			allowed.add(this.allowed.split(" ")[i]);
		}
		return allowed;
	}

	public void setAllowed(List<String> allowed){
		for(int i = 0; i < allowed.size(); i++){
			if(i != 0){
				this.allowed += " ";
			}
			this.allowed += allowed.get(i);
		}
	}

	public String getAllowed(){
		return this.allowed;
	}

	public void setAllowed(String allowed){
		this.allowed = allowed;
	}

	public String getPassword(){
		return this.password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getWorld(){
		return this.world;
	}

	public void setWorld(String world){
		this.world = world;
	}

	public int getX(){
		return this.x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return this.y;
	}

	public void setY(int y){
		this.y = y;
	}

	public int getZ(){
		return this.z;
	}

	public void setZ(int z){
		this.z = z;
	}

	public Lock getLock(){
		return this;
	}

	public void setLock(Lock lock){
		this.owner = lock.owner;
		this.allowed = lock.allowed;
		this.password = lock.password;
		this.world = lock.world;
		this.x = lock.x;
		this.y = lock.y;
		this.z = lock.z;
	}
}