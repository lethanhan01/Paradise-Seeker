package com.paradise_seeker.game.entity.npc.dialogue;

import java.util.ArrayList;
import java.util.List;

public class NPCDialogueManager implements NPCDialogue {
    private List<String> dialogueLines = new ArrayList<>();
    private int currentLineIndex = 0;

    public NPCDialogueManager() {
        this.dialogueLines = new ArrayList<>();
    }

    public NPCDialogueManager(List<String> lines) {
        setDialogue(lines);
    }
    @Override
    public void setDialogue(List<String> lines) {
        this.dialogueLines = new ArrayList<>(lines);
        resetDialogue();
    }

    public String getCurrentLine() {
        if (dialogueLines.isEmpty()) {
            return "";
        }
        return dialogueLines.get(currentLineIndex);
    }
    @Override
    public boolean hasNextLine() {
        return currentLineIndex < dialogueLines.size() - 1;
    }
    @Override
    public void nextLine() {
        if (hasNextLine()) {
            currentLineIndex++;
        }
    }
    @Override
    public void resetDialogue() {
        currentLineIndex = 0;
    }

    public int getCurrentLineIndex() {
        return currentLineIndex;
    }
    @Override
    public void addLine(String line) {
        dialogueLines.add(line);
    }
    @Override
    public boolean isEmpty() {
        return dialogueLines.isEmpty();
    }
}
