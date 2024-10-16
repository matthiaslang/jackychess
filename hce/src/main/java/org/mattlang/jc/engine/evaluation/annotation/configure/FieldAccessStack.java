package org.mattlang.jc.engine.evaluation.annotation.configure;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;

/**
 * Stack of field accesses to navigate from an evaluation object down to a specific field which is associated by
 * a configuration.
 */
public class FieldAccessStack {

    private List<Field> fields = new ArrayList<>();

    public FieldAccessStack() {
    }

    public FieldAccessStack(List<Field> fields) {
        this.fields = fields;
    }

    public FieldAccessStack with(Field declaredField) {
        List<Field> newFields = new ArrayList<>(fields);
        newFields.add(declaredField);
        return new FieldAccessStack(newFields);
    }

    public Object get(ParameterizedEvaluation e) {
        Object o = e;
        for (Field field : fields) {
            o = getFieldValue(o, field);
        }
        return o;
    }

    public void set(ParameterizedEvaluation e, Object val) {
        Object o = e;
        for (int i = 0; i < fields.size() - 1; i++) {
            o = getFieldValue(o, fields.get(i));
        }
        setFieldValue(o, fields.get(fields.size() - 1), val);
    }

    public static void setFieldValue(Object eval, Field declaredField, Object intVal) {
        try {
            declaredField.setAccessible(true);
            declaredField.set(eval, intVal);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(Object eval, Field declaredField) {
        try {
            declaredField.setAccessible(true);
            return declaredField.get(eval);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
