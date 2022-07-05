package com.bjpowernode.crm.workbench.mapper;


import com.bjpowernode.crm.workbench.model.Activity;
import com.bjpowernode.crm.workbench.model.Clue;


import java.util.List;
import java.util.Map;

public interface ClueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sun Jun 26 13:30:15 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sun Jun 26 13:30:15 CST 2022
     */
    int insert(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sun Jun 26 13:30:15 CST 2022
     */
    int insertSelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sun Jun 26 13:30:15 CST 2022
     */
    Clue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sun Jun 26 13:30:15 CST 2022
     */
    int updateByPrimaryKeySelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sun Jun 26 13:30:15 CST 2022
     */
    int updateByPrimaryKey(Clue record);

    //  保存创建的线索
    int insertClue(Clue clue);


    /**
     * 根据条件分页查询市场活动列表
     *
     * @param map
     * @return
     */
     List<Clue> selectActivityConditionForPage(Map<String, Object> map);



    /**
     * 根据条件查询市场活动总条数
     *
     * @param map
     * @return
     */
    int selectCountOfActivityByCondition(Map<String, Object> map);


    //  根据id查询clue
    Clue selectClueForDetailById(String id);

    // 根据id查询Clue
    Clue selectClueById(String id);

    //  保存更新的市场活动活动
    int updateClue(Clue clue);

    // 根据ids批量删除市场活动
    int deleteClueByIds(String[] ids);


    //  根据id查询线索的信息
    Clue selectClueByID(String id);

    //  根据 id 删除线索
    int deleteClueById(String id);


}