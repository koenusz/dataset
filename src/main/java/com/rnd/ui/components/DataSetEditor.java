package com.rnd.ui.components;


import com.google.inject.Inject;
import com.rnd.db.DocumentHandler;
import com.rnd.model.DataSet;
import com.rnd.model.enumerations.ModelAspect;
import com.rnd.model.enumerations.Source;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DataSetEditor extends CustomComponent {

    private Logger log = LoggerFactory.getLogger(DataSet.class);
    @Inject
    private DocumentHandler handler;
    private DataSet dataSet;
    private BeanFieldGroup<DataSet> binder = new BeanFieldGroup(DataSet.class);
    private VerticalLayout root = new VerticalLayout();
    private HorizontalLayout dataForms = new HorizontalLayout();
    private HorizontalLayout buttonBar = new HorizontalLayout();
    private FormLayout leftForm = new FormLayout();
    private FormLayout middleForm = new FormLayout();
    private FormLayout rightForm = new FormLayout();
    @PropertyId("operator")
    private TextField operator = new TextField("Operator");
    @PropertyId("dateChecked")
    private DateField dateChecked = new DateField("Date checked");
    @PropertyId("version")
    private TextField version = new TextField("Version");
    @PropertyId("name")
    //@NotNull
    private TextField name = new TextField("Name");
    @PropertyId("url")
    private TextField url = new TextField("URL");
    @PropertyId("author")
    private TextField author = new TextField("Author");
    @PropertyId("source")
    private ComboBox source = new ComboBox("Source");
    @PropertyId("neutral")
    private TextField neutral = new TextField("Neutral");
    @PropertyId("structured")
    private CheckBox structured = new CheckBox("Structured");
    //@PropertyId("aspects")
    private TwinColSelect aspects = new TwinColSelect("Aspects");
    @PropertyId("validationInfo")
    private TextField validationInfo = new TextField("Validation Information");
    @PropertyId("applicability")
    private TextField applicability = new TextField("Applicability");
    @PropertyId("description")
    private TextArea description = new TextArea("Description");
    // @PropertyId("keys")
    private TwinColSelect keys = new TwinColSelect("Keys");
    // @PropertyId("tags")
    private TwinColSelect tags = new TwinColSelect("Tags");


    private Button save = new Button("Save");
    private Button test = new Button("Load test");

    public DataSetEditor() {
        this.setId(this.getClass().getSimpleName());
        init();
    }

    void loadData(DataSet dataSet) {
        this.dataSet = dataSet;
        init();
    }

    private void init() {
        initDataSet();
        instantiateTextFields();
        initializeSelectors();
        renderFormComponents();
    }

    private void initDataSet() {
        dataSet = new DataSet();
        dataSet.setDateChecked(new Date());
        dataSet.setOperator("Koen Sugeng");
    }

    private void renderFormComponents() {


        leftForm.setCaption("Data Set Editor");
        leftForm.addComponent(operator);
        leftForm.addComponent(version);
        leftForm.addComponent(name);
        leftForm.addComponent(dateChecked);
        leftForm.addComponent(url);
        leftForm.addComponent(author);
        leftForm.addComponent(source);
        leftForm.addComponent(neutral);


        middleForm.addComponent(structured);
        middleForm.addComponent(description);
        middleForm.addComponent(validationInfo);
        middleForm.addComponent(applicability);

        rightForm.addComponent(aspects);
        rightForm.addComponent(keys);
        rightForm.addComponent(tags);

        dataForms.addComponent(leftForm);
        dataForms.addComponent(middleForm);
        dataForms.addComponent(rightForm);


        buttonBar.addComponent(save);
        buttonBar.addComponent(test);

        root.addComponent(dataForms);
        root.addComponent(buttonBar);
        setCompositionRoot(root);


        binder.setItemDataSource(dataSet);

        binder.bindMemberFields(this);
        save.addClickListener(c -> {
            log.debug("saving");
            try {
                binder.commit();
                handler.saveDataSet(dataSet);
            } catch (FieldGroup.CommitException e) {
                e.printStackTrace();
            }

        });

        //FIXME: remove testing code
        test.addClickListener(c ->
                handler.loadAll().forEach(d -> leftForm.addComponent(new Label("loaded: " + d.getName())))
        );
    }

    private void instantiateTextFields() {

        name.addValidator(new NullValidator("Must be given", false));

        operator.setNullRepresentation("");
        version.setNullRepresentation("");
        name.setNullRepresentation("");
        url.setNullRepresentation("");
        author.setNullRepresentation("");
        neutral.setNullRepresentation("");
        validationInfo.setNullRepresentation("");
        applicability.setNullRepresentation("");
        description.setNullRepresentation("");

        //field.setIcon(FontAwesome.DATABASE);


    }

    private void initializeSelectors() {
        for (Source sourceEnum : Source.values()) {
            source.addItem(sourceEnum);
            source.setItemCaption(sourceEnum, sourceEnum.getValue());
            source.setFilteringMode(FilteringMode.STARTSWITH);
        }
        for (ModelAspect aspect : ModelAspect.values()) {
            aspects.addItem(aspect);
        }

    }
}
