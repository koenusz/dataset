package com.rnd.model.enumerations;

import lombok.Data;

import java.util.List;

@Data
public class Key {

    private Type type;
    private String name;
    private List<Key> related;

    public enum Type {PRIVATE, FOREIGN, UNIQUE}
}
