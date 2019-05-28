package me.jho.wcg.gallery;

public class GalleryVO {
    int id;
    String title;
    String font;
    String backgroundColor;
    byte[] wordCloudByte;

    public GalleryVO() {
        title = "";
        font = "";
        backgroundColor = "";
        id = 0;
        wordCloudByte = new byte[0];
    }

    public GalleryVO(int id, String title, String font, String backgroundColor, byte[] wordCloudByte) {

        this.id = id;
        this.title = title;
        this.font = font;
        this.backgroundColor = backgroundColor;
        this.wordCloudByte = wordCloudByte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public byte[] getWordCloudByte() {
        return wordCloudByte;
    }

    public void setWordCloudByte(byte[] wordCloudByte) {
        this.wordCloudByte = wordCloudByte;
    }
}
