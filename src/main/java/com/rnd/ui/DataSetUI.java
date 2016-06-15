package com.rnd.ui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.rnd.ui.components.DataSetEditor;
import com.rnd.ui.components.DataSetOverview;
import com.vaadin.annotations.Push;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;

import java.io.File;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Push
public class DataSetUI extends UI {

    @Inject
    Injector injector;

    private VerticalLayout layout = new VerticalLayout();
    private HorizontalLayout header = new HorizontalLayout();
    private HorizontalLayout body = new HorizontalLayout();
    private VerticalLayout leftMenu = new VerticalLayout();
    private VerticalLayout contends = new VerticalLayout();


    @Override
    protected void init(VaadinRequest vaadinRequest) {

        layout.addComponent(header);
        layout.addComponent(body);
        body.addComponent(leftMenu);
        body.addComponent(contends);

        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        FileResource resource = new FileResource(new File(basepath + "/images/rnd.png"));

        // Show the image in the application
        Image image = new Image("", resource);
        header.addComponent(image);
        header.setId("header");
        //TODO: get styles to work
        header.setStyleName("background-white");


        //FIXME: put something usefull in
        leftMenu.setMargin(true);
        leftMenu.addComponent(new Button("Buttons go here"));


        contends.addComponent(injector.getInstance(DataSetOverview.class));
        contends.addComponent(injector.getInstance(DataSetEditor.class));

        contends.setMargin(true);
        contends.setSpacing(true);

        setContent(layout);

    }

}
