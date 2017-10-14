//package ru.ezhov.testtable;
//
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.DomDriver;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import ru.ezhov.columneditor.columnsobjects.DataOwnerColumnInfo;
//import ru.ezhov.columneditor.columnsobjects.Table;
//import ru.ezhov.columneditor.columnsobjects.Tables;
//
///**
// *
// * @author rrndeonisiusezh
// */
//public class TestXStream
//{
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args)
//    {
//        Tables tables = new Tables(2);
//        List<DataOwnerColumnInfo> listOwner = new ArrayList<DataOwnerColumnInfo>();
//        for (int i = 0; i < 10; i++)
//        {
//            listOwner.add(new DataOwnerColumnInfo(String.valueOf(i), i));
//        }
//        Table table = new Table("пробуем", listOwner);
//        List<Table> ts = new ArrayList<Table>();
//        ts.add(table);
//        tables.addTable(table);
//        listOwner = new ArrayList<DataOwnerColumnInfo>();
//        for (int i = 0; i < 10; i++)
//        {
//            listOwner.add(new DataOwnerColumnInfo(String.valueOf(i), i));
//        }
//        table = new Table("опять таблица", listOwner);
//        ts = new ArrayList<Table>();
//        ts.add(table);
//        tables.addTable(table);
//        listOwner = new ArrayList<DataOwnerColumnInfo>();
//        for (int i = 0; i < 10; i++)
//        {
//            listOwner.add(new DataOwnerColumnInfo(String.valueOf(i), i));
//        }
//        table = new Table("и вот опять таблица", listOwner);
//        ts = new ArrayList<Table>();
//        ts.add(table);
//        tables.addTable(table);
//
//        FileOutputStream outputStream = null;
//        try
//        {
//            outputStream = new FileOutputStream(new File("test_column.xml"));
//        } catch (FileNotFoundException ex)
//        {
//            Logger.getLogger(TestXStream.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        XStream xstream = new XStream(new DomDriver()); // require XPP3 library
//        xstream.alias("tables", Tables.class);
//        xstream.addImplicitCollection(Table.class, "tables");
//        xstream.alias("table", Table.class);
//        xstream.alias("column", DataOwnerColumnInfo.class);
//        //String xml = xstream.toXML(tables, outputStream);
//        xstream.toXML(tables, outputStream);
//        if (outputStream != null)
//        {
//            try
//            {
//                outputStream.close();
//            } catch (IOException ex)
//            {
//                Logger.getLogger(TestXStream.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//}
