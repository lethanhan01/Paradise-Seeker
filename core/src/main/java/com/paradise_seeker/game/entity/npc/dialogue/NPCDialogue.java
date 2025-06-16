package com.paradise_seeker.game.entity.npc.dialogue;

import java.util.List;

public interface NPCDialogue {
	public void setDialogue(List<String> lines);
	public boolean hasNextLine();
	public void nextLine();
	public void resetDialogue();
	public void addLine(String line);
	public boolean isEmpty();
}
