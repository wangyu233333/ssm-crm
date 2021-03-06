package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.model.Tran;
import com.bjpowernode.crm.workbench.model.TranHistory;
import com.bjpowernode.crm.workbench.model.TranRemark;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranHistoryService;
import com.bjpowernode.crm.workbench.service.TranRemarkService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TranController {

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TranService tranService;

    @Autowired
    private TranHistoryService tranHistoryService;

    @Autowired
    private TranRemarkService tranRemarkService;



    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request) {
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        request.setAttribute("stageList", stageList);

        //  ????????????
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request) {
        //  ??????Service??????????????????????????????
        List<User> userList = userService.queryAllUsers();
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        request.setAttribute("userList", userList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        request.setAttribute("stageList", stageList);

        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stageValue){
        //  ??????properties??????????????????????????????????????????
        ResourceBundle bundle = ResourceBundle.getBundle("possibilityy");

        return bundle.getString(stageValue);

    }

    @RequestMapping("/workbench/transaction/queryCustomerNameByName.do")
    @ResponseBody
    public Object queryAllCustomerName(String name){
      return customerService.queryAllCustomerNames(name);
    }

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    //  @RequestParam ??????????????????????????????????????? map???????????????????????? key ???????????? value
    public Object saveCreateTran(@RequestParam Map<String, Object> map, HttpSession session){
        //  ????????????
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));

        ReturnObject returnObject = new ReturnObject();
        try {
            //  ??????service?????????,?????????????????????-----
            tranService.saveCreateTran(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch(Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("????????????????????????????????????");
        }
        return returnObject;
    }


    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String tranId,HttpServletRequest request){
        //  ?????? service ?????????
        Tran tran = tranService.queryTranForDetailById(tranId);
        List<TranRemark> remarkList = tranRemarkService.queryTranRemarkForDetailByTranId(tranId);
        List<TranHistory> historyList = tranHistoryService.queryTranHistoryForDetailByTranId(tranId);


        //  ?????? stage ?????????????????????????????????
        ResourceBundle bundle = ResourceBundle.getBundle("possibilityy");
        String possibility = bundle.getString(tran.getStage());
        tran.setPossibility(possibility);

        request.setAttribute("tran",tran);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("historyList",historyList);

        //  ?????? service????????????????????????????????????
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        request.setAttribute("stageList",stageList);


        return "workbench/transaction/detail";
    }















}
