package com.rnd.db;

import com.google.common.base.Defaults;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.rnd.model.DataSet;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
                        //TODO store enums as strings not as int
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

    public static ODocument convert(Object set, ODocument doc) {
        if (doc == null) {
            doc = new ODocument(set.getClass().getSimpleName());
        }

        Field[] fields = set.getClass().getDeclaredFields();
        for (Field declaredField : fields) {
            if (declaredField.getName().equals("id") == false) {
                try {
                    Object value = new PropertyDescriptor(declaredField.getName(), set.getClass()).getReadMethod().invoke(set);

                    if (value != null && Collection.class.isAssignableFrom(declaredField.getType())) {
                        ParameterizedType listType = (ParameterizedType) declaredField.getGenericType();
                        //this only works if the list has only objects of the same type.
                        Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
                        if (needsConverterType(listClass)) {
                            //TODO add support for other collection types
                            value = convertList((List<?>) value);
                        }
                    }
                    doc.field(declaredField.getName(), value);

                } catch (InvocationTargetException | IllegalAccessException | IntrospectionException e) {
                    e.printStackTrace();
                }
            }
        }
        return doc;
    }

    private static List<OIdentifiable> convertList(List<?> list) {

        List<OIdentifiable> result = new ArrayList<>();
        for (Object o : list) {
            result.add(convert(o, null));
        }

        //list.stream().forEach(object -> result.add(convert(object, null)));
        log.debug(list.toString());
        return result;
    }


    private static boolean needsConverterType(Class<?> clazz) {

        return !ClassUtils.isPrimitiveOrWrapper(clazz) && !clazz.equals(String.class) && !clazz.isEnum();
    }
}
