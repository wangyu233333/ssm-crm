package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.model.TranHistory;

import java.util.List;

public interface TranHistoryService {
    List<TranHistory> queryTranHistoryForDetailByTranId(String tranId);
}
