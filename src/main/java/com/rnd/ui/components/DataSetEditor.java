package com.rnd.ui.components;


import com.google.inject.Inject;
import com.rnd.db.DocumentHandler;
import com.rnd.model.DataSet;
import com.rnd.model.Tag;
import com.rnd.model.enumerations.Key;
import com.rnd.model.enumerations.ModelAspect;
import com.rnd.model.enumerations.Source;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DataSetEditor extends CustomComponent {

    private Logger log = LoggerFactory.getLogger(DataSet.class);
    @Inject
    private DocumentHandler handler;
    private DataSet dataSet;
    private BeanFieldGroup<DataSet> binder = new BeanFieldGroup<>(DataSet.class);
    private VerticalLayout root = new VerticalLayout();
    private HorizontalLayout dataForms = new HorizontalLayout();
    private VerticalLayout buttonBar = new VerticalLayout();

    private FormLayout leftForm = new FormLayout();
    private FormLayout middleForm = new FormLayout();
    private VerticalLayout aspectContainer = new VerticalLayout();
    private VerticalLayout keyContainer = new VerticalLayout();
    private VerticalLayout tagContainer = new VerticalLayout();


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

    private TwinColSelect aspectSelect = new TwinColSelect("Aspects");
    @PropertyId("validationInfo")
    private TextField validationInfo = new TextField("Validation Information");
    @PropertyId("applicability")
    private TextField applicability = new TextField("Applicability");
    @PropertyId("description")
    private TextArea description = new TextArea("Description");


    private TextField keyName = new TextField("New Key");
    private Button addKeyButton = new Button("Add");
    private TwinColSelect keySelect = new TwinColSelect("Keys");
    private TextField newTag = new TextField("New Tag");
    private TwinColSelect tagSelect = new TwinColSelect("Tags");


    private Button save = new Button("Save");
    private Button clearAll = new Button("Clear");

    public DataSetEditor() {
        this.setId(this.getClass().getSimpleName());
        initDataSet();
        init();
    }

    void loadData(DataSet dataSet) {
        this.dataSet = dataSet;
        init();
    }

    private void clearData() {
        this.dataSet = new DataSet();
        initDataSet();
        init();
    }


    private void init() {
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

        aspectContainer.addComponent(aspectSelect);
        keyContainer.addComponent(keyName);
        keyContainer.addComponent(addKeyButton);
        keyContainer.addComponent(keySelect);
        tagContainer.addComponent(newTag);
        tagContainer.addComponent(tagSelect);

        dataForms.addComponent(buttonBar);
        dataForms.addComponent(leftForm);
        dataForms.addComponent(middleForm);
        dataForms.addComponent(aspectContainer);
        dataForms.addComponent(keyContainer);
        dataForms.addComponent(tagContainer);


        buttonBar.addComponent(clearAll);
        buttonBar.addComponent(save);
        buttonBar.setMargin(true);

        root.addComponent(dataForms);
        setCompositionRoot(root);


        binder.setItemDataSource(dataSet);

        binder.bindMemberFields(this);
        save.addClickListener(c -> {
            log.debug("saving");
            try {
                binder.commit();
                commitAspects();
                commitKeys();
                commitTags();
                handler.saveDataSet(dataSet);
            } catch (FieldGroup.CommitException e) {
                e.printStackTrace();
            }

        });
        clearAll.addClickListener(c -> this.clearData());
        addKeyButton.addClickListener(c -> addNewKey());
    }

    private void addNewKey() {
        String keyName = this.keyName.getValue();
        if (keyName.isEmpty()) {
            Notification.show("NO keyName", "Give the key a name.", Notification.Type.WARNING_MESSAGE);
            return;
        }

        Key key = new Key();
        key.setName(keyName);
        keySelect.addItem(key);
        keySelect.select(key);
    }

    private void commitAspects() {
        List<ModelAspect> selectedAspects = new ArrayList<>();
        selectedAspects.addAll((Collection<? extends ModelAspect>) aspectSelect.getValue());
        dataSet.setAspect(selectedAspects);
    }

    private void commitKeys() {
        List<Key> selectedKeys = new ArrayList<>();
        selectedKeys.addAll((Collection) keySelect.getValue());
        dataSet.setKeys(selectedKeys);
    }

    private void commitTags() {
        List<Tag> selectedTags = new ArrayList<>();
        selectedTags.addAll((Collection) tagSelect.getValue());
        dataSet.setTags(selectedTags);
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
        newTag.setNullRepresentation("");
        keyName.setNullRepresentation("");

        //field.setIcon(FontAwesome.DATABASE);


    }

    private void initializeSelectors() {
        for (Source sourceEnum : Source.values()) {
            source.addItem(sourceEnum);
            source.setItemCaption(sourceEnum, sourceEnum.getValue());
            source.setFilteringMode(FilteringMode.STARTSWITH);
        }
        aspectSelect.setValue(null);
        for (ModelAspect aspect : ModelAspect.values()) {
            aspectSelect.addItem(aspect);
            if (dataSet.getAspect() != null && dataSet.getAspect().contains(aspect.toString())) {
                aspectSelect.select(aspect);
            }
        }
        keySelect.setValue(null);
        keySelect.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        keySelect.setItemCaptionPropertyId("name");
        //FIXME add a typelistener to get postconstruct functionality.
        if (handler != null) {
            BeanItemContainer<Key> container = new BeanItemContainer<>(Key.class);
            keySelect.setContainerDataSource(container);
            for (Key key : handler.getKeys()) {
                container.addItem(key);
                if (dataSet.getAspect() != null && dataSet.getAspect().contains(key.toString())) {
                    keySelect.select(key);
                }
            }

        }
    }
}
