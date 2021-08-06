package com.rumooursindoyo.moheeeetgupta;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// Model class for nested JSON objects
/**
 * This is used as object of userArray attribute of Results class
 */
class ItemsArray {

    // in JSON format, snippet named JSON object is present, so it is exactly implementing that JSON structure.
    @SerializedName("snippet")
    private Snippet snippet;


    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }
}
