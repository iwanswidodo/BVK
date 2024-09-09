package BVK.GlobalMethod.Utils;

import BVK.GlobalMethod.Logger.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import static BVK.GlobalMethod.Utils.DateAndTime.getFutureDate1;
import static BVK.GlobalMethod.Utils.DateAndTime.getTime;


public class ExcelUtils {
    private static XSSFSheet xlsxWorkSheet;
    private static XSSFWorkbook xlsxWorkBook;
    private static XSSFCell xlsxCell;
    private static XSSFRow xlsxRow;

    private static HSSFSheet xlsWorkSheet;
    private static HSSFWorkbook xlsWorkBook;
    private static HSSFCell xlsCell;
    private static HSSFRow xlsRow;

    private static FileInputStream fis;
    private static FileOutputStream fos;

    private static Cell cell2Update;

    /**
     * To get the Excel-XLSX File with Path and SheetName
     */
    public static void getExcelFile(String Path, String SheetName) throws Exception {
        try {
            File file = new File(Path);
            if (file.getAbsolutePath().endsWith(".xlsx")) {
                fis = new FileInputStream(file);
                xlsxWorkBook = new XSSFWorkbook(fis);
                xlsxWorkSheet = xlsxWorkBook.getSheet(SheetName);
                if(xlsxWorkSheet == null){ xlsxWorkSheet = xlsxWorkBook.createSheet(SheetName);}
            } else if (file.getAbsolutePath().endsWith(".xls")) {
                fis = new FileInputStream(file);
                xlsWorkBook = new HSSFWorkbook(fis);
                xlsWorkSheet = xlsWorkBook.getSheet(SheetName);
                if(xlsWorkSheet == null){ xlsWorkSheet = xlsWorkBook.createSheet(SheetName);}
            }
        } catch (Exception e) {
            throw (e);
        }
    }

    public static void writeExcelFile(String Path){
        try{
            File file = new File(Path);
            if(file.getAbsolutePath().endsWith(".xlsx")){
                fis.close();
                fos = new FileOutputStream(Path);
                xlsxWorkBook.write(fos);
                fos.close();
                Log.info("Output/input stream closed.");
            } else if (file.getAbsolutePath().endsWith(".xls")) {
                fis.close();
                fos = new FileOutputStream(Path);
                xlsWorkBook.write(fos);
                fos.close();
                Log.info("Output/input stream closed.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * To Return the Excel-XLSX Values given Path to the File and Sheet Name
     */
    public static Object[][] getTableArray(String FilePath, String SheetName) throws Exception {
        Object[][] tabArray = null;
        try {
            File file = new File(FilePath);
            if (file.getAbsolutePath().endsWith(".xlsx")) {
                FileInputStream ExcelFile = new FileInputStream(file);
                xlsxWorkBook = new XSSFWorkbook(ExcelFile);
                xlsxWorkSheet = xlsxWorkBook.getSheet(SheetName);

                int startRow = 1;
                int startCol = 0;
                int ci, cj;
                int totalRows = ExcelUtils.xlsxRowCount();
                int totalCols = ExcelUtils.xlsxColumnCount();
                tabArray = new Object[totalRows - 1][totalCols];
                ci = 0;
                for (int i = startRow; i < totalRows; i++) {
                    cj = 0;
                    for (int j = startCol; j < totalCols; j++) {
                        tabArray[ci][cj] = getCellData_XLSX(i, j);
                        cj++;
                    }
                    ci++;
                }
            } else if (file.getAbsolutePath().endsWith(".xls")) {
                FileInputStream ExcelFile = new FileInputStream(file);
                xlsWorkBook = new HSSFWorkbook(ExcelFile);
                xlsWorkSheet = xlsWorkBook.getSheet(SheetName);

                int startRow = 1;
                int startCol = 0;
                int ci, cj;
                int totalRows = ExcelUtils.xlsRowCount();
                int totalCols = ExcelUtils.xlsColumnCount();
                tabArray = new Object[totalRows - 1][totalCols];
                ci = 0;
                for (int i = startRow; i < totalRows; i++) {
                    cj = 0;
                    for (int j = startCol; j < totalCols; j++) {
                        tabArray[ci][cj] = getCellData_XLS(i, j);
                        cj++;
                    }
                    ci++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new Exception("Could not Find the Excel File/Sheet");
        } catch (Exception e) {
            throw new Exception("Could not Open the Excel File");
        }
        return (tabArray);
    }


    /**
     * To Return the Excel-XLSX Values given Path to the File
     */
    public static Object[][] getTableArray(String FilePath) throws Exception {
        Object[][] tabArray = null;
        try {
            File file = new File(FilePath);
            if (file.getAbsolutePath().endsWith(".xlsx")) {
                FileInputStream ExcelFile = new FileInputStream(file);
                xlsxWorkBook = new XSSFWorkbook(ExcelFile);
                xlsxWorkSheet = xlsxWorkBook.getSheetAt(0);

                int startRow = 1;
                int startCol = 0;
                int ci, cj;
                int totalRows = ExcelUtils.xlsxRowCount();
                int totalCols = ExcelUtils.xlsxColumnCount();
                tabArray = new Object[totalRows - 1][totalCols];
                ci = 0;
                for (int i = startRow; i < totalRows; i++) {
                    cj = 0;
                    for (int j = startCol; j < totalCols; j++) {
                        tabArray[ci][cj] = getCellData_XLSX(i, j);
                        cj++;
                    }
                    ci++;
                }
            } else if (file.getAbsolutePath().endsWith(".xls")) {
                FileInputStream ExcelFile = new FileInputStream(file);
                xlsWorkBook = new HSSFWorkbook(ExcelFile);
                xlsWorkSheet = xlsWorkBook.getSheetAt(0);

                int startRow = 1;
                int startCol = 0;
                int ci, cj;
                int totalRows = ExcelUtils.xlsRowCount();
                int totalCols = ExcelUtils.xlsColumnCount();
                tabArray = new Object[totalRows - 1][totalCols];
                ci = 0;
                for (int i = startRow; i < totalRows; i++) {
                    cj = 0;
                    for (int j = startCol; j < totalCols; j++) {
                        tabArray[ci][cj] = getCellData_XLS(i, j);
                        cj++;
                    }
                    ci++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new Exception("Could not Find the Excel File/Sheet");
        } catch (Exception e) {
            throw new Exception("Could not Open the Excel File");
        }
        return (tabArray);
    }


    /**
     * To get cell data from Excel-XLSX
     */
    public static Object getCellData_XLSX(int RowNum, int ColNum) throws Exception {
        Object CellData = null;
        try {
            xlsxCell = xlsxWorkSheet.getRow(RowNum).getCell(ColNum);
            if (xlsxCell.getCellType() == CellType.STRING) {
                String stringCellData = xlsxCell.getStringCellValue();
                CellData = stringCellData;
            } else if (xlsxCell.getCellType() == CellType.NUMERIC) {
                double numericCellData = xlsxCell.getNumericCellValue();
                CellData = numericCellData;
            } else if (xlsxCell.getCellType() == CellType.BOOLEAN) {
                boolean booleanCellData = xlsxCell.getBooleanCellValue();
                CellData = booleanCellData;
            } else if (xlsxCell.getCellType() == CellType.FORMULA) {
                String formulaCellData = xlsxCell.getCellFormula();
                CellData = formulaCellData;
            }
            return CellData;
        } catch (Exception e) {
            return "";
        }
    }

    public static void updateCell(int row, int cell, double cellValue) {
        xlsxRow = xlsxWorkSheet.getRow(row);
        if(xlsxRow==null) {xlsxRow = xlsxWorkSheet.createRow(row);}
        xlsxCell = xlsxRow.getCell(cell);
        if(xlsxCell==null) {xlsxCell = xlsxRow.createCell(cell);}
        xlsxCell.setCellValue(cellValue);
    }

    public static void updateCell(int row, int cell, String cellValue) {
        xlsxRow = xlsxWorkSheet.getRow(row);
        if(xlsxRow==null) {xlsxRow = xlsxWorkSheet.createRow(row);}
        xlsxCell = xlsxRow.getCell(cell);
        if(xlsxCell==null) {xlsxCell = xlsxRow.createCell(cell);}
        xlsxCell.setCellValue(cellValue);
    }


    public static void updateCell(int row, int cell, Object cellValue) {
        xlsxRow = xlsxWorkSheet.getRow(row);
        if(xlsxRow==null) {xlsxRow = xlsxWorkSheet.createRow(row);}
        xlsxCell = xlsxRow.getCell(cell);
        if(xlsxCell==null) {xlsxCell = xlsxRow.createCell(cell);}
        if (cellValue instanceof String) {
            xlsxCell.setCellValue((String) cellValue);
        } else if (cellValue instanceof Integer) {
            xlsxCell.setCellValue((Integer) cellValue);
        }
    }

    /**
     * To get cell data from Excel-XLS
     */
    public static Object getCellData_XLS(int RowNum, int ColNum) throws Exception {
        Object CellData = null;
        try {
            xlsCell = xlsWorkSheet.getRow(RowNum).getCell(ColNum);
            if (xlsCell.getCellType() == CellType.STRING) {
                String stringCellData = xlsCell.getStringCellValue();
                CellData = stringCellData;
            } else if (xlsCell.getCellType() == CellType.NUMERIC) {
                double numericCellData = xlsCell.getNumericCellValue();
                CellData = numericCellData;
            } else if (xlsCell.getCellType() == CellType.BOOLEAN) {
                boolean booleanCellData = xlsCell.getBooleanCellValue();
                CellData = booleanCellData;
            } else if (xlsxCell.getCellType() == CellType.FORMULA) {
                String formulaCellData = xlsxCell.getCellFormula();
                CellData = formulaCellData;
            }
            return CellData;
        } catch (Exception e) {
            return "";
        }
    }

    public static void exportDataToXlsx(String Filepath, String Sheetname,int testNumber,String testCase,Object[] testData, String responseBody) throws Exception {
        getExcelFile(Filepath,Sheetname);
        int row = testNumber+2;
        int col = 0;
        boolean header = true;
        updateCell(2,col,"no");//test number header
        updateCell(row,col++,testNumber);//test number
        updateCell(2,col,"case");//case header
        updateCell(row,col++,testCase);//test case description
        for(Object field : testData){
            if(header){
                updateCell(2,col,field);
            } else {
                updateCell(row,col++,field);
            }
            header = !header;
        }//test data
        updateCell(2,col,"");//remarks header
        updateCell(row,col++,"");//remarks
        updateCell(2,col,"Valid/Invalid");//valid header
        updateCell(row,col++,"valid");//valid
        updateCell(2,col,"Response Body");//body header
        updateCell(row,col++,responseBody);//body
        updateCell(2,col,"Automation status");//automation status header
        updateCell(row,col++,"pass");//automation status
        updateCell(2,col,"Last run");//date header
        updateCell(row,col++,getFutureDate1(0)+" "+ getTime());//date
        writeExcelFile(Filepath);
    }

    /**
     * To get Excel-XLSX Row Count
     */
    public static int xlsxRowCount() {
        int rowNum = xlsxWorkSheet.getLastRowNum() + 1;
        return rowNum;
    }

    /**
     * To get Excel-XLS Row Count
     */
    public static int xlsRowCount() {
        int rowNum = xlsWorkSheet.getLastRowNum() + 1;
        return rowNum;
    }

    /**
     * To get Excel-XLSX Column Count
     */
    public static int xlsxColumnCount() {
        int rowNum = xlsxWorkSheet.getRow(0).getLastCellNum();
        return rowNum;
    }

    /**
     * To get Excel-XLS Column Count
     */
    public static int xlsColumnCount() {
        int rowNum = xlsWorkSheet.getRow(0).getLastCellNum();
        return rowNum;
    }
}