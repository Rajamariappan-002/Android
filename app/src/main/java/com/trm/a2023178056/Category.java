package com.trm.a2023178056;

// Category.java
public class Category {
    private final String name;
    private final int imageResource;

    public Category(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }
}
