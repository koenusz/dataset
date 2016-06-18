package com.rnd.model;


import com.orientechnologies.orient.core.id.ORID;
import com.rnd.model.enumerations.Key;
import com.rnd.model.enumerations.ModelAspect;
import com.rnd.model.enumerations.Source;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DataSet {

    private ORID id;

    private String operator;

    private Date dateChecked;

    private String version;

    private String name;

    private String url;

    private String author;

    private Source source;

    private String neutral;

    private boolean structured;

    private List<ModelAspect> aspect;

    private String validationInfo;

    private String applicability;

    private String description;

    private List<Key> keys;

    private List<Tag> tags;

}
