package com.example.kyoantrovice.fruitrecognition.MenuSettings.LanguageCustomeListview;

/**
 * Created by KyoAntrovice on 4/16/2016.
 */
public class LanguageTable {
    private int imageID ;
    private String Nation;

    public LanguageTable(int imageID, String nation) {
        this.imageID = imageID;
        Nation = nation;
    }

    public int getImageID() {

        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getNation() {
        return Nation;
    }

    public void setNation(String nation) {
        Nation = nation;
    }
}
