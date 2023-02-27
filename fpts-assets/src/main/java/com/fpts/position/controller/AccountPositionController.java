package com.fpts.position.controller;

import java.util.List;

import com.fpts.assets.domain.AccountAssets;
import com.fpts.assets.service.IAccountAssetsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.fpts.common.annotation.Log;
import com.fpts.common.enums.BusinessType;
import com.fpts.position.domain.AccountPosition;
import com.fpts.position.service.IAccountPositionService;
import com.fpts.common.core.controller.BaseController;
import com.fpts.common.core.domain.AjaxResult;
import com.fpts.common.utils.poi.ExcelUtil;
import com.fpts.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 账户持仓Controller
 *
 * @author lzy
 * @date 2022-12-12
 */
@Controller
@RequestMapping("/position/accountPosition")
public class AccountPositionController extends BaseController
{
    private String prefix = "position/accountPosition";

    @Autowired
    private IAccountPositionService accountPositionService;

    @Autowired
    private IAccountAssetsService accountAssetsService;

    @RequiresPermissions("position:accountPosition:view")
    @GetMapping()
    public String accountPosition()
    {
        return prefix + "/accountPosition";
    }

    /**
     * 查询账户持仓列表
     */
    @RequiresPermissions("position:accountPosition:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AccountPosition accountPosition)
    {
        startPage();
        List<AccountPosition> list = accountPositionService.selectAccountPositionList(accountPosition);
        return getDataTable(list);
    }

    /**
     * 导出账户持仓列表
     */
    @RequiresPermissions("position:accountPosition:export")
    @Log(title = "账户持仓", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AccountPosition accountPosition)
    {
        List<AccountPosition> list = accountPositionService.selectAccountPositionList(accountPosition);
        ExcelUtil<AccountPosition> util = new ExcelUtil<AccountPosition>(AccountPosition.class);
        return util.exportExcel(list, "账户持仓数据");
    }

    /**
     * 新增账户持仓
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存账户持仓
     */
    @RequiresPermissions("position:accountPosition:add")
    @Log(title = "账户持仓", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AccountPosition accountPosition)
    {
        return toAjax(accountPositionService.insertAccountPosition(accountPosition));
    }

    /**
     * 修改账户持仓
     */
    @RequiresPermissions("position:accountPosition:edit")
    @GetMapping("/edit/{orderId}")
    public String edit(@PathVariable("orderId") Long orderId, ModelMap mmap)
    {
        AccountPosition accountPosition = accountPositionService.selectAccountPositionByOrderId(orderId);
        mmap.put("accountPosition", accountPosition);
        return prefix + "/edit";
    }

    /**
     * 修改保存账户持仓
     */
    @RequiresPermissions("position:accountPosition:edit")
    @Log(title = "账户持仓", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AccountPosition accountPosition)
    {
        return toAjax(accountPositionService.updateAccountPosition(accountPosition));
    }

    /**
     * 删除账户持仓
     */
    @RequiresPermissions("position:accountPosition:remove")
    @Log(title = "账户持仓", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(accountPositionService.deleteAccountPositionByOrderIds(ids));
    }

    /**
     * 打印跳转
     */
    @RequestMapping("/print")
    public String print(){
        return prefix + "/print";
    }
    /**
     * 打印操作
     */
    @PostMapping("/printToHtml")
    @ResponseBody
    public TableDataInfo printToHtml(AccountPosition accountPosition)
    {
        List<AccountPosition> list = accountPositionService.selectAccountPositionList(accountPosition);
        return getDataTable(list);
    }

    /**
     * 统计
     */
    @GetMapping("/eCharts")
    public String statistics()
    {
        return prefix + "/eCharts";
    }

    @PostMapping("/eCharts")
    @ResponseBody
    public List<Integer> statisticsData(Long userId)
    {
        return accountPositionService.getMonthlyData(userId);
    }

    /**
     * 输入卖出数量
     */
    @GetMapping("/sellPart/{orderId}")
    public String setSellAmount(ModelMap mmap, @PathVariable("orderId")  Long id)
    {
        AccountPosition accountPosition = accountPositionService.selectAccountPositionByOrderId(id);
        System.out.println(accountPosition.toString());
        mmap.put("accountPosition", accountPosition);
        return prefix + "/sellPart";
    }

    /**
     * 输入卖出数量保存
     */
    @PostMapping("/sellPartSave")
    public String setSellAmountSave(@RequestParam("orderId") String orderId, @RequestParam("amount") String amount){
        Long o = Long.parseLong(orderId);
        Long a = Long.parseLong(amount);
        AccountPosition accountPosition = accountPositionService.selectAccountPositionByOrderId(o);
        Long newAmount = accountPosition.getProductAmount() - a;
        if (accountPosition.getProductAmount().equals(a)){
            accountPositionService.deleteAccountPositionByOrderId(o);
        }
        else{
            accountPosition.setProductAmount(newAmount);
            accountPositionService.updateAccountPosition(accountPosition);
        }

        return "position/accountPosition";
    }

    @PostMapping("/sell")
    @ResponseBody
    public AjaxResult toItemComplete(@RequestParam(value="orderId")  Long id, @RequestParam(value="sellAmount")  Long sellAmount, @RequestParam(value="productPrice")  Long productPrice, @RequestParam(value="productType")  Long productType ){
        System.out.println(id + " " + sellAmount + " " + productPrice+ " " + productType +" ");
        AccountPosition accountPosition =  accountPositionService.selectAccountPositionByOrderId(id);
        accountPosition.setProductAmount(accountPosition.getProductAmount() - sellAmount);
        System.out.println(accountPosition.getProductAmount());
//        AccountAssets accountAssets = accountAssetsService.selectAccountAssetsByNo()
        return toAjax(accountPositionService.updateAccountPosition(accountPosition));

    }
}
