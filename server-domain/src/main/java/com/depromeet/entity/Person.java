package com.depromeet.entity;

public class Person {

    private String name;
    private String message;

    public Person(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}