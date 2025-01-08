package com.untitled.server.untitled.global.handler;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.binary.XSSFBSheetHandler;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.xmlbeans.XmlOptionsBean;
import org.apache.xmlbeans.impl.common.SAXHelper;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelSheetHandler implements XSSFBSheetHandler.SheetContentsHandler {

    private int currentCol = -1;
    private int currRowNum = 0;

    private List<List<String>> rows = new ArrayList<List<String>>();
    private List<String> row = new ArrayList<String>();
    private List<String> header = new ArrayList<String>();

    public static ExcelSheetHandler readExcel(File file) throws Exception {

        ExcelSheetHandler sheethandler = new ExcelSheetHandler();
        try {
            OPCPackage opc = OPCPackage.open(file);
            XSSFReader xssfReader = new XSSFReader(opc);
            StylesTable styles = xssfReader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);

            InputStream inputStream = xssfReader.getSheetsData().next();
            InputSource inputSource = new InputSource(inputStream);

            ContentHandler handle = new XSSFSheetXMLHandler(styles, strings, sheethandler, false);

            XmlOptionsBean opts = new XmlOptionsBean();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);

            XMLReader xmlReader = SAXHelper.newXMLReader(opts);
            xmlReader.setContentHandler(handle);

            xmlReader.parse(inputSource);
            inputStream.close();
            opc.close();

        } catch (Exception e) {

        }

        return sheethandler;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    @Override
    public void startRow(int i) {
        this.currentCol = -1;
        this.currRowNum = i;
    }

    @Override
    public void endRow(int i) {
//        if (i == 0) {
//            header = new ArrayList(row);
//        } else {
//            if (row.size() < header.size()) {
//                for (int j = row.size(); j < header.size(); j++) {
//                    row.add("");
//                }
//            }
            rows.add(new ArrayList(row));
//        }
        row.clear();
    }

    @Override
    public void cell(String s, String s1, XSSFComment xssfComment) {
        int iCol = (new CellReference(s)).getCol();
        int emptyCol = iCol - currentCol - 1;

        for (int i = 0; i < emptyCol; i++) {
            row.add("");
        }
        currentCol = iCol;
        row.add(s1);
    }

    @Override
    public void hyperlinkCell(String s, String s1, String s2, String s3, XSSFComment xssfComment) {

    }
}
