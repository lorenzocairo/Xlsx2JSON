package it.fsants.JSONExtractor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class Table {

    private final int startRow;
    private int endRow;
    private final int startCol;
    private final int endCol;
    private final Sheet sheet;
    private int currentRow;

    Table(int startRow, int startCol, int endCol, Sheet sheet) throws Xlsx2JSONException {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endCol = endCol;
        this.sheet = sheet;
        this.currentRow = startRow;

        if ((this.endCol - this.startCol) < 0) {
            throw new Xlsx2JSONException("Table start row cannot be < than end row");
        }
    }

    Table(int startRow, int endRow, int startCol, int endCol, Sheet sheet) throws Xlsx2JSONException {
        this(startRow, startCol, endCol, sheet);
        this.endRow = endRow;

        if ((this.endRow - this.startRow) < 2) {
            throw new Xlsx2JSONException("Table cannot have one row");
        }
    }

    int getColNum() {
        return this.endCol - this.startCol;
    }

    boolean hasNext() {
        this.currentRow++;
        if (this.endRow > 0) {
            return this.currentRow <= this.endRow;
        }
        Row r = this.sheet.getRow(this.currentRow);
        return r != null && this.isRowEmpty(r);
    }

    private boolean isRowEmpty(Row row) {
        boolean empty = false;
        for (int i = this.startCol; i < this.endCol; i++) {
            Cell c = row.getCell(i);
            empty = empty | (c != null && !c.getCellType().equals(CellType.BLANK));
        }
        return empty;
    }

    /**
     * @param col relative column, starts at 0
     * @return
     */
    Cell getCell(int col) throws Xlsx2JSONException {
        if ((this.startCol + col) > this.endCol) {
            throw new Xlsx2JSONException(String.format("Request column %s is outside range", col));
        }
        return this.sheet.getRow(this.currentRow) != null ? this.sheet.getRow(this.currentRow).getCell(this.startCol + col) : null;
    }

    String getCellValue(int col) throws Xlsx2JSONException {
        Cell c = this.getCell(col);
        if (c == null) return null;
        switch (c.getCellType()) {
            case STRING -> {
                return this.getStringCellValue(c);
            }
            case NUMERIC -> {
                return String.valueOf(this.getNumericCellValue(c));
            }
            default -> {
                return null;
            }
        }
    }

    String getStringCellValue(Cell c) throws Xlsx2JSONException {
        if (c == null || !c.getCellType().equals(CellType.STRING)) return null;
        return c.getStringCellValue();
    }

    Double getNumericCellValue(Cell c) throws Xlsx2JSONException {
        if (c == null || !c.getCellType().equals(CellType.NUMERIC)) return null;
        return c.getNumericCellValue();
    }
}
