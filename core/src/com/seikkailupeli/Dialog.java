package com.seikkailupeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dialog {

    private int dialog_id;
    private int character_id;
    private int dialogStart;
    private int dialogEnd;
    private int dialogPosX;
    private int dialogPosY;

    private float dialogMaxTime = 1.5f;

    private boolean playerSpeaking;

    static private List<String> dialogs = new ArrayList<String>();
    static private boolean dialogsLoaded = false;

    public Dialog(int character_id, int dialogIdStart, int dialogIdEnd, boolean playerStart, int positionX, int positionY) {

        this.character_id = character_id;

        if (!dialogsLoaded) {
            loadDialogs();
        }

        dialog_id = dialogIdStart;
        dialogEnd = dialogIdEnd;

        playerSpeaking = playerStart;

        dialogPosX = positionX;
        dialogPosY = positionY;

    }

    private void loadDialogs() {
        FileHandle handle = Gdx.files.internal("dialog_texts.txt");
        String dialogText = handle.readString();
        String textArray[] = dialogText.split("\\r?\\n");
        Collections.addAll(dialogs, textArray);
        dialogsLoaded = true;
    }

    String getCurrentDialog() {
        return dialogs.get(dialog_id);
    }

    void goNextDialog() {
        dialog_id++;
    }

    void goToDialog(int id) {
        dialog_id = id;
    }

    float getDialogMaxTime() {
        return dialogMaxTime;
    }

    int getDialog_id() {
        return dialog_id;
    }

    int getDialogEnd() {
        return dialogEnd;
    }

    int getCharacter_id() {
        return character_id;
    }

    boolean isPlayerSpeaking() {
        return playerSpeaking;
    }

    void switchSpeaker() {
        playerSpeaking = !playerSpeaking;
    }

    int getDialogPosX() {
        return dialogPosX;
    }
    int getDialogPosY() {
        return dialogPosY;
    }
}

