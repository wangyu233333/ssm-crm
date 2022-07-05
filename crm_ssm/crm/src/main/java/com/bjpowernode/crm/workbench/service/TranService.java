package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.model.FunnelVO;
import com.bjpowernode.crm.workbench.model.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    void saveCreateTran(Map<String, Object> map);

    Tran queryTranForDetailById(String id);

    List<FunnelVO> queryCountOfTranGroupByStage();
}
