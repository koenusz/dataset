package com.rnd.model;

import com.orientechnologies.orient.core.id.ORID;
import lombok.Data;

@Data
public class Tag {

    private ORID id;

    private String name;

}
