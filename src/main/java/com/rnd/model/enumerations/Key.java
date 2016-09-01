package com.rnd.model.enumerations;

import com.orientechnologies.orient.core.id.ORID;
import lombok.Data;

@Data
public class Key {

    private ORID id;

    private Type type;
    private String name;
    //  private List<Key> related;

    public enum Type {PRIVATE, FOREIGN, UNIQUE}
}
