package com.example.aokly.samsungfragments.simpleData;

/**
 * Created by teacher on 11.02.17.
 */

public class PointData {
    private Vector3 pos;
    private Color4 color;

    public PointData(Vector3 pos, Color4 color) {
        this.pos = pos.clone();
        this.color = color.clone();
    }

    public Vector3 getPos() {
        return pos;
    }

    public void setPos(Vector3 pos) {
        this.pos = pos.clone();
    }

    public Color4 getColor() {
        return color;
    }

    public void setColor(Color4 color) {
        this.color = color.clone();
    }
}
