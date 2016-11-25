package com.github.shchurov.particleview;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class TextureAtlas {

    private int width;
    private int height;
    private List<Region> regions = new ArrayList<>();
    private boolean editable = true;

    public TextureAtlas(int width, int height) {
        this.width = width;
        this.height = height;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    List<Region> getRegions() {
        return regions;
    }

    void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int addRegion(int x, int y, Bitmap bitmap) {
        if (!editable) {
            throw new IllegalStateException("TextureAtlas is not editable anymore");
        }
        regions.add(new Region(x, y, bitmap));
        return regions.size() - 1;
    }

    static class Region {
        final int x;
        final int y;
        final Bitmap bitmap;

        Region(int x, int y, Bitmap bitmap) {
            this.x = x;
            this.y = y;
            this.bitmap = bitmap;
        }
    }

}
