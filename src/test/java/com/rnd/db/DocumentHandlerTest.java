package com.rnd.db;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.rnd.model.DataSet;
import com.rnd.model.enumerations.Key;
import com.rnd.setup.OrientDBModule;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class DocumentHandlerTest {

    static final String LOCALURL = "plocal:datasetTest";

    Injector sInjector;
    @Inject
    DocumentHandler documentHandler;


    @Before
    public void setup() {
        sInjector = Guice.createInjector(new OrientDBModule(LOCALURL, "admin", "admin"));
        sInjector.injectMembers(this);


    }

    private DataSet createDataSet() {
        DataSet set = new DataSet();
        set.setName("test name");
        return set;
    }

    @Test
    public void saveAndLoad() {
        DataSet dataSet = createDataSet();
        documentHandler.saveDataSet(dataSet);
        documentHandler.logAll();
    }

    @Test
    public void saveKey() {
        DataSet dataSet = createDataSet();
        Key key = new Key();
        key.setName("test key");
        dataSet.setKeys(Collections.singletonList(key));
        documentHandler.saveDataSet(dataSet);
        documentHandler.logAll();
    }

//    class OrientModuleInitializer extends AbstractModule {
//        @Override
//        protected void configure() {
//            bind(OrientDBModule.class).toInstance(new OrientDBModule("plocal:datasetTest", "admin", "admin"));
//        }
//    }
}
