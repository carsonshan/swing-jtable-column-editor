package ru.ezhov.columneditor.testUI;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import ru.ezhov.columneditor.InitColumnEditor;

/**
 *
 * @author rrndeonisiusezh
 */
public class TestTable
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame frame = new JFrame("test table");
                final JTable table = new JTable(new Object[][]
                {
                    {
                        "1", "2", "3", "4", "5", "6"
                    },
                    {
                        "1", "2", "3", "4", "5", "6"
                    },
                    {
                        "1", "2", "3", "4", "5", "6"
                    },
                    {
                        "1", "2", "3", "4", "5", "6"
                    },
                    {
                        "1", "2", "3", "4", "5", "6"
                    },
                    {
                        "1", "2", "3", "4", "5", "6"
                    }
                }, new Object[]
                {
                    "1", "2", "3", "4", "5", "6"
                });
                frame.add(new JScrollPane(table), BorderLayout.CENTER);
                final InitColumnEditor initColumnEditor = new InitColumnEditor();
                table.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        if (e.getButton() == MouseEvent.BUTTON3)
                        {
                            initColumnEditor.showEditDialog();
                        }
                    }
                });
                try
                {
                    initColumnEditor.initColumnEditor(table, "portal_soz_table");
                    initColumnEditor.initDialog();
                } catch (IOException ex)
                {
                    Logger.getLogger(TestTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
