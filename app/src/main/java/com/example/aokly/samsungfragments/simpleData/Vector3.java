package com.example.aokly.samsungfragments.simpleData;
public class Vector3 {
    private float x;
    private float y;
    private float z;

    protected Vector3 clone() {
        return new Vector3(x, y, z);
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

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
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

    public Vector3(float[] f) {
        this.x = f[0];
        this.y = f[1];
        this.z = f[2];
    }

    public Vector3 sum(Vector3 v) {
        return new Vector3(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public Vector3 mul(float f) {
        return new Vector3(this.x * f, this.y * f, this.z * f);
    }


}
