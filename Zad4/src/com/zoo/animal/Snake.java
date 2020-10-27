package com.zoo.animal;

public class Snake extends Animal {
    protected void Say() {
        System.out.println("Snake say");
    }

    protected void move() {
        System.out.println("crawling");
    }

    protected void giveFood() {
        System.out.println("mouse");
    }

    public Snake(){
        super();
    }
    public Snake(String name) {
        super(name);
    }
}
