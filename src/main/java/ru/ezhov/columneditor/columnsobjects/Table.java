package ru.ezhov.columneditor.columnsobjects;

import java.util.List;

/**
 * Этот класс представляет из себя таблицу со столбцами
 *
 * @author rrndeonisiusezh
 */
public class Table
{
    /**название таблицы*/
    private String name;
    /**список столбов*/
    private List<DataOwnerColumnInfo> columns;

    public Table(String name, List<DataOwnerColumnInfo> columns)
    {
        this.name = name;
        this.columns = columns;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<DataOwnerColumnInfo> getColumns()
    {
        return columns;
    }

    public void setColumns(List<DataOwnerColumnInfo> columns)
    {
        this.columns = columns;
    }

}
