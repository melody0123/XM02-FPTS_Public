package com.fpts.bank_account_management.mapper;

import java.util.List;
import com.fpts.bank_account_management.domain.AccountInfo;

/**
 * 银行账户管理Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-08
 */
public interface AccountInfoMapper 
{
    /**
     * 查询银行账户管理
     * 
     * @param id 银行账户管理主键
     * @return 银行账户管理
     */
    public AccountInfo selectAccountInfoById(Long id);

    /**
     * 查询银行账户管理列表
     * 
     * @param accountInfo 银行账户管理
     * @return 银行账户管理集合
     */
    public List<AccountInfo> selectAccountInfoList(AccountInfo accountInfo);

    /**
     * 新增银行账户管理
     * 
     * @param accountInfo 银行账户管理
     * @return 结果
     */
    public int insertAccountInfo(AccountInfo accountInfo);

    /**
     * 修改银行账户管理
     * 
     * @param accountInfo 银行账户管理
     * @return 结果
     */
    public int updateAccountInfo(AccountInfo accountInfo);

    /**
     * 删除银行账户管理
     * 
     * @param id 银行账户管理主键
     * @return 结果
     */
    public int deleteAccountInfoById(Long id);

    /**
     * 批量删除银行账户管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAccountInfoByIds(String[] ids);
}
