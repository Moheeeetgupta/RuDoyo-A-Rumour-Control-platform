package com.rumooursindoyo.moheeeetgupta;

import com.google.gson.annotations.SerializedName;

// Model class for nested JSON objects
/**
 * This class object is used in Snippet class
 */
class TopComments {

    // snippet named JSON object is inside TopComments in JSON format.
    @SerializedName("snippet")
    private Snippetii snippetii;

    public Snippetii getSnippetii() {
        return snippetii;
    }

    public void setSnippetii(Snippetii snippetii) {
        this.snippetii = snippetii;
    }
}
