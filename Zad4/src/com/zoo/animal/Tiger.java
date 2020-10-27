package com.zoo.animal;

public class Tiger extends Animal {
    protected void Say() {
        System.out.println("Tiger say");
    }

    protected void move() {
        System.out.println("running");
    }

    protected void giveFood() {
        System.out.println("meat");
    }

    public Tiger(){
        super();
    }
    public Tiger(String name) {
        super(name);
    }
}
