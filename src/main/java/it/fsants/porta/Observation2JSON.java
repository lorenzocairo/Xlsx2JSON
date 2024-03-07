package it.fsants.porta;

import it.fsants.dal.DBConnection;
import it.fsants.JSONExtractor.JSONExtractor;
import it.fsants.JSONExtractor.Table2JsonArr;
import it.fsants.JSONExtractor.Xlsx2JSONException;
import it.fsants.bird_names.Bird;
import it.fsants.bird_names.BirdNameValidator;
import it.fsants.dal.DBInsert;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;

public class Observation2JSON {

    private static String BASE_DIR = "C:\\Users\\loren\\Downloads\\2023";
    private static String SPECIE = "specie";
    private static String NOME_SCIENTIFICO = "nomeScientifico";
    private static String NUMERO = "numero";
    private static BirdNameValidator birdNameValidator;

    public static void main(String[] args) {
        JSONArray a = null;
        birdNameValidator = new BirdNameValidator();
        try {
            a = extractObservations();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        for (int i = 0; i < a.length(); i++) {
            try {
                DBInsert.addMonthObservations((JSONObject) a.get(i));
            } catch (Exception e) {
                System.out.printf("Cannot insert: %s%n", e.getMessage());
            }
        }
    }

    public static JSONArray extractObservations() throws Xlsx2JSONException {
        JSONArray array = new JSONArray();
        File[] files = new File(BASE_DIR).listFiles();
        for (int i = 0; i < files.length; i++) {
            final File f = files[i];
            if (f.getName().contains(".xlsx")) {
                JSONObject o = extractFile(f);
                if (o != null) {
                    array.put(o);
                }
            }
        }
        return array;
    }

    /**
     * Schema:
     *  obs: {
     *     date: date
     *     observations: {
     *         specie: specie,
     *         nomeScientifico: scName,
     *         numero: num
     *     }
     * }
     * @param file
     * @return
     * @throws Xlsx2JSONException
     */
    private static JSONObject extractFile(File file) throws Xlsx2JSONException {
        JSONObject o = new JSONObject();
        try (InputStream is = new FileInputStream(file); Workbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            Table2JsonArr table2JsonArr = JSONExtractor.generate(1, 0, 2, sheet);
            JSONArray obs = table2JsonArr.extract();
            if (!validation(obs)) return null;
            o.put("observations", obs);

            String date = sheet.getRow(0).getCell(1).getStringCellValue();
            o.put("date", convertDate(date));

            return o;
        } catch (Exception e) {
            throw new Xlsx2JSONException(String.format("Error extracting file %s: %s", file.getName(), e.getMessage()));
        }
    }

    private static String convertDate(String date) {
        if (date == null) return null;
        final String[] arr = date.split("\\.");
        return String.format("%s-%s-%s", arr[2], arr[1], arr[0]);
    }

    private static boolean validation(JSONArray a) {
        JSONObject o;
        for (Iterator it = a.iterator(); it.hasNext();) {
            o = (JSONObject) it.next();
            if (!schemaValidationObs(o)) {
                return false;
            }
            String name = (String) o.get(SPECIE);
            Bird bird = birdNameValidator.getBirdByName(name);
            if (bird == null) {
                it.remove();
            } else {
                o.put(SPECIE, bird.getName());
                o.put(NOME_SCIENTIFICO, bird.getScientificName());
            }
        }
        return true;
    }

    private static boolean schemaValidationObs(JSONObject o) {
        if (o == null) return false;
        return o.has(SPECIE) && o.has(NOME_SCIENTIFICO) && o.has(NUMERO);
    }
}
