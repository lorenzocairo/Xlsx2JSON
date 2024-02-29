package it.fsants.porta;

import it.fsants.JSONExtractor.JSONExtractor;
import it.fsants.JSONExtractor.Table2JsonArr;
import it.fsants.JSONExtractor.Xlsx2JSONException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Observation2JSON {

    private static String BASE_DIR = "C:\\Users\\loren\\Downloads\\2023";

    public static void main(String[] args) {
        try {
            JSONArray a = extractObservations();
            System.out.println(a.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static JSONArray extractObservations() throws Xlsx2JSONException {
        JSONArray array = new JSONArray();
        File[] files = new File(BASE_DIR).listFiles();
        for (int i = 0; i < files.length; i++) {
            final File f = files[i];
            if (f.getName().contains(".xlsx")) {
                JSONObject o = extractFile(f);
                array.put(o);
            }
        }
        return array;
    }

    private static JSONObject extractFile(File file) throws Xlsx2JSONException {
        JSONObject o = new JSONObject();
        try (InputStream is = new FileInputStream(file); Workbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            Table2JsonArr table2JsonArr = JSONExtractor.generate(1, 0, 2, sheet);
            JSONArray obs = table2JsonArr.extract();
            o.put("observations", obs);

            String date = sheet.getRow(0).getCell(1).getStringCellValue();
            o.put("date", date);

            return o;
        } catch (Exception e) {
            throw new Xlsx2JSONException(String.format("Error extracting file %s: %s", file.getName(), e.getMessage()));
        }
    }
}
