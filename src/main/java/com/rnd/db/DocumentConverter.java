package com.rnd.db;

import com.google.common.base.Defaults;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.rnd.model.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


public class DocumentConverter {


    private static Logger log = LoggerFactory.getLogger(DocumentConverter.class);

    public static DataSet convert(ODocument doc) {
        DataSet set = new DataSet();
        set.setId(doc.getIdentity());

        //PropertyDescriptor value = new PropertyDescriptor("name", Person.class).getReadMethod().invoke(person);

        Field[] fields = set.getClass().getDeclaredFields();

        for (Field declaredField : fields) {
            log.debug("converting {} ", declaredField.getName());

            if (declaredField.getName().equals("id") == false) {
                try {
                    Object value = doc.field(declaredField.getName());
                    if (value == null && declaredField.getType().isPrimitive()) {
                        value = Defaults.defaultValue(declaredField.getType());
                    } else if (value != null && declaredField.getType().isEnum()) {
                        value = Enum.valueOf((Class<Enum>) declaredField.getType(), (String) value);

                    }


                    new PropertyDescriptor(declaredField.getName(), DataSet.class).getWriteMethod().invoke(set, value);
                } catch (InvocationTargetException | IllegalAccessException | IntrospectionException e) {
                    e.printStackTrace();
                }
            }
        }
        return set;
    }

    public static ODocument convert(DataSet set, ODocument doc) {
        if (doc == null) {
            doc = new ODocument(set.getClass().getSimpleName());
        }

        Field[] fields = set.getClass().getDeclaredFields();
        for (Field declaredField : fields) {
            if (declaredField.getName().equals("id") == false) {
                try {
                    Object value = new PropertyDescriptor(declaredField.getName(), DataSet.class).getReadMethod().invoke(set);
                    doc.field(declaredField.getName(), value);
                } catch (InvocationTargetException | IllegalAccessException | IntrospectionException e) {
                    e.printStackTrace();
                }
            }
        }
        return doc;
    }
}
