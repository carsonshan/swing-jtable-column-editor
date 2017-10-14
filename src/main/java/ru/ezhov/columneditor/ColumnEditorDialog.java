package ru.ezhov.columneditor;

import ru.ezhov.columneditor.columnsobjects.DataOwnerColumnInfo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableColumn;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import ru.ezhov.columneditor.dnd.ListTransferHandler;

/**
 * Класс реализовывает редактор столбцов
 * <p>
 *
 * @author rrndeonisiusezh
 */
public class ColumnEditorDialog extends JDialog {
    private static final Logger LOG = Logger.getLogger(ColumnEditorDialog.class.getName());
    /**
     * статическая переменная синглтон
     */
    private static ColumnEditorDialog INSTANCE = new ColumnEditorDialog();
    //--------------------------------------------------------------------------
    private final JList listAllColumn = new JList();
    private final JList listAllSelect = new JList();
    private final JScrollPane scrollPaneListAllColumn = new JScrollPane(listAllColumn);
    private final JScrollPane scrollPaneListAllSelect = new JScrollPane(listAllSelect);
    private final JButton buttonToAllColumn = new JButton();
    private final JButton buttonToSelect = new JButton();
    private final JButton buttonToAllColumnAll = new JButton();
    private final JButton buttonToSelectAll = new JButton();
    private final JButton buttonMoveUp = new JButton();
    private final JButton buttonMoveDown = new JButton();
    private final JButton buttonCommit = new JButton();
    private final JLabel labelAllColumn = new JLabel();
    private final JLabel labelAllSelect = new JLabel();
    //--------------------------------------------------------------------------
    private final JPanel panelBasic = new JPanel();
    private final JPanel panelTransferColumn = new JPanel();
    private final JPanel panelMoveColumn = new JPanel();
    protected Insets inset = new Insets(2, 2, 2, 2);
    //--------------------------------------------------------------------------
    private JTable table;
    private String pathToIniFile;
    private final DefaultListModel listModelColumnAll = new DefaultListModel();
    private final DefaultListModel listModelColumnSelect = new DefaultListModel();
    private String nameTable;
    private Ini ini;
    //---------------------------------------------------------------------------------------------------------------------------------------
    private Map<String, TableColumn> mapAllTableColumn;

    /**
     * закрытый конструктор
     */
    private ColumnEditorDialog() {
        init();
    }

    /**
     * закрытый конструктор
     *
     * @param owner - родитель
     */
    private ColumnEditorDialog(Frame owner) {
        super(owner);
        init();
    }

    /**
     * инициализируем редактор столбцов
     *
     * @param owner - форма родитель (может быть null)
     *              <p>
     * @return редактор столбцов
     */
    public static ColumnEditorDialog getInstance(Frame owner) {
        if (INSTANCE == null) {
            if (owner == null) {
                INSTANCE = new ColumnEditorDialog();
            } else {
                INSTANCE = new ColumnEditorDialog(owner);
            }
        }
        return INSTANCE;
    }

    /**
     * настраиваем элементы
     */
    private void init() {
        LOG.info("настраиваем элементы для редактора столбцов");
        listAllColumn.setCellRenderer(new OwnListCellRenderer());
        listAllColumn.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listAllColumn.setDragEnabled(true);
        listAllColumn.setModel(listModelColumnAll);
        listAllColumn.setDropMode(DropMode.INSERT);
        listAllSelect.setCellRenderer(new OwnListCellRenderer());
        listAllSelect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listAllSelect.setDragEnabled(true);
        listAllSelect.setModel(listModelColumnSelect);
        listAllColumn.setDropMode(DropMode.INSERT);
        //-------------------------------------------------------------------------------------------------------------------------------------
        buttonToAllColumn.addActionListener(new TransferOneListener(TransferOneListener.TRANSFER_TO_RIGHT));
        buttonToSelect.addActionListener(new TransferOneListener(TransferOneListener.TRANSFER_TO_LEFT));
        buttonToAllColumnAll.addActionListener(new TransferAllListener(TransferAllListener.TRANSFER_TO_RIGHT));
        buttonToSelectAll.addActionListener(new TransferAllListener(TransferAllListener.TRANSFER_TO_LEFT));
        //-------------------------------------------------------------------------------------------------------------------------------------
        buttonMoveUp.addActionListener(new MoveListener(MoveListener.MOVE_UP));
        buttonMoveDown.addActionListener(new MoveListener(MoveListener.MOVE_DOWN));
        //-------------------------------------------------------------------------------------------------------------------------------------
        buttonCommit.addActionListener(new CommitListener());
    }

    /**
     * инициализация редактора столбцов
     */
    public void initPanels() {
        LOG.info("создаем панели отображения");
        initTransferColumnPanel();    //создаем панель перемещения между списками
        initMoveColumnPanel();          //создаем панель перемещения
        initBasicPanel();                       //создаем базовую панель
        add(panelBasic, BorderLayout.CENTER);
    }

    /**
     * инициализируем базовую панель
     */
    protected void initBasicPanel() {
        LOG.info("создаем базовую панель");
        panelBasic.setLayout(new GridBagLayout());
        //1 строка
        panelBasic.add(labelAllColumn, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, inset, 0, 0));
        panelBasic.add(labelAllSelect, new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, inset, 0, 0));
        //2 строка
        panelBasic.add(scrollPaneListAllColumn, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, inset, 0, 0));
        panelBasic.add(panelTransferColumn, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, inset, 0, 0));
        panelBasic.add(scrollPaneListAllSelect, new GridBagConstraints(2, 1, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, inset, 0, 0));
        //3 строка
        panelBasic.add(buttonCommit, new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, inset, 0, 0));
        panelBasic.add(panelMoveColumn, new GridBagConstraints(2, 2, 1, 1, 1, 0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, inset, 0, 0));
        panelBasic.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createEtchedBorder(BevelBorder.LOWERED)));
    }

    /**
     * инициализируем панель перемещения столбцов между списками
     */
    protected void initTransferColumnPanel() {
        LOG.info("создаем панель перекидывания столбцов");
        panelTransferColumn.setLayout(new BoxLayout(panelTransferColumn, BoxLayout.Y_AXIS));
        panelTransferColumn.add(buttonToSelect);
        panelTransferColumn.add(buttonToSelectAll);
        panelTransferColumn.add(buttonToAllColumn);
        panelTransferColumn.add(buttonToAllColumnAll);
    }

    /**
     * инициализируем панель перемещения столбцов по порядку
     */
    protected void initMoveColumnPanel() {
        LOG.info("создаем панель перемещения столбцов");
        panelMoveColumn.add(buttonMoveUp);
        panelMoveColumn.add(buttonMoveDown);
    }

    /**
     * загружаем таблицу для работы
     *
     * @param table     - таблица которую загружаем
     * @param nameTable - название таблицы в файле настроек
     */
    protected void loadTable(JTable table, String nameTable) throws IOException {
        LOG.log(Level.INFO, "загружаем таблицу: {0}", nameTable);
        this.table = table;
        this.nameTable = nameTable;
        loadAllColumnTable();       //формируем карту из всех столбцов таблицы
        loadListTableColumn();      // получаем список всех столбцов
        loadListSelectColumn();     //загружаем список выбранных столбцов
        synchronizationLists();        //чистим списки перед отображение
    }

    /**
     * наполняем список всех столбцов в таблице
     */
    private void loadListTableColumn() {
        LOG.info("загружаем список всех столбцов");
        final int columnCount = table.getColumnCount();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listModelColumnAll.removeAllElements(); //удаляем все элементы на всякий случай, вдруг там есть старые данные
                for (int i = 0; i < columnCount; i++) {
                    listModelColumnAll.addElement(
                            new DataOwnerColumnInfo(
                                    table.getColumnName(i), table.getColumn(table.getColumnName(i)).getModelIndex()
                            )
                    );
                }
                listAllColumn.setTransferHandler(new ListTransferHandler());
            }
        });
    }

    /**
     * метод загружает все столбцы таблицы при первом вызове,
     * чтоб потом можно было удалять и загружать заново
     */
    private void loadAllColumnTable() {
        final int columnCount = table.getColumnCount();
        mapAllTableColumn = new HashMap<String, TableColumn>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            mapAllTableColumn.put(
                    table.getColumnName(i),
                    table.getColumn(table.getColumnName(i)));
        }
    }

    /**
     * загружаем выбранные столбцы из файла в список
     *
     * @throws IOException
     */
    private void loadListSelectColumn() throws IOException {
        LOG.info("загружаем выделенные столбцы из файла настроек");
        ini = new Ini();    //получаем объект для работы с ini
        LOG.log(Level.INFO, "файл с настройками: {0}", pathToIniFile);
        ini.load(new File(pathToIniFile));  //загружаем файл


        final Section section = ini.get(nameTable);   //получаем секцию по названию таблицы
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listModelColumnSelect.removeAllElements();  //в любом случае чистим список
                if (section == null || table.getColumnCount() == 0) {
                    LOG.info("нет указанного названия таблицы  для загрузки или таблица не загружена");
                    return;
                }
                LOG.info("загружаем данные по выделенным столбцам");
                //в противном случае загружаем в список столбцы, которые должны быть выбраны
                /*
                 * в связи с тем, что при наполнении у нас список пуст,
                 * мы его наполняем не нужными объектами,
                 * чтоб потом заменить на наши
                 */
                int size = section.size();
                for (int i = 0; i < size; i++) {
                    listModelColumnSelect.addElement(new DataOwnerColumnInfo(null, 0));
                }
                //-------------------------------------------------------------------------------------------------------------------------------------
                Set<Entry<String, String>> entry = section.entrySet();
                Iterator<Entry<String, String>> iterator = entry.iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> e = iterator.next();
                    try {
                        TableColumn tableColumn = table.getColumn(e.getKey());
                        //добавляем его в список для отображения
                        listModelColumnSelect.set(Integer.parseInt(e.getValue()),
                                new DataOwnerColumnInfo(
                                        tableColumn.getIdentifier().toString(),
                                        tableColumn.getModelIndex())
                        );
                    } catch (ExceptionInInitializerError ex) {
                        //игнорируем это исключение, так как отсутсвие столбца не мешает отображению текущих столбцов
                    }
                }
                listAllSelect.setTransferHandler(new ListTransferHandler());
            }
        });
    }

    /**
     * данный метод приводит списки к нормальным результатам,
     * а именно, чистит левый и правый список,
     * если в правом есть левый столбец (а он будет), тогда удаляем его слева
     */
    private void synchronizationLists() {
        LOG.info("синхронизируем списки");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Enumeration<?> enumeration = listModelColumnSelect.elements();
                while (enumeration.hasMoreElements()) {
                    DataOwnerColumnInfo dataOwnerColumnInfo = (DataOwnerColumnInfo) enumeration.nextElement();
                    if (listModelColumnAll.contains(dataOwnerColumnInfo)) {
                        listModelColumnAll.removeElement(dataOwnerColumnInfo);
                    }
                }
            }
        });
    }

    /**
     * данный метод загружает таблицу с настройками из файла
     */
    protected void fillTableFromSettings() throws IOException {
        LOG.info("загружаем таблицу настройками из файла");
        showTable();
    }

    /**
     * отображаем таблицу из выбранных столбцоы
     */
    private void showTable() {
        LOG.info("отображаем таблицу с применение настроек");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                /*
                 * если список для отображения пустой(это может произойти)
                 * при первом запуске, тогда выходим из наполнения и оставляем столбцы
                 */
                if (listModelColumnSelect.isEmpty()) {
                    return;
                }
                /*
                 * в противном случаем формируем список для столбцов,
                 * которые скрываем, чтоб потом их снова открыть
                 */
                Map<String, TableColumn> tableColumns = new HashMap<String, TableColumn>(table.getColumnModel().getColumnCount());
                //перебираем столбцы для скрытия
                while (table.getColumnModel().getColumns().hasMoreElements()) {
                    TableColumn tableColumn = table.getColumnModel().getColumns().nextElement();
                    table.removeColumn(tableColumn);
                    tableColumns.put(tableColumn.getIdentifier().toString(), tableColumn);
                }
                //перебираем столбцы для  отображения
                Enumeration<?> enumeration = listModelColumnSelect.elements();
                while (enumeration.hasMoreElements()) {
                    DataOwnerColumnInfo dataOwnerColumnInfo = (DataOwnerColumnInfo) enumeration.nextElement();
                    TableColumn tableColumn = mapAllTableColumn.get(dataOwnerColumnInfo.getNameColumn());
                    if (tableColumn != null) {
                        table.addColumn(tableColumn);
                    }
                }
            }
        });
    }

    //--------------------------------------------------------------------------
    //геттеры и сеттеры
    //--------------------------------------------------------------------------
    public String getPathToIniFile() {
        return pathToIniFile;
    }

    public void setPathToIniFile(String pathToIniFile) {
        this.pathToIniFile = pathToIniFile;
    }

    public JList getListAllColumn() {
        return listAllColumn;
    }

    public JList getListAllSelect() {
        return listAllSelect;
    }

    public JButton getButtonToAllColumn() {
        return buttonToAllColumn;
    }

    public JButton getButtonToSelect() {
        return buttonToSelect;
    }

    public JButton getButtonToAllColumnAll() {
        return buttonToAllColumnAll;
    }

    public JButton getButtonToSelectAll() {
        return buttonToSelectAll;
    }

    public JButton getButtonMoveUp() {
        return buttonMoveUp;
    }

    public JButton getButtonMoveDown() {
        return buttonMoveDown;
    }

    public JButton getButtonCommit() {
        return buttonCommit;
    }

    public JLabel getLabelAllColumn() {
        return labelAllColumn;
    }

    public JLabel getLabelAllSelect() {
        return labelAllSelect;
    }
    //---------------------------------------------------------------------------------------------------------------------------------------
    //внутренние классы
    //---------------------------------------------------------------------------------------------------------------------------------------

    /**
     * внутренний класс, который отвечает за отображение списка
     */
    private class OwnListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof DataOwnerColumnInfo) {
                label.setText(((DataOwnerColumnInfo) value).getNameColumn());
            }
            return label;
        }
    }

    /**
     * класс отвечает за перемещение одного элемента
     */
    private class TransferOneListener implements ActionListener {
        /**
         * константа переноса вправо
         */
        public static final int TRANSFER_TO_RIGHT = 0;
        /**
         * константа переноса влево
         */
        public static final int TRANSFER_TO_LEFT = 1;
        private final int transfer;

        public TransferOneListener(int transfer) {
            this.transfer = transfer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (transfer) {
                case TRANSFER_TO_RIGHT:
                    transfer(listAllSelect, listAllColumn);
                    break;
                case TRANSFER_TO_LEFT:
                    transfer(listAllColumn, listAllSelect);
                    break;
            }
        }

        /**
         * перемещаем элементы
         * <p>
         *
         * @param from - из какого списка
         * @param to   - в какой список
         */
        private void transfer(JList from, JList to) {
            if (from.isSelectionEmpty())    //если ничего не выбрано, тогда нечего перемещать
            {
                return;
            }
            final DataOwnerColumnInfo tableColumn = (DataOwnerColumnInfo) from.getSelectedValue();    //получаем выделенный объект
            final DefaultListModel defaultListModelFrom = (DefaultListModel) from.getModel();
            final DefaultListModel defaultListModelTo = (DefaultListModel) to.getModel();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    defaultListModelFrom.removeElement(tableColumn);
                    defaultListModelTo.addElement(tableColumn);
                }
            });
        }
    }

    /**
     * класс отвечает за перемещение всех элементов
     */
    private class TransferAllListener implements ActionListener {
        /**
         * константа переноса вправо
         */
        public static final int TRANSFER_TO_RIGHT = 0;
        /**
         * константа переноса влево
         */
        public static final int TRANSFER_TO_LEFT = 1;
        private final int transfer;

        public TransferAllListener(int transfer) {
            this.transfer = transfer;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (transfer) {
                case TRANSFER_TO_RIGHT:
                    transfer(listModelColumnSelect, listModelColumnAll);
                    break;
                case TRANSFER_TO_LEFT:
                    transfer(listModelColumnAll, listModelColumnSelect);
                    break;
            }
        }

        /**
         * перемещаем элементы
         * <p>
         *
         * @param from - из какого списка
         * @param to   - в какой список
         */
        private void transfer(final DefaultListModel from, final DefaultListModel to) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int size = from.size();
                    for (int i = 0; i < size; i++) {
                        to.addElement(from.get(i));
                    }
                    from.removeAllElements();
                }
            });
        }
    }

    /**
     * класс отвечает за вертикальное перемещение
     */
    private class MoveListener implements ActionListener {
        /**
         * константа переноса вправо
         */
        public static final int MOVE_UP = -1;
        /**
         * константа переноса влево
         */
        public static final int MOVE_DOWN = 1;
        private final int move;

        public MoveListener(int move) {
            this.move = move;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            move();
        }

        private void move() {
            if (listAllSelect.isSelectionEmpty())   //если нет выбранных элементов, тогда ничего не делаем
            {
                return;
            }
            final int indexSelect = listAllSelect.getSelectedIndex();
            final DataOwnerColumnInfo tableColumn = (DataOwnerColumnInfo) listAllSelect.getSelectedValue();
            int size = listModelColumnSelect.size();
            //если при перемещении позиция объекта будет меньше 0, или больше размера списка - 1тогда не перемещаем
            if ((indexSelect + move) < 0 || (indexSelect + move) > size - 1) {
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    //в противном случаем перемещаем
                    DataOwnerColumnInfo columnOld = (DataOwnerColumnInfo) listModelColumnSelect.get(indexSelect + move);
                    listModelColumnSelect.set(indexSelect + move, tableColumn);
                    listModelColumnSelect.set(indexSelect, columnOld);
                    listAllSelect.setSelectedIndex(indexSelect + move);
                }
            });
        }
    }

    /**
     * класс обрабатывает применение фильтра по столбцам
     */
    private class CommitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                commit();
            } catch (IOException ex) {
                Logger.getLogger(ColumnEditorDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * применяем выбранный фильтр
         */
        private void commit() throws IOException {
            if (listModelColumnSelect.isEmpty())    //если список выбранных столбцов ]
            {
                return;
            }
            writeIni();            //пишем файл с настройками
            showTable();           //отображаем выбранные столбцы таблицы
        }

        /**
         * пишем файл настроек
         */
        private void writeIni() throws IOException {
            Section section = ini.get(nameTable);
            if (section == null)    //если такой секции нет, тогда создаем
            {
                section = ini.add(nameTable);
            }
            section.clear();
            //перебираем столбцы для  отображения
            Enumeration<?> enumeration = listModelColumnSelect.elements();
            while (enumeration.hasMoreElements()) {
                DataOwnerColumnInfo dataOwnerColumnInfo = (DataOwnerColumnInfo) enumeration.nextElement();
                section.add(dataOwnerColumnInfo.getNameColumn(), listModelColumnSelect.indexOf(dataOwnerColumnInfo));  //пишем название столбца и порядковый номер
            }
            ini.store(new File(pathToIniFile));
        }
    }
}
