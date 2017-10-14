package ru.ezhov.columneditor.dnd;

import java.awt.datatransfer.DataFlavor;
import ru.ezhov.columneditor.columnsobjects.DataOwnerColumnInfo;

/**
 *
 * @author rrndeonisiusezh
 */
public class OwnDataFlavor
{

    public static final DataFlavor DATA_FLAVOR_TABLE_COLUMN = new DataFlavor(DataOwnerColumnInfo.class, "column");
}
