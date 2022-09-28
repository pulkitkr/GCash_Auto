package com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

	
	public static Object[][] getTestData(String filePath, String sheetName, String testcaseName) throws IOException
	  {
	    //fileInputStream argument
	    FileInputStream fis = new FileInputStream(new File(filePath));
	    XSSFWorkbook wb = new XSSFWorkbook(fis);
	    XSSFSheet sheet = wb.getSheet(sheetName);

	    ArrayList<ArrayList<String>> tcResultArray = new ArrayList<ArrayList<String>>();
	    Iterator<Row> it = sheet.rowIterator();
	    //Skip first row
	    if(it.hasNext()){
	      it.next();
	    }
	    // if more than one row exist than iterate..
	    while(it.hasNext()){
	      Row row = it.next();
	      Iterator<Cell> tsCell = row.cellIterator();
	      if(testcaseName.equalsIgnoreCase(tsCell.next().getStringCellValue())){
	        ArrayList<String> tc = new ArrayList<String>();
	        while(tsCell.hasNext()){
	          Cell c = tsCell.next();
	           if(c.getCellType()==CellType.STRING) {
	        	   tc.add(c.getStringCellValue());
	           }else if (c.getCellType()==CellType.BLANK){
	        	   tc.add("");
	           }else{
					
					tc.add(NumberToTextConverter.toText(c.getNumericCellValue()));
				
			   }          
	        }
	        tcResultArray.add(tc);
	      }
	    }

	    //convert from array list to array of object.
	    Object[][] arrayObj = new String[tcResultArray.size()][];
	    for (int i = 0; i < tcResultArray.size(); i++) {
	      ArrayList<String> row = tcResultArray.get(i);
	      arrayObj[i] = row.toArray(new String[row.size()]);
	    }

	    fis.close();
	    return arrayObj;
	  }
}
					
					
					
					
					