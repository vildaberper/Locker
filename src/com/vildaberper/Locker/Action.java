package com.vildaberper.Locker;

public class Action{
	String name;
	String command;
	String[] arguments;

	public Action(String name, String command, String[] arguments){
		this.name = name;
		this.command = command;
		this.arguments = arguments;
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getCommand(){
		return this.command;
	}

	public void setCommand(String command){
		this.command = command;
	}

	public String[] getArguments(){
		return this.arguments;
	}

	public void setArguments(String[] arguments){
		this.arguments = arguments;
	}

	public Action getAction(){
		return this;
	}

	public void setAction(Action action){
		this.name = action.name;
		this.command = action.command;
		this.arguments = action.arguments;
	}
}