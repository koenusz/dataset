package com.rnd.setup;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

public class OrientDBModule extends AbstractModule {

    // private static final Logger logger =
    // LoggerFactory.getLogger(OrientDBModule.class);

    private final OPartitionedDatabasePool pool;

    public OrientDBModule(String url, String userName, String password) {
        super();
        pool = new OPartitionedDatabasePool(url, userName, password);
        pool.setAutoCreate(true);
    }

    @Provides
    public OPartitionedDatabasePool providesPool() {
        return pool;
    }

    @Override
    protected void configure() {
        try (OObjectDatabaseTx db = new OObjectDatabaseTx(pool.acquire())) {
            db.getEntityManager().registerEntityClasses("com.rnd.model");
        }
    }

//	private void addCluster(String clusterName) {
//		try (OObjectDatabaseTx db = new OObjectDatabaseTx(pool.acquire())) {
//			if (db.getClusterIdByName(clusterName) == -1) {
//				db.addCluster(clusterName);
//				db.getMetadata().getSchema().getClass(PersistentEntity.class).addCluster(clusterName);
//			}
//		}
//	}


}
