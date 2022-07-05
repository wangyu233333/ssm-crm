package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.model.Activity;
import com.bjpowernode.crm.workbench.model.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.*;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;


    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        //  调用service层方法，查询所有用户
        List<User> userList = userService.queryAllUsers();

        //  把数据保存作用域
        request.setAttribute("userList", userList);

        //  转发市场活动主页面
        return "workbench/activity/index";

    }

    //  保存市场活动
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //  继续封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();

        try {
            //  调用service方法保存
            int ret = activityService.saveCreateActivity(activity);

            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试。。。");
            }

        } catch (Exception e) {
            e.printStackTrace();

            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试。。。");
        }

        return returnObject;
    }

    //  根据条件分页查询
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate, int pageNo, int pageSize) {

        //  封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);

        //  调用service层方法查询数据
        List<Activity> activityList = activityService.queryActivityByConditionForPager(map);

        int totalRows = activityService.queryCountOfActivityByCondition(map);

        //  根据查询结果生成查询响应信息
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("activityList", activityList);
        retMap.put("totalRows", totalRows);

        return retMap;
    }


    @RequestMapping("/workbench/activity/deleteActivityIds.do")
    @ResponseBody
    public Object deleteActivityIds(String[] id) {

        ReturnObject returnObject = new ReturnObject();

        //  调用service层方法，删除市场活动
        try {
            int ret = activityService.deleteActivityByIds(id);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试......");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试......");
        }
        return returnObject;
    }


    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        //  根据响应结果生成相应信息  调用service层方法，查询市场活动
        return activityService.queryActivityById(id);
    }


    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity, HttpSession session) {
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        //  封装参数
        activity.setEditTime(DateUtils.formateDate(new Date()));
        activity.setEditBy(user.getId());

        try {
            int ret = activityService.saveEditActivity(activity);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试.....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试.....");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/exportAllActivitys.do")
    public void exportAllActivitys(HttpServletResponse response) throws Exception {
        //  调用service层方法，查询所有的市场活动
        List<Activity> activityList = activityService.queryAllActivitys();

        //  创建excel文件，并且把activityList 写入到excel中
        HSSFWorkbook workbook = new HSSFWorkbook(); //  创建一个excel文件
        HSSFSheet sheet = workbook.createSheet("市场活动表");//excel文件中的一张表
        HSSFRow row = sheet.createRow(0);   //  行

        HSSFCell cell = row.createCell(0);  //  列
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        //  遍历activityList,创建HSSFRow对象，生成所有的数据行
        if (activityList != null && activityList.size() > 0) {
            Activity activity = null;
            for (int i = 0; i < activityList.size(); i++) {
                activity = activityList.get(i);

                //   遍历除一个  activity 生成一行
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        //  把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=activityList.xls");
        ServletOutputStream out = response.getOutputStream();

        workbook.write(out);
        workbook.close();
        out.flush();

    }


    @RequestMapping("/workbench/activity/exportActivityById.do")
    public void exportActivityById(String[] id, HttpServletResponse response) throws Exception {
        //  调用service层方法，查询市场活动
        List<Activity> activityList = activityService.queryActivityById(id);

        //  创建excel文件，并且把activityList 写入到excel中
        HSSFWorkbook workbook = new HSSFWorkbook(); //  创建一个excel文件
        HSSFSheet sheet = workbook.createSheet("市场活动表");//excel文件中的一张表
        HSSFRow row = sheet.createRow(0);   //  行

        HSSFCell cell = row.createCell(0);  //  列
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        //  遍历activityList,创建HSSFRow对象，生成所有的数据行
        if (activityList != null && activityList.size() > 0) {
            Activity activity = null;
            for (int i = 0; i < activityList.size(); i++) {
                activity = activityList.get(i);

                //   遍历除一个  activity 生成一行
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        //  把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=activityByIdList.xls");
        ServletOutputStream out = response.getOutputStream();

        workbook.write(out);
        workbook.close();
        out.flush();

    }

    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();

        try {
            HSSFWorkbook wb = new HSSFWorkbook(activityFile.getInputStream());
            //根据wb获取HSSFSheet对象，封装了一页的所有信息
            HSSFSheet sheet = wb.getSheetAt(0);//页的下标，下标从0开始，依次增加
            //根据sheet获取HSSFRow对象，封装了一行的所有信息
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {//sheet.getLastRowNum()：最后一行的下标
                row = sheet.getRow(i);//行的下标，下标从0开始，依次增加
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));
                activity.setCreateBy(user.getId());

                for (int j = 0; j < row.getLastCellNum(); j++) {//row.getLastCellNum():最后一列的下标+1
                    //根据row获取HSSFCell对象，封装了一列的所有信息
                    cell = row.getCell(j);//列的下标，下标从0开始，依次增加

                    //获取列中的数据
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if (j == 0) {
                        activity.setName(cellValue);
                    } else if (j == 1) {
                        activity.setStartDate(cellValue);
                    } else if (j == 2) {
                        activity.setEndDate(cellValue);
                    } else if (j == 3) {
                        activity.setCost(cellValue);
                    } else if (j == 4) {
                        activity.setDescription(cellValue);
                    }
                }

                //每一行中所有列都封装完成之后，把activity保存到list中
                activityList.add(activity);
            }

            //调用service层方法，保存市场活动
            int ret = activityService.saveCreateActivityByList(activityList);

            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(ret);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }

        return returnObject;

    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,HttpServletRequest request) {
        //  调用service层方法，查询数据
        Activity activity = activityService.queryActivityFoeDetailById(id);

        List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);

        //  把数据保存到作用域
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);

        //  请求转发
        return "workbench/activity/detail";

    }


}
