package com.rumooursindoyo.moheeeetgupta;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ItemsArray {
    @SerializedName("snippet")
    private Snippet snippet;


    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }
}
