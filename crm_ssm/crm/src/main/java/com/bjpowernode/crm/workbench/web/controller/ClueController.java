package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.model.Activity;
import com.bjpowernode.crm.workbench.model.Clue;
import com.bjpowernode.crm.workbench.model.ClueActivityRelation;
import com.bjpowernode.crm.workbench.model.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request) {
        //  调用service层方法
        List<User> userList = userService.queryAllUsers();

        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        //  把数据保存到作用域
        request.setAttribute("userList", userList);
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("clueStateList", clueStateList);
        request.setAttribute("sourceList", sourceList);

        //  请求转发
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //  封装参数
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        clue.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            //  调用service层方法
            int ret = clueService.saveCreateClues(clue);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setMessage("系统忙，请稍后重试！！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setMessage("系统忙，请稍后重试！！！");
        }
        return returnObject;
    }


    //  根据条件分页查询
    @RequestMapping("/workbench/activity/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(String name, String owner, String company, String phone, String source, String mphone, String state, int pageNo, int pageSize) {

        //  封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("company", company);
        map.put("phone", phone);
        map.put("mphone", mphone);
        map.put("source", source);
        map.put("state", state);
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);

        //  调用service层方法查询数据
        List<Clue> clueList = clueService.queryActivityByConditionForPager(map);

        int totalRows = clueService.queryCountOfActivityByCondition(map);

        //  根据查询结果生成查询响应信息
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("clueList", clueList);
        retMap.put("totalRows", totalRows);

        return retMap;
    }


    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id, HttpServletRequest request) {
        //  调用service层方法，查询数据
        Clue clue = clueService.queryClueForDetailById(id);
        //  根据clueId查询clueRemark
        String clueId =clue.getId();
        //  List<ClueRemark> emarkList = clueRemarkService.queryClueRemarkForDetailByClueId(clueId);
        //  根据clueId查询相关的市场活动信息
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(clueId);
        //  把数据保存到作用域
        request.setAttribute("clue", clue);
        //  request.setAttribute("remarkList", remarkList);
        request.setAttribute("activityList", activityList);
        //  请求转发
        return "workbench/clue/detail";

    }


    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String activityName, String clueId) {
        //  封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);

        //  调用service层方法
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);

        return activityList;

    }

    @RequestMapping("/workbench/clue/queryClueById.do")
    @ResponseBody
    public Object queryClueById(String id) {
        // 调用service层方法
        return clueService.queryClueById(id);
    }

    @RequestMapping("/workbench/clue/saveEditClue.do")
    @ResponseBody
    public Object saveEditClue(Clue clue, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //  封装参数
        clue.setEditBy(user.getId());
        clue.setEditTime(DateUtils.formateDateTime(new Date()));

        ReturnObject returnObject = new ReturnObject();
        try {
            //  调用service层方法
            int ret = clueService.saveEditAClue(clue);
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

    @RequestMapping("/workbench/clue/saveBund.do")
    @ResponseBody
    public Object saveBund(String[] activityId, String clueId) {
        List<ClueActivityRelation> relationList = new ArrayList<>();
        ClueActivityRelation car = null;
        //  封装参数
        for (String aid : activityId) {
            car = new ClueActivityRelation();
            car.setId(UUIDUtils.getUUID());
            car.setActivityId(aid);
            car.setClueId(clueId);
            relationList.add(car);
        }
        ReturnObject returnObject = new ReturnObject();
        //  调用service层方法
        try {
            int ret = clueActivityRelationService.saveClueActivityRelationByList(relationList);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
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

    @RequestMapping("/workbench/clue/saveUnBund.do")
    @ResponseBody
    public Object saveUnBund(ClueActivityRelation relation) {
        ReturnObject returnObject = new ReturnObject();
        try {
            //  调用service层方法
            int ret = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);

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

    @RequestMapping("/workbench/clue/deleteClueByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] ids) {
        ReturnObject returnObject = new ReturnObject();
        System.out.println("ids = " + ids);
        try {
            //  调用service层方法
            int ret = clueService.deleteClueByIds(ids);
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

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id, HttpServletRequest request) {
        //  调用service层方法
        Clue clue = clueService.queryClueForDetailById(id);

        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        request.setAttribute("clue", clue);
        request.setAttribute("stageList", stageList);

        return "workbench/clue/convert";
    }


    @RequestMapping("/workbench/clue/selectActivityForConvertByNameClueId.do")
    @ResponseBody
    public Object selectActivityForConvertByNameClueId(String activityName, String clueId) {
        //  封装参数
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);
        //  调用service层方法
        List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
        return activityList;

    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId, String money, String name, String expectedDate, String stage, String activityId, String isCreateTran, HttpSession session) {
        //   封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("clueId", clueId);
        map.put("money", money);
        map.put("name", name);
        map.put("expectedDate", expectedDate);
        map.put("stage", stage);
        map.put("activityId", activityId);
        map.put("isCreateTran", isCreateTran);
        map.put(Contants.SESSION_USER, session.getAttribute(Contants.SESSION_USER));

        ReturnObject returnObject = new ReturnObject();
        try {
            //  调用service层方法
            clueService.saveConvert(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试.....");
        }
        return returnObject;
    }


}
