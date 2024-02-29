import it.fsants.JSONExtractor.JSONExtractor;
import it.fsants.JSONExtractor.Table2JsonArr;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;

public class TestJSON {

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        try (Workbook wb = new HSSFWorkbook();) {
            Sheet sheet = wb.createSheet();
            Row r = sheet.createRow(0);
            Cell c = r.createCell(0);
            c.setCellValue("Prop Name");
            c = r.createCell(1);
            c.setCellValue("Prop Num");

            r = sheet.createRow(1);
            c = r.createCell(0);
            c.setCellValue("Aquila Minore");
            c = r.createCell(1);
            c.setCellValue(12);

            r = sheet.createRow(2);
            c = r.createCell(0);
            c.setCellValue("Biancone");
            c = r.createCell(1);
            c.setCellValue(24);

            Table2JsonArr table2JsonArr = JSONExtractor.generate(0, 0, 1, sheet);
            JSONArray array = table2JsonArr.extract();
            System.out.println(array.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
