package com.rnd.db;


import com.google.inject.Inject;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OLiveQuery;
import com.orientechnologies.orient.core.sql.query.OLiveResultListener;
import com.rnd.model.DataSet;
import com.rnd.model.enumerations.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DocumentHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private OPartitionedDatabasePool pool;

    @Inject
    public DocumentHandler(OPartitionedDatabasePool pool) {
        this.pool = pool;
    }

    public void saveDataSet(DataSet dataSet) {
        try (ODatabaseDocumentTx db = pool.acquire()) {

            ODocument oDataSet = null;
            if (dataSet.getId() != null) {
                oDataSet = db.load(dataSet.getId());
            }
            oDataSet = DocumentConverter.convert(dataSet, oDataSet);
            oDataSet.field("name", dataSet.getName());
            oDataSet.save();
        }
    }

    public List<DataSet> loadAll() {
        try (ODatabaseDocumentTx db = pool.acquire()) {
            List<DataSet> list = new ArrayList<>();
            db.browseClass(DataSet.class.getSimpleName()).forEach(c -> list.add(DocumentConverter.convert(c)));
            return list;
        }
    }

    public void delete(ORID recordId) {
        try (ODatabaseDocumentTx db = pool.acquire()) {
            db.delete(recordId);
        }
    }

    public void registerLiveLoadAllListener(OLiveResultListener listener) {
        try (ODatabaseDocumentTx db = pool.acquire()) {

            db.query(new OLiveQuery<ODocument>("live select from DataSet", listener));
            //for unregistering
            //String token = result.get(0).field("token");

        }
    }

    public void logAll() {
        try (ODatabaseDocumentTx db = pool.acquire()) {
            db.browseClass(DataSet.class.getSimpleName()).forEach(d -> log.debug("logall: {}", d.fieldNames()));
        }
    }

    public List<Key> getKeys() {
        List<Key> keys = new ArrayList<>();
        try (ODatabaseDocumentTx db = pool.acquire()) {
            for (ODocument keyDoc : db.browseClass(Key.class.getSimpleName())) {
                Key key = new Key();
                key.setName(keyDoc.field("name"));
                key.setType(keyDoc.field("type"));
                key.setRelated(keyDoc.field("related"));
                keys.add(key);
            }
        }
        return keys;
    }


}
