package it.fsants.JSONExtractor;

import org.apache.poi.ss.usermodel.Sheet;

public class JSONExtractor {

    /**
     * Use this if number of rows is not known
     * @param startRow start row including headers
     * @param startCol
     * @param endCol
     * @param sheet
     * @return
     */
    public static Table2JsonArr generate(int startRow, int startCol, int endCol, Sheet sheet) throws Xlsx2JSONException {
        Table t = new Table(startRow, startCol, endCol, sheet);
        return new Table2JsonArr(t);
    }

    /**
     * @param startRow start row including headers
     * @param endRow
     * @param startCol
     * @param endCol
     * @param sheet
     * @return
     * @throws Xlsx2JSONException
     */
    public static Table2JsonArr generate(int startRow, int endRow, int startCol, int endCol, Sheet sheet) throws Xlsx2JSONException {
        Table t = new Table(startRow, endRow, startCol, endCol, sheet);
        return new Table2JsonArr(t);
    }
}
