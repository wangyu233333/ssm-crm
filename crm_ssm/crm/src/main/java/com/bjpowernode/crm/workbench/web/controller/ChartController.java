package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChartController {

    @Autowired
    private TranService tranService;


    @RequestMapping("/workbench/chart/transaction/toIndex.do")
    public String toIndex() {
        //  跳转页面
        return "workbench/chart/transaction/index";
    }


    @RequestMapping("/workbench/chart/transaction/queryCountOfTranGroupByStages.do")
    @ResponseBody
    public Object queryCountOfTranGroupByStages() {
        //  调用service层方法，查询数据
        return tranService.queryCountOfTranGroupByStage();
    }
}
