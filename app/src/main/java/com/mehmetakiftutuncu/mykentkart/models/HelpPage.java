package com.mehmetakiftutuncu.mykentkart.models;

public class HelpPage {
    public int titleResourceId;
    public int imageResourceId;
    public int messageResourceId;

    public HelpPage(int titleResourceId, int imageResourceId, int messageResourceId) {
        this.titleResourceId = titleResourceId;
        this.imageResourceId = imageResourceId;
        this.messageResourceId = messageResourceId;
    }
}
