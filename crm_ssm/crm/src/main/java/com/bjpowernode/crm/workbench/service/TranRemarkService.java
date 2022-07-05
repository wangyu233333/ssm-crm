package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.model.TranRemark;

import java.util.List;

public interface TranRemarkService {
    List<TranRemark> queryTranRemarkForDetailByTranId(String tranId);
}
