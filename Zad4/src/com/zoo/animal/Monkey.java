package com.zoo.animal;

public class Monkey extends Animal {
    protected void Say() {
        System.out.println("Monkey say");
    }

    protected void move() {
        System.out.println("jumping");
    }

    protected void giveFood() {
        System.out.println("banana");
    }

    public Monkey(){
        super();
    }
    public Monkey(String name) {
        super(name);
    }
}
