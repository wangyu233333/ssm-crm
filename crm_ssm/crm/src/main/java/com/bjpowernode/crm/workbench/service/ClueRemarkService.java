package com.bjpowernode.crm.workbench.service;


import com.bjpowernode.crm.workbench.model.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId);
}
