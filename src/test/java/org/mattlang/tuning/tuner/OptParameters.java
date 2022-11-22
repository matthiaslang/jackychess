package org.mattlang.tuning.tuner;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.mattlang.jc.tools.MarkdownWriter;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OptParameters {

    /**
     * the paramset name to tune.
     */
    private String evalParamSet = "TUNED01";
    private final List<String> inputFiles;

    private boolean multiThreading = true;

    private boolean adjustK = false;

    private boolean tuneMaterial = true;

    private boolean tuneMobility = true;
    private boolean tunePst = true;
    private boolean tuneAdjustments = true;

    private boolean removeDuplicateFens = true;

    private int threadCount = 7;

    public void writeMarkdownInfos(MarkdownWriter mdWriter)
            throws IOException {
        mdWriter.h1("Tuning Options");
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(OptParameters.class).getPropertyDescriptors()) {
                if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                    Object value = pd.getReadMethod().invoke(this);
                    mdWriter.paragraph(pd.getName() + " = " + value);
                }
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}