package com.fpts.finance_collection.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import com.fpts.assets.domain.AccountAssets;
import com.fpts.assets.mapper.AccountAssetsMapper;
import com.fpts.assets.service.IAccountAssetsService;
import com.fpts.record.domain.TradingRecord;
import com.fpts.record.service.ITradingRecordService;
import com.fpts.finance_warehouse.domain.FinanceWarehouse;
import com.fpts.finance_warehouse.service.IFinanceWarehouseService;
import com.fpts.record.service.ITradingRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.fpts.common.annotation.Log;
import com.fpts.common.enums.BusinessType;
import com.fpts.finance_collection.domain.FinanceCollection;
import com.fpts.finance_collection.service.IFinanceCollectionService;
import com.fpts.common.core.controller.BaseController;
import com.fpts.common.core.domain.AjaxResult;
import com.fpts.common.utils.poi.ExcelUtil;
import com.fpts.common.core.page.TableDataInfo;

/**
 * 产品收藏Controller
 *
 * @author laybxc
 * @date 2022-11-30
 */
@Controller
@RequestMapping("/finance_collection/collection")
public class FinanceCollectionController extends BaseController
{
    private String prefix = "finance_collection/collection";

    @Autowired
    private IFinanceCollectionService financeCollectionService;
    @Autowired
    private IAccountAssetsService accountAssetsService;

    @Autowired
    private IFinanceWarehouseService financeWarehouseServiceImpl;

    @Autowired
    private ITradingRecordService tradingRecordService;


    @RequiresPermissions("finance_collection:collection:view")
    @GetMapping()
    public String collection()
    {
        return prefix + "/collection";
    }

    /**
     * 查询产品收藏列表
     */
    @RequiresPermissions("finance_collection:collection:list")
    @PostMapping("/list")
    @ResponseBody
    /*public TableDataInfo list(FinanceWarehouse financeCollection)
    {
        startPage();
        List<FinanceWarehouse> list = financeWarehouseServiceImpl.selectFinanceWarehouseListTocoll(financeCollection);
        return getDataTable(list);
    }*/
    public TableDataInfo list(FinanceCollection financeCollection)
    {
        startPage();
        List<FinanceCollection> list = financeCollectionService.selectFinanceCollectionListWithColl(financeCollection);
        return getDataTable(list);
    }

    /**
     * 导出产品收藏列表
     */
    @RequiresPermissions("finance_collection:collection:export")
    @Log(title = "产品收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FinanceCollection financeCollection)
    {
        List<FinanceCollection> list = financeCollectionService.selectFinanceCollectionList(financeCollection);
        ExcelUtil<FinanceCollection> util = new ExcelUtil<FinanceCollection>(FinanceCollection.class);
        return util.exportExcel(list, "产品收藏数据");
    }

    /**
     * 新增产品收藏
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存产品收藏
     */
    @RequiresPermissions("finance_collection:collection:add")
    @Log(title = "产品收藏", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FinanceCollection financeCollection)
    {
        return toAjax(financeCollectionService.insertFinanceCollection(financeCollection));
    }

    /**
     * 修改产品收藏
     */
    @RequiresPermissions("finance_collection:collection:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinanceCollection financeCollection = financeCollectionService.selectFinanceCollectionById(id);
        mmap.put("financeCollection", financeCollection);
        return prefix + "/edit";
    }

    /**
     * 修改保存产品收藏
     */
    @RequiresPermissions("finance_collection:collection:edit")
    @Log(title = "产品收藏", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FinanceCollection financeCollection)
    {
        return toAjax(financeCollectionService.updateFinanceCollection(financeCollection));
    }

    /**
     * 删除产品收藏
     */
    @RequiresPermissions("finance_collection:collection:remove")
    @Log(title = "产品收藏", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(financeCollectionService.deleteFinanceCollectionByIds(ids));
    }


    /**
     * 统计报表
     */
    @RequestMapping("/chart")

    public String showChart(ModelMap mmap){
        List<FinanceCollection> list = financeCollectionService.selectFinanceCollectionListWithColl(new FinanceCollection());
        Map<String, Integer> map = new TreeMap<String, Integer>();

        for(FinanceCollection f: list){
            String type = f.getType();

            if(map.containsKey(type)){
                int cnt = map.get(type);
                map.replace(type, cnt, cnt + 1);
            }else{
                map.put(type, 1);
            }
        }
        String[] typeSet = map.keySet().toArray(new String[0]);
        List<String> typeList= Arrays.asList(typeSet);
        Integer[] cntSet = map.values().toArray(new Integer[0]);
        List<Integer> cntList=Arrays.asList(cntSet);

        System.out.println(typeList.toString());
        System.out.println(cntList.toString());
        mmap.put("cTypeList", typeList);
        mmap.put("cCntList", cntList);
        return prefix + "/chart";
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
    public TableDataInfo printToHtml(FinanceWarehouse financeWarehouse)
    {
//        startPage();
        List<FinanceWarehouse> list = financeWarehouseServiceImpl.selectFinanceWarehouseListTocoll(financeWarehouse);
        return getDataTable(list);
    }

    @GetMapping("/addTransactionRecord")
    public String addTransactionRecord()
    {
        return prefix + "/addTransactionRecord";
    }

    /**
     * 新增交易记录
     */
    @RequiresPermissions("record:transaction_record:add")
    @Log(title = "交易记录", businessType = BusinessType.INSERT)
    @PostMapping("/addTransactionRecord")
    @ResponseBody
    public AjaxResult addTransactionRecordSave(TradingRecord tradingRecord) {
        return toAjax(tradingRecordService.insertTradingRecord(tradingRecord));
    }

    @PostMapping("/addCollection")
    @ResponseBody
    public AjaxResult addCollection(@RequestParam Long userId) {
        FinanceCollection financeCollection = new FinanceCollection();
        financeCollection.setUserId(userId);
        return toAjax(financeCollectionService.insertFinanceCollection(financeCollection));
    }

    /*查询账户信息列表
    @PostMapping("/accountist")
    @ResponseBody
    public TableDataInfo list( chargePile)
    {
        startPage();
        List<ChargePile> list = chargePileService.selectChargePileList(chargePile);
        return getDataTable(list);
    }*/
}
