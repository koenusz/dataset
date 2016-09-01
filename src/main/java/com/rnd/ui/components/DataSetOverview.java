package com.rnd.ui.components;

import com.google.inject.Inject;
import com.orientechnologies.common.exception.OException;
import com.orientechnologies.orient.core.db.record.ORecordOperation;
import com.orientechnologies.orient.core.sql.query.OLiveResultListener;
import com.rnd.db.DocumentHandler;
import com.rnd.model.DataSet;
import com.rnd.ui.util.VaadinUtils;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tepi.filtertable.FilterTable;

import java.util.ArrayList;
import java.util.List;

public class DataSetOverview extends CustomComponent implements OLiveResultListener {

    String KEY_PROPERTY = "Keys";

    HorizontalLayout layout = new HorizontalLayout();
    private Logger log = LoggerFactory.getLogger(DataSetOverview.class);
    private FilterTable table = new FilterTable("DataSet");
    ;

    private VerticalLayout buttonBar = new VerticalLayout();
    private Button deleteButton = new Button("Delete");


    private DocumentHandler handler;
    private List<DataSet> datasets;

    @Inject
    public DataSetOverview(DocumentHandler handler) {
        this.handler = handler;
        loadData();
        configure();
        populateTable();
        handler.registerLiveLoadAllListener(this);
    }

    public void loadData() {
        datasets = handler.loadAll();
    }

    private void configure() {
        layout.setSpacing(true);
        layout.setMargin(true);
        buttonBar.setMargin(true);


        this.setCompositionRoot(layout);
        layout.addComponent(buttonBar);
        layout.addComponent(table);


        table.setSelectable(true);
        table.setImmediate(true);
        table.addItemClickListener(itemClickEvent ->
        {
            DataSetEditor editor = VaadinUtils.findById(this.getUI(), DataSetEditor.class);
            editor.loadData((DataSet) ((BeanItem) itemClickEvent.getItem()).getBean());
        });

        deleteButton.addClickListener(clickEvent -> handler.delete(((DataSet) table.getValue()).getId()));

        buttonBar.addComponent(deleteButton);

    }


    private void populateTable() {

        BeanItemContainer<DataSet> container = new BeanItemContainer<>(DataSet.class, datasets);
        table.setContainerDataSource(container);
        table.setFilterBarVisible(true);
        java.lang.reflect.Field[] declaredFields = DataSet.class.getDeclaredFields();

        List<String> columns = new ArrayList<>();

        for (java.lang.reflect.Field declaredField : declaredFields) {
            if (declaredField.getName().equals("id") == false) {
                columns.add(declaredField.getName());
            }
        }


        table.setVisibleColumns(columns.toArray());
        table.setColumnHeaders(columns.toArray(new String[columns.size()]));
        table.sort(new Object[]{"id"}, new boolean[]{
                true, true});
        table.addGeneratedColumn("keys", new KeyColumnGenerator());
    }


    @Override
    public void onLiveResult(int iLiveToken, ORecordOperation iOp) throws OException {
        log.debug("recieved " + iOp.getRecord());
        this.getUI().access(() -> {
            this.loadData();
            this.populateTable();
        });
    }

    @Override
    public void onError(int iLiveToken) {
        Notification.show("Dataset live Query error",
                "iLiveToken " + iLiveToken,
                Notification.Type.ERROR_MESSAGE);
    }

    @Override
    public void onUnsubscribe(int iLiveToken) {

    }
}
