package it.fsants.JSONExtractor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Table2JsonArr {

    private final Table table;
    private final List<String> properties = new ArrayList<>();
    private final JSONArray array;

    Table2JsonArr(Table table) {
        this.table = table;
        this.array = new JSONArray();
    }

    /**
     * Extract rows of given table as JSON array of objects where properties name are defined on the first row.
     * @return
     * @throws Xlsx2JSONException
     */
    public JSONArray extract() throws Xlsx2JSONException {
        this.extractPropertiesName();

        while (this.table.hasNext()) {
            JSONObject o = new JSONObject();
            final int colNum = this.table.getColNum();
            for (int i = 0; i <= colNum; i++) {
                String val = this.table.getCellValue(i);
                o.put(this.properties.get(i), val);
            }
            this.array.put(o);
        }
        return this.array;
    }

    private void extractPropertiesName() throws Xlsx2JSONException {
        final int colNum = this.table.getColNum();
        for (int i = 0; i <= colNum; i++) {
            String prop = this.table.getStringCellValue(this.table.getCell(i));
            if (prop == null) {
                throw new Xlsx2JSONException(String.format("Cannot determine property name for column %s", i));
            }
            this.properties.add(this.getPropAsCamelCase(prop));
        }
    }

    private String getPropAsCamelCase(String s) {
        StringBuilder sb = new StringBuilder();
        String[] arr = s.trim().split(" ");
        for (int i = 0; i < arr.length; i++) {
            String sub = arr[i].toLowerCase();
            if (i==0) {
                sb.append(sub);
                continue;
            }
            sb.append(sub.substring(0, 1).toUpperCase()).append(sub.substring(1));
        }
        return sb.toString();
    }
}
