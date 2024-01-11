package com.snake.Model;
public class DoubleVector
{
    double x, y;

    public DoubleVector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }


    public DoubleVector(Vector vec)
    {
        this.x = vec.x;
        this.y = vec.y;
    }

    public DoubleVector subtract(DoubleVector v)
    {
        return new DoubleVector(this.x - v.x, this.y - v.y);
    }

    public double dotProduct(DoubleVector v)
    {
        return this.x * v.x + this.y * v.y;
    }

    public DoubleVector add(DoubleVector other)
    {
        return new DoubleVector(this.x + other.x, this.y + other.y);
    }

    public DoubleVector multiply(double t)
    {
        return new DoubleVector(this.x * t, this.y * t);
    }

    public double distance(DoubleVector v)
    {
        return Math.sqrt(Math.pow(this.x - v.x, 2) + Math.pow(this.y - v.y, 2));
    }
}
