package org.mattlang.jc.command.commands;

import org.mattlang.tuning.DataSet;

public interface DataSetConsumer {

    public void accept(DataSet dataSet) throws Exception;
}
