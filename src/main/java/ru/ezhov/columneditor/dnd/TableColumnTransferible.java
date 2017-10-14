package ru.ezhov.columneditor.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import ru.ezhov.columneditor.columnsobjects.DataOwnerColumnInfo;

/**
 * Данный класс, реализует объект для перетаскивания
 *
 * @author rrndeonisiusezh
 */
public class TableColumnTransferible implements Transferable
{

    private final transient DataOwnerColumnInfo tableColumn;

    public TableColumnTransferible(DataOwnerColumnInfo tableColumn)
    {
        this.tableColumn = tableColumn;
    }

    private final DataFlavor[] dataFlMass = new DataFlavor[]
    {
        OwnDataFlavor.DATA_FLAVOR_TABLE_COLUMN
    };

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return dataFlMass;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return Arrays.asList(dataFlMass).contains(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        return tableColumn;
    }

}
