package com.joe.utils.poi.data;

import com.joe.utils.poi.ExcelDataWriter;
import org.apache.poi.ss.usermodel.Cell;

/**
 * @author joe
 * @version 2018.06.14 11:50
 */
public final class BooleanDataWriter implements ExcelDataWriter<Boolean> {
    @Override
    public void write(Cell cell, Boolean data) {
        cell.setCellValue(data);
    }

    @Override
    public boolean writeable(Object data) {
        return (data instanceof Boolean);
    }

    @Override
    public boolean writeable(Class<?> type) {
        if (type == null) {
            return false;
        }
        if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return true;
        }
        return false;
    }

}
