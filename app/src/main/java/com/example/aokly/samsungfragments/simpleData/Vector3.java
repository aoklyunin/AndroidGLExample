package com.example.aokly.samsungfragments.simpleData;

/**
 * Created by aokly on 11.02.2017.
 */
public class Vector3 {
    private float x;
    private float y;
    private float z;

    protected Vector3 clone(){
        return  new Vector3(x,y,z);
    }
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getX() {

        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Vector3(float x, float y, float z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }


}
