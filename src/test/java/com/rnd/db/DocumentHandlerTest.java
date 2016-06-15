package com.rnd.db;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.rnd.model.DataSet;
import com.rnd.setup.OrientDBModule;
import org.junit.Before;
import org.junit.Test;

public class DocumentHandlerTest {

    static final String LOCALURL = "plocal:datasetTest";

    Injector sInjector;
    @Inject
    DocumentHandler documentHandler;

    DataSet dataSet;

    @Before
    public void setup() {
        sInjector = Guice.createInjector(new OrientDBModule(LOCALURL, "admin", "admin"));
        sInjector.injectMembers(this);
        dataSet = new DataSet();
        dataSet.setName("my testing name");
    }

    @Test
    public void saveAndLoad() {
        documentHandler.saveDataSet(dataSet);
        documentHandler.logAll();
    }

    class OrientModuleInitializer extends AbstractModule {
        @Override
        protected void configure() {
            bind(OrientDBModule.class).toInstance(new OrientDBModule("plocal:datasetTest", "admin", "admin"));
        }
    }
}
