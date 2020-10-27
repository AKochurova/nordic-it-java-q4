package com.zoo.animal;

public class Animal {
    protected String name = "Animal";

    protected void Say() {
        System.out.println("say");
    }

    //два общих метода
    protected void walk() {
        System.out.println("walk");
    }

    protected void eat() {
        System.out.println("eat");
    }

    //два метода, которые отличаются у всех животных
    protected void move() {
        System.out.println("moving");
    }

    protected void giveFood() {
        System.out.println("food");
    }

    public Animal() {
        System.out.println(name);
        Say();
        walk();
        eat();
        move();
        giveFood();
    }

    public Animal(String name) {
        this.name = name;
        System.out.println(name);
        Say();
        walk();
        eat();
        move();
        giveFood();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
