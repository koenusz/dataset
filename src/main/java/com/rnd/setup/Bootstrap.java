package com.rnd.setup;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import javax.servlet.annotation.WebListener;

@WebListener
public class Bootstrap extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                serve("/*").with(DataSetServlet.class);

            }
        }, new OrientDBModule("remote:192.168.99.100:32769/dataset", "admin", "admin"));
    }
}