package com.zoo.animal;

public class Hippo extends Animal {
    protected void Say() {
        System.out.println("Hippo say");
    }

    protected void move() {
        System.out.println("swimming");
    }

    protected void giveFood() {
        System.out.println("vegetables");
    }

    public Hippo(){
        super();
    }
    public Hippo(String name) {
        super(name);
    }
}
