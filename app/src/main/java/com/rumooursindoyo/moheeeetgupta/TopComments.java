package com.rumooursindoyo.moheeeetgupta;

import com.google.gson.annotations.SerializedName;

class TopComments {

    @SerializedName("snippet")
    private Snippetii snippetii;

    public Snippetii getSnippetii() {
        return snippetii;
    }

    public void setSnippetii(Snippetii snippetii) {
        this.snippetii = snippetii;
    }
}
