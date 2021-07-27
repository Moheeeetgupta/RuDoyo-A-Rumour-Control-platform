package com.rumooursindoyo.moheeeetgupta;

import com.google.gson.annotations.SerializedName;

class Snippet {

    @SerializedName("topLevelComment")
    private TopComments comment;

    public TopComments getComment() {
        return comment;
    }

    public void setComment(TopComments comment) {
        this.comment = comment;
    }
}