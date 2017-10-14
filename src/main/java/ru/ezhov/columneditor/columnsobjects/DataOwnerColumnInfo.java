package ru.ezhov.columneditor.columnsobjects;

/**
 * Этот объект по сути является основням держателем
 * информации о столбцах и именно им мы будем манипулировать в листах
 *
 * @author rrndeonisiusezh
 */
public class DataOwnerColumnInfo
{

    /** название столбца */
    private final String nameColumn;
    /** номер столбца */
    private final int numColumn;

    public DataOwnerColumnInfo(String nameColumn, int numColumn)
    {
        this.nameColumn = nameColumn;
        this.numColumn = numColumn;
    }

    public String getNameColumn()
    {
        return nameColumn;
    }

    public int getNumColumn()
    {
        return numColumn;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof DataOwnerColumnInfo)
        {
            if (this.nameColumn.equals(((DataOwnerColumnInfo) obj).getNameColumn())
                    & this.numColumn == ((DataOwnerColumnInfo) obj).getNumColumn())
            {
                return true;
            }
        }
        return false;
    }

}
