package me.jho.wcg.gallery;

public class GalleryVO {
    String title;
    String font;
    String backgroundColor;
    long id;

    public GalleryVO() {
        title = "";
        font = "";
        backgroundColor = "";
        id = 0;
    }

    public GalleryVO(long id, String title, String font, String backgroundColor) {
        this.id = id;
        this.title = title;
        this.font = font;
        this.backgroundColor = backgroundColor;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
