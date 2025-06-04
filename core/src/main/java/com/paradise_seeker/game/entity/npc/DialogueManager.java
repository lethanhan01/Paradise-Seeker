package com.paradise_seeker.game.entity.npc;

import java.util.ArrayList;
import java.util.List;

/**
 * Class quản lý hội thoại của NPC
 */
public class DialogueManager {
    private List<String> dialogueLines = new ArrayList<>();
    private int currentLineIndex = 0;

    public DialogueManager() {
        this.dialogueLines = new ArrayList<>();
    }

    public DialogueManager(List<String> lines) {
        setDialogue(lines);
    }

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

    public boolean hasNextLine() {
        return currentLineIndex < dialogueLines.size() - 1;
    }

    public void nextLine() {
        if (hasNextLine()) {
            currentLineIndex++;
        }
    }

    public void resetDialogue() {
        currentLineIndex = 0;
    }

    public int getCurrentLineIndex() {
        return currentLineIndex;
    }

    public void addLine(String line) {
        dialogueLines.add(line);
    }

    public boolean isEmpty() {
        return dialogueLines.isEmpty();
    }
}
