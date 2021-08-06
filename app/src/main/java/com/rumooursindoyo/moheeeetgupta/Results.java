package com.rumooursindoyo.moheeeetgupta;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results {

    //@SerializedName("name")
    // @SerializedName("kind")
    private String kind;
    @SerializedName("items")
    private List<ItemsArray> userArray = null;

    public Results(String name) {
        this.kind = name;
    }

    public String getKind() {
        return kind;
    }

    public List<ItemsArray> getUserArray() {
        return userArray;
    }

    public void setUserArray(List<ItemsArray> userArray) {
        this.userArray = userArray;
    }
}