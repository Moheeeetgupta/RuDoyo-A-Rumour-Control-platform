package com.rumooursindoyo.moheeeetgupta;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// Model class for receiving response from retrofit, we have to get
public class Results {

    //@SerializedName("name")
    // @SerializedName("kind")
    private String kind;  // "kind", is same as it in JSON output formate field.


    /**
     * The @SerializedName annotation can be used to serialize a field with a different name instead of an actual field name.
     * We can provide the expected serialized name as an annotation attribute, Gson can make sure to read or write a field with
     * the provided name.
     */
    @SerializedName("items") // "items" tag belongs to output data JSON format which is representing their as JSON array,
    // "List<ItemsArray> userArray", this has used in place of "items" field
    private List<ItemsArray> userArray = null;

    // constructor
    public Results(String name) {
        this.kind = name;
    }

    //
    public String getKind() {
        return kind;
    }

    // for geeting items array
    public List<ItemsArray> getUserArray() {
        return userArray;
    }

    // for setting items array
    public void setUserArray(List<ItemsArray> userArray) {
        this.userArray = userArray;
    }
}