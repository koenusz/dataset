package com.rnd.ui.components;

import com.rnd.model.enumerations.Key;
import com.vaadin.data.Property;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

public class KeyColumnGenerator implements CustomTable.ColumnGenerator {

    @Override
    public Object generateCell(CustomTable customTable, Object itemId, Object columnId) {
        // Get the object stored in the cell as a property
        Property<List<Key>> prop =
                customTable.getItem(itemId).getItemProperty(columnId);
        VerticalLayout cellLayout = new VerticalLayout();
        prop.getValue().stream().forEach(key -> cellLayout.addComponent(new KeyPanel(key)));
        return cellLayout;
    }
}

