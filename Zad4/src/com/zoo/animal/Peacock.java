package com.zoo.animal;

public class Peacock extends Animal {
    protected void Say() {
        System.out.println("Peacock say");
    }

    protected void move() {
        System.out.println("flying");
    }

    protected void giveFood() {
        System.out.println("grains");
    }

    public Peacock(){
        super();
    }
    public Peacock(String name) {
        super(name);
    }
}

