package com.fypvpreventor.VpreventorFYP;

public class RecyclerEntity {
    private String title;
    private boolean showMenu = false;
    private int image;

    public RecyclerEntity() {
    }

    public RecyclerEntity(String title, int image, boolean showMenu) {
        this.title = title;
        this.showMenu = showMenu;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    //... all the getters and setters
}