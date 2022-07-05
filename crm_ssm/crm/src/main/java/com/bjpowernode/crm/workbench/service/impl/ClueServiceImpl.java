package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.model.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public int saveCreateClues(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryActivityByConditionForPager(Map<String, Object> map) {
        return clueMapper.selectActivityConditionForPage(map);
    }

    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public int saveEditAClue(Clue clue) {
        return clueMapper.updateClue(clue);
    }

    @Override
    public int deleteClueByIds(String[] ids) {
        return clueMapper.deleteClueByIds(ids);
    }

    @Override
    public void saveConvert(Map<String, Object> map) {

        String clueId = (String) map.get("clueId");
        User user = (User) map.get(Contants.SESSION_USER);
        String isCreateTran = (String) map.get("isCreateTran");

        //  根据id查询线索的信息
        Clue clue = clueMapper.selectClueByID(clueId);
        //  把线索中有关公司的信息转换到客户表中
        Customer customer = new Customer();
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formateDateTime(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setId(UUIDUtils.getUUID());
        customer.setName(clue.getCompany());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setOwner(user.getId());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        //  调用customerMapper方法保存客户信息
        customerMapper.insertCustomer(customer);

        //  把线索中有关公司的信息转换到联系人表中
        Contacts contacts = new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formateDateTime(new Date()));
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtils.getUUID());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        //  调用 contactsMapper 方法保存联系人信息
        contactsMapper.insertContacts(contacts);

        //  根据 clueId查询该线索下所有的备注信息
        List<ClueRemark> crList = clueRemarkMapper.selectClueRemarkByClueId(clueId);

        //  如果该线索不为空，把该线索下所有的备注转换到客户备注表中一份,把该线索下所有的备注转换到联系人备注表中一份
        if (crList != null && crList.size() > 0) {
            //  遍历 crList，封装客户备注
            CustomerRemark cur = null;
            ContactsRemark cor = null;
            List<CustomerRemark> curList = new ArrayList<>();
            List<ContactsRemark> corList = new ArrayList<>();
            for (ClueRemark cr : crList) {
                cur = new CustomerRemark();
                cur.setCreateBy(cr.getCreateBy());
                cur.setCreateTime(cr.getCreateTime());
                cur.setCustomerId(customer.getId());
                cur.setEditBy(cr.getEditBy());
                cur.setEditFlag(cr.getEditFlag());
                cur.setEditTime(cr.getEditTime());
                cur.setId(UUIDUtils.getUUID());
                cur.setNoteContent(cr.getNoteContent());
                curList.add(cur);

                cor = new ContactsRemark();
                cor.setContactsId(contacts.getId());
                cor.setCreateBy(cr.getCreateBy());
                cor.setCreateTime(cr.getCreateTime());
                cor.setEditBy(cr.getEditBy());
                cor.setEditFlag(cr.getEditFlag());
                cor.setEditTime(cr.getEditTime());
                cor.setContactsId(contacts.getId());
                cor.setId(UUIDUtils.getUUID());
                corList.add(cor);
            }
            customerRemarkMapper.insertCustomerRemarkByList(curList);
            contactsRemarkMapper.insertContactsRemarkByList(corList);
        }

        //  根据clueId查询该线索和和市场活动的关联关系
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);

        //  把该线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中
        if (carList != null && carList.size() > 0) {
            ContactsActivityRelation coar = null;
            List<ContactsActivityRelation> coarList = new ArrayList<>();
            for (ClueActivityRelation car : carList) {
                coar = new ContactsActivityRelation();
                coar.setId(UUIDUtils.getUUID());
                coar.setActivityId(car.getActivityId());
                coar.setContactsId(contacts.getId());
                coarList.add(coar);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationByList(coarList);
        }

        //  如果需要创建交易，则往交易表中添加一条记录
        if ("true".equals(isCreateTran)) {
            Tran tran = new Tran();
            tran.setActivityId((String) map.get("activityId"));
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtils.formateDateTime(new Date()));
            tran.setCustomerId(customer.getId());
            tran.setExpectedDate((String) map.get("expectedDate"));
            tran.setId(UUIDUtils.getUUID());
            tran.setMoney((String) map.get("money"));
            tran.setName((String) map.get("name"));
            tran.setOwner(user.getId());
            tran.setStage((String) map.get("stage"));

            tranMapper.insertTan(tran);

            //  如果需要创建交易，则还需要把该线索下所有的备注转换到转换到交易备注表中一份
            if (crList != null && crList.size() > 0) {
                List<TranRemark> trList = new ArrayList<>();;
                TranRemark tr = null;
                for (ClueRemark cr : crList) {
                    tr = new TranRemark();
                    tr.setCreateBy(cr.getCreateBy());
                    tr.setCreateTime(cr.getCreateTime());
                    tr.setId(UUIDUtils.getUUID());
                    tr.setEditBy(cr.getEditBy());
                    tr.setEditFlag(cr.getEditFlag());
                    tr.setEditTime(cr.getEditTime());
                    tr.setNoteContent(cr.getNoteContent());
                    tr.setTranId(tran.getId());
                    trList.add(tr);
                }
                tranRemarkMapper.insertTranRemarkByList(trList);
            }
        }
        //  删除该线索下所有的备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);

        //  删除该线索和市场活动的关联关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);

        //  删除该线索
        clueMapper.deleteClueById(clueId);

    }































}
