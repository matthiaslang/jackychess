package org.mattlang.jc.util;

import java.util.Set;

import org.junit.Test;

public class PropertyConfigTest {

    @Test
    public void testProps(){
        PropertyConfig config=new PropertyConfig();

        Set<String> all = config.getStringPropertyNames();
        for (String s : all) {
            System.out.println(s + "= " + config.getOptProperty(s));
        }
    }
}