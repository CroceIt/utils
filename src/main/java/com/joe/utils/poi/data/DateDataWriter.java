package com.joe.utils.poi.data;

import com.joe.utils.poi.ExcelDataWriter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Date;

/**
 * 日期数据
 *
 * @author joe
 * @version 2018.06.14 11:49
 */
public final class DateDataWriter implements ExcelDataWriter<Date> {
    @Override
    public void write(Cell cell, Date data) {
        cell.setCellValue(data);
    }

    @Override
    public boolean writeable(Object data) {
        return (data instanceof Date);
    }

    @Override
    public boolean writeable(Class<?> type) {
        if (type != null && type.equals(Date.class)) {
            return true;
        }
        return false;
    }
}
