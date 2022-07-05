package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.model.Clue;


import java.util.List;
import java.util.Map;

public interface ClueService {

    int saveCreateClues(Clue clue);


    List<Clue> queryActivityByConditionForPager(Map<String, Object> map);


    int queryCountOfActivityByCondition(Map<String, Object> map);

    Clue queryClueForDetailById(String id);

    Clue queryClueById(String id);

    int saveEditAClue(Clue clue);

    int deleteClueByIds(String[] ids);

    void saveConvert(Map<String, Object> map);

}
