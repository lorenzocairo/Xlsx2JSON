package it.fsants.bird_names;

import it.fsants.dal.DBConfig;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BirdNameValidator {

    private Map<String, Bird> birdMap = new HashMap<>(); //key nome

    public BirdNameValidator() {
        try (InputStream input = DBConfig.class.getClassLoader().getResourceAsStream("bird_list.xlsx")) {
            this.createMap(input);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot find birds list file: %s", e.getMessage()));
        }
    }

    private void createMap(InputStream is) {
        try (Workbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            for (Iterator<Row> it = sheet.rowIterator(); it.hasNext(); ) {
                Row r = it.next();
                Cell cName = r.getCell(1);
                if (cName == null) continue;
                String name = cName.getStringCellValue();
                Cell cScName = r.getCell(0);
                if (cScName == null) continue;
                String scName = cScName.getStringCellValue();
                if (name != null && !name.isEmpty() && scName != null && !scName.isEmpty()) {
                    this.birdMap.put(name.trim().toLowerCase(), new Bird(name, scName));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot create bird map: %s", e.getMessage()));
        }
    }

    public Bird getBirdByName(String name) {
        if (name == null || name.isEmpty()) return null;
        if (this.birdMap.containsKey(name.trim().toLowerCase())) {
            return this.birdMap.get(name.trim().toLowerCase());
        }
        return null;
    }
}
