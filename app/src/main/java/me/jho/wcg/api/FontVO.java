package me.jho.wcg.api;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class FontVO {
    @SerializedName("name")
    private String name;
    @SerializedName("path")
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
