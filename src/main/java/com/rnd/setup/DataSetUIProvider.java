package com.rnd.setup;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.rnd.ui.DataSetUI;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;

public class DataSetUIProvider extends UIProvider {
    private static final long serialVersionUID = 5821024521446127058L;

    @Inject
    private Injector injector;

    @Override
    public DataSetUI createInstance(UICreateEvent event) {
        return injector.getInstance(DataSetUI.class);
    }

    @Override
    public Class<DataSetUI> getUIClass(UIClassSelectionEvent event) {
        return DataSetUI.class;
    }
}