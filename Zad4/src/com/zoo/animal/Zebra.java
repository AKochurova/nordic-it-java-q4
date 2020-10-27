package com.zoo.animal;

public class Zebra extends Animal {
    protected void Say() {
        System.out.println("Zebra say");
    }

    protected void move() {
        System.out.println("galloping");
    }

    protected void giveFood() {
        System.out.println("grass");
    }

    public Zebra(){
        super();
    }
    public Zebra(String name) {
        super(name);
    }
}
