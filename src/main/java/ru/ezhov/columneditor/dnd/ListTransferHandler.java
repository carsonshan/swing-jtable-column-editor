package ru.ezhov.columneditor.dnd;
import java.awt.datatransfer.Transferable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import ru.ezhov.columneditor.columnsobjects.DataOwnerColumnInfo;
/**
 * Этот класс отвечает за перетаскивание элементов между списками
 *
 * @author rrndeonisiusezh
 */
public class ListTransferHandler extends TransferHandler
{
    private static final Logger LOG = Logger.getLogger(ListTransferHandler.class.getName());
    /** индекс компонента при передаче */
    private int indexMove;
    @Override
    public int getSourceActions(JComponent c)
    {
        return TransferHandler.MOVE;
    }
    @Override
    public boolean canImport(TransferSupport support)
    {
        return support.isDataFlavorSupported(OwnDataFlavor.DATA_FLAVOR_TABLE_COLUMN);
    }
    @Override
    protected Transferable createTransferable(JComponent c)
    {
        JList list = (JList) c;
        final DataOwnerColumnInfo tableColumn = (DataOwnerColumnInfo) list.getSelectedValue();
        TableColumnTransferible transferable = new TableColumnTransferible(tableColumn);
        indexMove = list.getSelectedIndex();
        return transferable;
    }
    @Override
    public boolean importData(final TransferSupport support)
    {
        if (!canImport(support))
        {
            return false;
        }
        TransferSupport sup = support;
        JList componentTarget = (JList) sup.getComponent();
        try
        {
            final DefaultListModel modelTarget = (DefaultListModel) componentTarget.getModel();
            final DataOwnerColumnInfo dataOwnerColumnInfo;
            final int index = componentTarget.locationToIndex(sup.getDropLocation().getDropPoint());
            dataOwnerColumnInfo = (DataOwnerColumnInfo) sup.getTransferable().getTransferData(OwnDataFlavor.DATA_FLAVOR_TABLE_COLUMN);
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    modelTarget.removeElement(dataOwnerColumnInfo);
                    if (modelTarget.isEmpty())
                    {
                        modelTarget.addElement(dataOwnerColumnInfo);
                    } else
                    {
                        modelTarget.add(index, dataOwnerColumnInfo);
                    }
                }
            });
            return true;
        } catch (Exception ex)
        {
            Logger.getLogger(ListTransferHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    @Override
    protected void exportDone(JComponent source, Transferable data, int action)
    {
        if (action == getSourceActions(source))
        {
            try
            {
                final JList list = (JList) source;
                final DataOwnerColumnInfo dataOwnerColumnInfo = (DataOwnerColumnInfo) data.getTransferData(OwnDataFlavor.DATA_FLAVOR_TABLE_COLUMN);
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        DefaultListModel defaultListModel = (DefaultListModel) list.getModel();
                        /* если индекс перемещаемого элемента равен текущему, тогда удаляем текущий элемент */
                        if (indexMove == defaultListModel.indexOf(dataOwnerColumnInfo))
                        {
                            defaultListModel.removeElement(dataOwnerColumnInfo); //удаляем его
                        }
                    }
                });
            } catch (Exception ex)
            {
                Logger.getLogger(ListTransferHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
