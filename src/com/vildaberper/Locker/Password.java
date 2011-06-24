package com.vildaberper.Locker;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.Block;

public class Password{
	Password(String name, List<Block> unlocked){
		this.name = name;
		this.unlocked = unlocked;
	}

	private String name;
	private List<Block> unlocked = new LinkedList<Block>();

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setUnlocked(List<Block> unlocked){
		this.unlocked = unlocked;
	}

	public List<Block> getUnlocked(){
		return unlocked;
	}

	public void addUnlocked(Block block){
		this.unlocked.add(block);
	}

	public void delUnlocked(Block block){
		for(int i = 0; i < this.unlocked.size(); i++){
			if(this.unlocked.get(i).equals(block)){
				this.unlocked.remove(i);
				i--;
			}
		}
	}

	public void clearUnlocked(){
		this.unlocked.clear();
	}
}