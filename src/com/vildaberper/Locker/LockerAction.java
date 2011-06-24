package com.vildaberper.Locker;

import java.util.LinkedList;
import java.util.List;

public class LockerAction {
	static List<Action> A = new LinkedList<Action>();

	public static Action getAction(String name){
		for(int i = 0; i < A.size(); i++){
			if(name.equals(A.get(i).getName())){
				return A.get(i).getAction();
			}
		}
		return null;
	}

	public static void setAction(String name, Action action){
		if(action == null){
			for(int i = 0; i < A.size(); i++){
				if(name.equals(A.get(i).getName())){
					A.remove(i);
					return;
				}
			}
			return;
		}
		for(int i = 0; i < A.size(); i++){
			if(name.equals(A.get(i).getName())){
				A.get(i).setAction(action);
				return;
			}
		}
		A.add(action);
	}
}