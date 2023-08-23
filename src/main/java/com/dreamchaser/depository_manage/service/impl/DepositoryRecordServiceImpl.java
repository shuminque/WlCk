package com.dreamchaser.depository_manage.service.impl;

import com.dreamchaser.depository_manage.entity.DepositoryRecord;
import com.dreamchaser.depository_manage.entity.Material;
import com.dreamchaser.depository_manage.entity.Notification;
import com.dreamchaser.depository_manage.entity.SimpleDepositoryRecord;
import com.dreamchaser.depository_manage.exception.MyException;
import com.dreamchaser.depository_manage.mapper.*;
import com.dreamchaser.depository_manage.pojo.DepositoryRecordP;
import com.dreamchaser.depository_manage.pojo.SimpleDepositoryRecordP;
import com.dreamchaser.depository_manage.service.DepositoryRecordService;
import com.dreamchaser.depository_manage.service.NotificationService;
import com.dreamchaser.depository_manage.utils.ObjectFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Dreamchaser
 */
@Service
public class DepositoryRecordServiceImpl implements DepositoryRecordService {
    @Autowired
    private DepositoryRecordMapper depositoryRecordMapper;
    @Autowired
    private DepositoryMapper depositoryMapper;
    @Autowired
    private TransferRecordMapper transferRecordMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MaterialEnginMapper materialEnginMapper;
    @Autowired
    private MaterialTypeMapper materialTypeMapper;


    @Override
    public Integer apply(Map<String, Object> map) {
        map.put("applyTime",new Date());
        map.put("state","待审核");
        return depositoryRecordMapper.insertDepositoryRecord(map);
    }

    @Override
    @Transactional
    public Integer applyAndReview(Map<String, Object> map, Integer userId) {
        // 设置申请时间和已出库状态
        map.put("applyTime", new Date());
        map.put("state", "已出库");

        // 设置申请人ID和审核人ID
        map.put("applicantId", userId);
        map.put("reviewerId", userId);

        // 插入出库申请记录
        depositoryRecordMapper.insertDepositoryRecord(map);

        BigInteger recordIdBigInteger = (BigInteger) map.get("id");
        Integer recordId = recordIdBigInteger.intValue();

        // 获取刚插入的出库记录
        DepositoryRecord record = depositoryRecordMapper.findDepositoryRecordById(recordId);

        // 设置出库相关信息
        map.put("depositoryId", record.getDepositoryId());
        map.put("atId", record.getAtId());
        map.put("model", record.getModel());
        map.put("mname", record.getMname());

        // 执行出库逻辑
        List<Material> list = materialMapper.findMaterialByCondition(map);
        Material material = list.get(0);
        if (material.getQuantity() > record.getQuantity()) {
            // 计算新的总数量
            double newQuantity = material.getQuantity() - record.getQuantity();
            // 计算出库的总价
            double outPrice = record.getPrice() * record.getQuantity();
            // 计算新的总价
            double newPrice = material.getPrice() - outPrice;
            BigDecimal bdNewPrice = new BigDecimal(newPrice);
            bdNewPrice = bdNewPrice.setScale(2, RoundingMode.HALF_UP); // 保留两位小数，四舍五入
            // 计算新的均价
            double newUnitPrice = newPrice / newQuantity;
            BigDecimal bdNewUnitPrice = new BigDecimal(newUnitPrice);
            bdNewUnitPrice = bdNewUnitPrice.setScale(2, RoundingMode.HALF_UP); // 保留两位小数，四舍五入
            // 更新物料
            material.setPrice(bdNewPrice.doubleValue());
            material.setQuantity(newQuantity);
            material.setUnitPrice(bdNewUnitPrice.doubleValue());
            materialMapper.updateMaterial(material);
        } else if (material.getQuantity().equals(record.getQuantity())) {
            materialMapper.deleteMaterialById(material.getId());
        } else {
            throw new MyException("库存不足于该出库请求");
        }
        return 1; // 返回值可以根据你的需求进行调整
    }

    /**
     * 转移申请
     * @param map 仓库调度信息
     * @return
     */
    @Override
    @Transactional
    public Integer transferApply(Map<String, Object> map) {
        map.put("state","待审核");
        map.put("applyTime",new Date());
        map.put("type",0);
        map.put("depositoryId",map.get("fromId"));
        depositoryRecordMapper.insertDepositoryRecord(map);
        map.put("fromId",map.get("id"));
        //清除主键
        map.remove("id");
        map.put("depositoryId",map.get("toId"));
        map.put("type",1);
        depositoryRecordMapper.insertDepositoryRecord(map);
        map.put("toId",map.get("id"));
        //清除主键
        map.remove("id");
        return transferRecordMapper.addTransferRecord(map);
    }


    @Override
    @Transactional
    public Integer review(Map<String, Object> map,Integer userid) {
        if (map.containsKey("reviewPass")) {
            map.put("reviewTime", new Date());
            map.put("reviewerId", userid);
            Integer reviewPass = (Integer) map.get("reviewPass");
            DepositoryRecord record;
            if (reviewPass == 1) {
                record = depositoryRecordMapper.findDepositoryRecordById(ObjectFormatUtil.toInteger(map.get("id")));
                map.put("depositoryId", record.getDepositoryId());
                map.put("atId", record.getAtId());
                map.put("model", record.getModel());
                map.put("mname", record.getMname());
                List<Material> list = materialMapper.findMaterialByCondition(map);
                Material material = list.get(0);
                if (1 == record.getType()) {
                    map.put("state", "已入库");
                    //这里貌似会引起并发问题
                    //material.setUnitPrice((material.getPrice()+(record.getPrice()*record.getQuantity()))/(material.getQuantity()+record.getQuantity()));
                    //material.setPrice(material.getPrice()+(record.getQuantity()*record.getPrice())) ;  //总价=原总+入单*入数
                    double totalPrice = material.getPrice() + (record.getPrice() * record.getQuantity());
                    BigDecimal bdTotalPrice = new BigDecimal(totalPrice);
                    bdTotalPrice = bdTotalPrice.setScale(2, RoundingMode.HALF_UP); // 保留两位小数，四舍五入
                    material.setPrice(bdTotalPrice.doubleValue());
                    // 计算新的总数量
                    double totalQuantity = material.getQuantity() + record.getQuantity();
                    // 计算新的均价
                    double unitPrice = totalPrice / totalQuantity;
                    BigDecimal bdUnitPrice = new BigDecimal(unitPrice);
                    bdUnitPrice = bdUnitPrice.setScale(2, RoundingMode.HALF_UP); // 保留两位小数，四舍五入
                    material.setUnitPrice(bdUnitPrice.doubleValue());
                    material.setQuantity(material.getQuantity() + record.getQuantity());
                    materialMapper.updateMaterial(material);
                } else {
                    if (material.getQuantity() > record.getQuantity()) {
                        // 计算新的总数量
                        double newQuantity = material.getQuantity() - record.getQuantity();
                        // 计算出库的总价
                        double outPrice = record.getPrice() * record.getQuantity();
                        // 计算新的总价
                        double newPrice = material.getPrice() - outPrice;
                        BigDecimal bdNewPrice = new BigDecimal(newPrice);
                        bdNewPrice = bdNewPrice.setScale(2, RoundingMode.HALF_UP); // 保留两位小数，四舍五入
                        // 计算新的均价
                        double newUnitPrice = newPrice / newQuantity;
                        BigDecimal bdNewUnitPrice = new BigDecimal(newUnitPrice);
                        bdNewUnitPrice = bdNewUnitPrice.setScale(2, RoundingMode.HALF_UP); // 保留两位小数，四舍五入
                        // 更新物料
                        material.setPrice(bdNewPrice.doubleValue());
                        material.setQuantity(newQuantity);
                        material.setUnitPrice(bdNewUnitPrice.doubleValue());
                        materialMapper.updateMaterial(material);
                    } else if (material.getQuantity().equals(record.getQuantity())) {
                        materialMapper.deleteMaterialById(material.getId());
                    } else {
                        throw new MyException("库存不足于该出库请求");
                    }
                    map.put("state", "已出库");
                }
            } else {
                record = depositoryRecordMapper.findDepositoryRecordById(ObjectFormatUtil.toInteger(map.get("id")));
                map.put("state", "审核未通过");
                // 添加通知

                Integer atId = record.getAtId();  // 假设这是AT号
                String mname = record.getMname();  // 假设这是品名
                Integer type = record.getType();
                String typeTag = type == 0 ? "[出库]" : "[入库]";
                String notificationContent = typeTag + " 申请已被拒绝,AT号:" + atId + ", 品名: " + mname;

                Notification notification = new Notification();
                notification.setUserId(record.getApplicantId());  // 假设申请人的ID是这样获取的
                notification.setContent(notificationContent);
                notification.setDateCreated(new Date());
                notification.setIsRead(false);
                notificationService.insertNotification(notification);
            }
        }
            return depositoryRecordMapper.updateDepositoryRecord(map);
    }

    @Override
    public Integer updateDepositoryRecord(Map<String, Object> map) {
        return depositoryRecordMapper.updateDepositoryRecord(map);
    }

    @Override
    public DepositoryRecordP findDepositoryRecordById(Integer id) {
        return singlePack(depositoryRecordMapper.findDepositoryRecordById(id));
    }

    public Integer checkPass(){
        return null;
    }

    @Override
    public List<DepositoryRecord> findDepositoryRecordAll() {
        return depositoryRecordMapper.findDepositoryRecordAll();
    }

    @Override
    public List<DepositoryRecordP> findDepositoryRecordPByCondition(Map<String,Object> map) {
        Integer size = 8,page=1;
        if (map.containsKey("size")){
            size=ObjectFormatUtil.toInteger(map.get("size"));
            map.put("size", size);
        }
        if (map.containsKey("page")){
            page=ObjectFormatUtil.toInteger(map.get("page"));
            map.put("begin",(page-1)*size);
        }
        return pack(depositoryRecordMapper.findDepositoryRecordByCondition(map));
    }

    @Override
    public List<SimpleDepositoryRecordP> findMyTask(Map<String, Object> map) {
        Integer size = 10,page=1;
        if (map.containsKey("size")){
            size=ObjectFormatUtil.toInteger(map.get("size"));
            map.put("size", size);
        }
        if (map.containsKey("page")){
            page=ObjectFormatUtil.toInteger(map.get("page"));
            map.put("begin",(page-1)*size);
        }
        return simplePack(depositoryRecordMapper.findMyTask(map));
    }

    @Override
    public Integer deleteDepositoryRecordById(Integer id) {
        return depositoryRecordMapper.deleteDepositoryRecordById(id);
    }

    @Override
    public Integer deleteDepositoryRecordByIds(List<Integer> list) {
        return depositoryRecordMapper.deleteDepositoryRecordByIds(list);
    }

    @Override
    public Integer findCount() {
        return depositoryRecordMapper.findCount();
    }

    @Override
    public Integer findMyTaskCount(Map<String,Object> map) {
        return depositoryRecordMapper.findMyTaskCount(map);
    }

    @Override
    public Integer findCountByCondition(Map<String, Object> map) {
        return depositoryRecordMapper.findCountByCondition(map);
    }



    /**
     * 对查出来的记录进行包装，包装成前端需要的数据
     * @param list SimpleDepositoryRecord集合
     * @return 包装好的集合
     */
    private List<SimpleDepositoryRecordP> simplePack(List<SimpleDepositoryRecord> list){
        List<SimpleDepositoryRecordP> result=new ArrayList<>(list.size());
        for (SimpleDepositoryRecord record: list){
            SimpleDepositoryRecordP d=new SimpleDepositoryRecordP(record);
            d.setApplicantName(userMapper.findUserNameById(record.getApplicantId()));
            result.add(d);
        }
        return result;
    }
    /**
     * 对查出来的记录进行包装，包装成前端需要的数据
     * @param list DepositoryRecord集合
     * @return 包装好的集合
     */
    private List<DepositoryRecordP> pack(List<DepositoryRecord> list){
        List<DepositoryRecordP> result=new ArrayList<>(list.size());
        for (DepositoryRecord record: list){
            result.add(singlePack(record));
        }
        return result;
    }
    private DepositoryRecordP singlePack(DepositoryRecord record){
        DepositoryRecordP d=new DepositoryRecordP(record);
        d.setApplicantName(userMapper.findUserNameById(record.getApplicantId()));
        d.setDepositoryName(depositoryMapper.findDepositoryNameById(record.getDepositoryId()));
        if (record.getReviewerId()!=null){
            d.setReviewerName(userMapper.findUserNameById(record.getReviewerId()));
        }
        if (record.getCheckerId()!=null){
            d.setCheckerName(userMapper.findUserNameById(record.getCheckerId()));
        }
        return d;
    }

}
