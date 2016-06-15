package com.rnd.ui.components;

import com.google.inject.Inject;
import com.rnd.db.DocumentHandler;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

public class DataSetViewer extends CustomComponent {

    private Panel panel = new Panel();
    private Table table = new Table();

    @Inject
    private DocumentHandler handler;

    public DataSetViewer() {
    }

}
