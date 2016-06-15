package com.rnd.setup;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private final static String LOCALURL = "plocal:dataset";
    private static Injector sInjector;
    @Inject
    private Injector injector;

    //private final static String DOCKERURL = "remote:192.168.99.100:32773/dataset";

    public static void main(String... args) {
        /*
		 * Guice.createInjector() takes your Modules, and returns a new Injector
		 * instance. Most applications will call this method exactly once, in
		 * their main() method.
		 */
        sInjector = Guice.createInjector(new OrientDBModule(LOCALURL, "admin", "admin"));

        Application a = sInjector.getInstance(Application.class);

        a.init();
    }

    private void init() {


    }

}
