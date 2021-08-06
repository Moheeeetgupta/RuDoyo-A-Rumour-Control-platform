package com.rumooursindoyo.moheeeetgupta;

import com.google.gson.annotations.SerializedName;

// Model class for nested JSON objects
/**
 * This class object is used as attribute of ItemsArray Class
 */
class Snippet {

    // topLevelComment , this is inside Snippet JSON object so , to retrive response this structure is implemented.
    @SerializedName("topLevelComment")
    private TopComments comment;

    public TopComments getComment() {
        return comment;
    }

    public void setComment(TopComments comment) {
        this.comment = comment;
    }
}