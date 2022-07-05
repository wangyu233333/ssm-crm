package poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;

/**
 * 使用 apache-poi生成excel文件
 */
public class CreateExcelTest {
    public static void main(String[] args)  throws Exception{
        //  创建 HSSFWorkbook对象，对应一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();

        //  使用 workbook 创建HSSFSheet对象,对应workbook文件中的一页
        HSSFSheet sheet = workbook.createSheet("学生信息列表");

        //  使用 sheet 创建 HSSFRow对象，对应sheet中的一行
        HSSFRow row = sheet.createRow(0);//  行号，从0开始，依次增加

        //  使用 row，创建HSSFCell对象，对应row中的列
        HSSFCell cell = row.createCell(0);//  列的编号，从0开始，依次增加
        cell.setCellValue("学号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("年龄");

        //  生成HSSFCellStyle对象
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        //  使用sheet创建10个HSSFRow对象，对应sheet中的10行
        for (int i = 1; i <=10 ; i++) {
            row = sheet.createRow(i);

            cell = row.createCell(0);
            cell.setCellValue(100+i);
            cell = row.createCell(1);
            cell.setCellValue("NAME"+i);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(20+i);
        }

        //  调用工具函数生成excel文件
        FileOutputStream os = new FileOutputStream("D:\\idea\\Project\\crm_ssm\\poiTest\\studentList.xls");//目录必须手动创建，文件如果不存在，poi会自动创建
        workbook.write(os);

        //  关闭
        os.close();
        workbook.close();


    }


}



























