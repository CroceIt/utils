package com.joe.utils.poi.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Cell;

/**
 * excel数据
 *
 * @author joe
 * @version 2018.06.14 11:46
 */
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ExcelData<T> {
    /**
     * 数据
     */
    protected T data;

    /**
     * 根据给定数据构建一个excel数据，给定数据不做校验
     *
     * @param object 数据
     * @param <D>    excel数据实际类型
     * @return 构建的excel单元格数据
     */
    public abstract <D extends ExcelData<T>> D build(Object object);

    /**
     * 将数据写入单元格
     *
     * @param cell 单元格
     */
    public abstract void write(Cell cell);

    /**
     * 数据是否可写
     *
     * @param data 要写入的数据
     * @return 返回true表示可写
     */
    public abstract boolean writeable(Object data);

    /**
     * 数据类型是否可写
     *
     * @param type 数据类型
     * @return 返回true表示可写
     */
    public abstract boolean writeable(Class<?> type);

    /**
     * 设置数据，不做检查，默认认为数据是该类可以处理的，不能处理时可以抛出异常
     *
     * @param data 数据
     */
    public abstract void setData(Object data);
}