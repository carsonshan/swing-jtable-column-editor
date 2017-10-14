package ru.ezhov.columneditor;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JTable;
/**
 * * класс, который инициализирует редактор
 *
 * @author rrndeonisiusezh
 */
public class InitColumnEditor
{
    /** это папка приложения в документах пользователя */
    private static final String DIRECTORY_APP = "test";
    /** это файл с настройками для всех таблиц приложения */
    private static final String INI_FILE = "display_tables.ini";
    private final static String DIRECTORY_USER_APP = System.getProperty("user.home") + File.separator + DIRECTORY_APP + File.separator;
    private ColumnEditorDialog columnEditorDialog;
    /** это размер кнопок переноса */
    private final Dimension dimensionButtonTransfer = new Dimension(30, 30);
    private final Dimension dimensionButtonMove = new Dimension(35, 35);
    public void initColumnEditor(JTable table, String tableName) throws IOException
    {
        columnEditorDialog = ColumnEditorDialog.getInstance(null);
        checkAppDirectoryCreate();
        try
        {
            columnEditorDialog.initPanels();    ///инициализируем панели
            //передаем путь к файлу настроек
            columnEditorDialog.setPathToIniFile(DIRECTORY_USER_APP + File.separator + INI_FILE);
            columnEditorDialog.loadTable(table, tableName);
            columnEditorDialog.fillTableFromSettings();
        } catch (IOException ex)
        {
            Logger.getLogger(InitColumnEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void initDialog()
    {
        columnEditorDialog.setTitle("Настройка колонок таблицы");
        columnEditorDialog.getLabelAllColumn().setText("Все столбцы:");
        columnEditorDialog.getLabelAllSelect().setText("Выбранные столбцы:");
        //-------------------------------------------------------------------------------------------------------------------------------------
        columnEditorDialog.setIconImage(new ImageIcon(InitColumnEditor.class.getResource("/settings.png")).getImage());
        //опускаем вниз
        columnEditorDialog.getButtonMoveDown().setIcon(new ImageIcon(InitColumnEditor.class.getResource("/arrow_down_32.png")));
        columnEditorDialog.getButtonMoveDown().setPreferredSize(dimensionButtonMove);
        columnEditorDialog.getButtonMoveDown().setMinimumSize(dimensionButtonMove);
        columnEditorDialog.getButtonMoveDown().setMaximumSize(dimensionButtonMove);
        columnEditorDialog.getButtonMoveDown().setToolTipText("переместить столбец вниз");
        //поднимаем вверх
        columnEditorDialog.getButtonMoveUp().setIcon(new ImageIcon(InitColumnEditor.class.getResource("/arrow_up_32.png")));
        columnEditorDialog.getButtonMoveUp().setPreferredSize(dimensionButtonMove);
        columnEditorDialog.getButtonMoveUp().setMinimumSize(dimensionButtonMove);
        columnEditorDialog.getButtonMoveUp().setMaximumSize(dimensionButtonMove);
        columnEditorDialog.getButtonMoveDown().setToolTipText("переместить столбец вверх");
        //-------------------------------------------------------------------------------------------------------------------------------------
        columnEditorDialog.getButtonToSelect().setIcon(new ImageIcon(InitColumnEditor.class.getResource("/arrow_right_24.png")));
        columnEditorDialog.getButtonToSelect().setPreferredSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToSelect().setMinimumSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToSelect().setMaximumSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToSelect().setToolTipText("добавить столбец в выбранные");
        columnEditorDialog.getButtonToSelectAll().setIcon(new ImageIcon(InitColumnEditor.class.getResource("/controls_right_24.png")));
        columnEditorDialog.getButtonToSelectAll().setPreferredSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToSelectAll().setMinimumSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToSelectAll().setMaximumSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToSelect().setToolTipText("добавить все столбцы в выбранные");
        columnEditorDialog.getButtonToAllColumn().setIcon(new ImageIcon(InitColumnEditor.class.getResource("/arrow_left_24.png")));
        columnEditorDialog.getButtonToAllColumn().setPreferredSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToAllColumn().setMinimumSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToAllColumn().setMaximumSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToSelect().setToolTipText("убрать столбец из выбранных");
        columnEditorDialog.getButtonToAllColumnAll().setIcon(new ImageIcon(InitColumnEditor.class.getResource("/controls_left_24.png")));
        columnEditorDialog.getButtonToAllColumnAll().setPreferredSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToAllColumnAll().setMinimumSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToAllColumnAll().setMaximumSize(dimensionButtonTransfer);
        columnEditorDialog.getButtonToSelect().setToolTipText("убрать все столбцы из выбранных");
        //-------------------------------------------------------------------------------------------------------------------------------------
        columnEditorDialog.getButtonCommit().setText("Применить");
        //-------------------------------------------------------------------------------------------------------------------------------------
        columnEditorDialog.setSize(350, 300);
        columnEditorDialog.setLocationRelativeTo(null);
    }
    public void showEditDialog()
    {
        columnEditorDialog.setVisible(true);
    }
    /**
     * метод проверяет наличие папки приложения в папках пользователя
     * и в случае отсутсвия создает папку и необходимый файл с настройками
     */
    private void checkAppDirectoryCreate() throws IOException
    {
        File fileDir = new File(DIRECTORY_USER_APP);
        if (!fileDir.exists()) //если папка не создана, создаем
        {
            fileDir.mkdir();
        }
        File fileIni = new File(DIRECTORY_USER_APP + File.separator + INI_FILE);
        if (!fileIni.exists()) //если папка не создана, создаем
        {
            fileIni.createNewFile();
        }
    }
}
