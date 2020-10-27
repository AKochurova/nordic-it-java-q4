package com.zoo.animal;

public class Lion extends Animal {
    protected void Say() {
        System.out.println("Lion say");
    }

    protected void move() {
        System.out.println("crawling");
    }

    protected void giveFood() {
        System.out.println("meat");
    }

    public Lion(){
        super();
    }
    public Lion(String name) {
        super(name);
    }
}
