package com.rnd.setup;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.rnd.ui.DataSetUI;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;

@Singleton
@VaadinServletConfiguration(productionMode = false, ui = DataSetUI.class, widgetset = "com.rnd.DataSetWidgetSet")
public class DataSetServlet extends VaadinServlet implements SessionInitListener {

    private static final long serialVersionUID = 4514824431070027087L;
    protected final DataSetUIProvider applicationProvider;

    @Inject
    public DataSetServlet(DataSetUIProvider applicationProvider) {
        this.applicationProvider = applicationProvider;
    }

    @Override
    protected void servletInitialized() {
        getService().addSessionInitListener(this);
    }

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        event.getSession().addUIProvider(applicationProvider);
    }
}