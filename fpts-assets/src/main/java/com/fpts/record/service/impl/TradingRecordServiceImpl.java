package com.fpts.record.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fpts.common.annotation.DataScope;
import com.fpts.position.domain.AccountPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fpts.record.mapper.TradingRecordMapper;
import com.fpts.record.domain.TradingRecord;
import com.fpts.record.service.ITradingRecordService;
import com.fpts.common.core.text.Convert;

/**
 * 交易记录Service业务层处理
 * 
 * @author lzy
 * @date 2022-12-02
 */
@Service
public class TradingRecordServiceImpl implements ITradingRecordService 
{
    @Autowired
    private TradingRecordMapper tradingRecordMapper;

    /**
     * 查询交易记录
     * 
     * @param orderId 交易记录主键
     * @return 交易记录
     */
    @Override
    @DataScope(deptAlias = "trading_record", userAlias = "trading_record")
    public TradingRecord selectTradingRecordByOrderId(Long orderId)
    {
        return tradingRecordMapper.selectTradingRecordByOrderId(orderId);
    }

    /**
     * 查询交易记录列表
     * 
     * @param tradingRecord 交易记录
     * @return 交易记录
     */
    @Override
    @DataScope(deptAlias = "trading_record", userAlias = "trading_record")
    public List<TradingRecord> selectTradingRecordList(TradingRecord tradingRecord)
    {
        return tradingRecordMapper.selectTradingRecordList(tradingRecord);
    }

    @Override
    @DataScope(deptAlias = "trading_record", userAlias = "trading_record")
    public List<TradingRecord> selectTradingRecordListWithName(TradingRecord tradingRecord)
    {
        return tradingRecordMapper.selectTradingRecordListWithName(tradingRecord);
    }

    /**
     * 查询A股交易记录列表
     *
     * @param tradingRecord 交易记录
     * @return 交易记录
     */
    @Override
    @DataScope(deptAlias = "trading_record", userAlias = "trading_record")
    public List<TradingRecord> selectAStockTradingRecordList(TradingRecord tradingRecord)
    {
        return tradingRecordMapper.selectAStockTradingRecordList(tradingRecord);
    }

    /**
     * 查询B股交易记录列表
     *
     * @param tradingRecord 交易记录
     * @return 交易记录
     */
    @Override
    @DataScope(deptAlias = "trading_record", userAlias = "trading_record")
    public List<TradingRecord> selectBStockTradingRecordList(TradingRecord tradingRecord)
    {
        return tradingRecordMapper.selectBStockTradingRecordList(tradingRecord);
    }


    /**
     * 查询债券交易记录列表
     *
     * @param tradingRecord 交易记录
     * @return 交易记录
     */
    @Override
    @DataScope(deptAlias = "trading_record", userAlias = "trading_record")
    public List<TradingRecord> selectBondTradingRecordList(TradingRecord tradingRecord)
    {
        return tradingRecordMapper.selectBondTradingRecordList(tradingRecord);
    }

    /**
     * 新增交易记录
     * 
     * @param tradingRecord 交易记录
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "trading_record", userAlias = "trading_record")
    public int insertTradingRecord(TradingRecord tradingRecord)
    {
        return tradingRecordMapper.insertTradingRecord(tradingRecord);
    }

    /**
     * 修改交易记录
     * 
     * @param tradingRecord 交易记录
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "trading_record", userAlias = "trading_record")
    public int updateTradingRecord(TradingRecord tradingRecord)
    {
        return tradingRecordMapper.updateTradingRecord(tradingRecord);
    }

    /**
     * 批量删除交易记录
     * 
     * @param orderIds 需要删除的交易记录主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "trading_record", userAlias = "trading_record")
    public int deleteTradingRecordByOrderIds(String orderIds)
    {
        return tradingRecordMapper.deleteTradingRecordByOrderIds(Convert.toStrArray(orderIds));
    }

    /**
     * 删除交易记录信息
     * 
     * @param orderId 交易记录主键
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "trading_record", userAlias = "trading_record")
    public int deleteTradingRecordByOrderId(Long orderId)
    {
        return tradingRecordMapper.deleteTradingRecordByOrderId(orderId);
    }

    //统计
    @Override
    public List<Integer> getMonthlyData(String userId) {
        int len = 12;
        List<Integer> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            list.add(0);
        }
        TradingRecord tradingRecord = new TradingRecord();
        if (!userId.equals("1")){
            tradingRecord.setUserId(userId);
        }
        Calendar c = Calendar.getInstance();
        List<TradingRecord> tradingRecords = tradingRecordMapper.selectTradingRecordList(tradingRecord);
        for (TradingRecord item:tradingRecords) {
            if ( !"".equals(item.getAccountId())) {
                c.setTime(item.getOrderTime());
                int idx = c.get(Calendar.MONTH);
                int cur = list.get(idx);
                list.set(idx, 1 + cur);
            }
        }
        return list;
    }
}
