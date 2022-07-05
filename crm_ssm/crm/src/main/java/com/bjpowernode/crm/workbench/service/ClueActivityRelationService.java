package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.model.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveClueActivityRelationByList(List<ClueActivityRelation> list);

    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);
}
