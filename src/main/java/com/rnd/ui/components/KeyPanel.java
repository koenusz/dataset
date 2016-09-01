package com.rnd.ui.components;


import com.rnd.model.enumerations.Key;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class KeyPanel extends Panel {


    Key key = new Key();

    public KeyPanel(Key key) {
        HorizontalLayout content = new HorizontalLayout();
        this.setContent(content);
        this.key = key;
        Label nameLabel = new Label(key.getName());
        Label typeLabel = new Label(key.getType().name());
    }


    public Key getKey() {
        return key;
    }

}
