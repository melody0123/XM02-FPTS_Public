package com.fpts.finance_query.controller;

import java.lang.reflect.Array;
import java.util.*;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import com.fpts.assets.domain.AccountAssets;
import com.fpts.assets.mapper.AccountAssetsMapper;
import com.fpts.assets.service.IAccountAssetsService;
import com.fpts.finance_query.service.impl.FinanceQueryServiceImpl;
import com.fpts.record.domain.TradingRecord;
import com.fpts.record.service.ITradingRecordService;
import org.apache.ibatis.transaction.Transaction;
import com.fpts.common.utils.security.PermissionUtils;
import com.fpts.finance_collection.domain.FinanceCollection;
import com.fpts.finance_collection.service.IFinanceCollectionService;
import com.fpts.finance_warehouse.domain.FinanceWarehouse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.fpts.common.annotation.Log;
import com.fpts.common.enums.BusinessType;
import com.fpts.finance_query.domain.FinanceQuery;
import com.fpts.finance_query.service.IFinanceQueryService;
import com.fpts.common.core.controller.BaseController;
import com.fpts.common.core.domain.AjaxResult;
import com.fpts.common.utils.poi.ExcelUtil;
import com.fpts.common.core.page.TableDataInfo;
import org.springframework.ui.Model;


/**
 * 行情查询Controller
 * 
 * @author laybxc
 * @date 2022-11-25
 */
@Controller
@RequestMapping("/finance_query/finance_query")



public class FinanceQueryController extends BaseController
{
    private String prefix = "finance_query/finance_query";

    private FinanceCollection financeCollection;

    @Autowired
    private IFinanceQueryService financeQueryService;
    @Autowired
    private IFinanceCollectionService financeCollectionService;
    @Autowired
    private ITradingRecordService tradingRecordService;
    @Autowired
    private IAccountAssetsService accountAssetsService;

    @RequiresPermissions("finance_query:finance_query:view")
    @GetMapping()
    public String finance_query()
    {
        return prefix + "/finance_query";
    }

    /**
     * 查询行情查询列表
     */
    @RequiresPermissions("finance_query:finance_query:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FinanceQuery financeQuery)
    {
        startPage();
        List<FinanceQuery> list = financeQueryService.selectFinanceQueryList(financeQuery);
        return getDataTable(list);
    }

    /**
     * 导出行情查询列表
     */
    @RequiresPermissions("finance_query:finance_query:export")
    @Log(title = "行情查询", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FinanceQuery financeQuery)
    {
        List<FinanceQuery> list = financeQueryService.selectFinanceQueryList(financeQuery);
        ExcelUtil<FinanceQuery> util = new ExcelUtil<FinanceQuery>(FinanceQuery.class);
        return util.exportExcel(list, "行情查询数据");
    }

    /**
     * 新增行情查询
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存行情查询
     */
    @RequiresPermissions("finance_query:finance_query:add")
    @Log(title = "行情查询", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FinanceQuery financeQuery)
    {
        return toAjax(financeQueryService.insertFinanceQuery(financeQuery));
    }

    /**
     * 修改行情查询
     */
    @RequiresPermissions("finance_query:finance_query:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        FinanceQuery financeQuery = financeQueryService.selectFinanceQueryById(id);
        mmap.put("financeQuery", financeQuery);
        return prefix + "/edit";
    }

    /**
     * 修改保存行情查询
     */
    @RequiresPermissions("finance_query:finance_query:edit")
    @Log(title = "行情查询", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FinanceQuery financeQuery)
    {
        return toAjax(financeQueryService.updateFinanceQuery(financeQuery));
    }

    /**
     * 删除行情查询
     */
    @RequiresPermissions("finance_query:finance_query:remove")
    @Log(title = "行情查询", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(financeQueryService.deleteFinanceQueryByIds(ids));
    }

    /**
     * 新增产品收藏
     */
    @PostMapping("/collect/{product_id}")
    @ResponseBody
    public AjaxResult addCollection(@PathVariable("product_id") String productId) {
        long userId = (long) PermissionUtils.getPrincipalProperty("userId");
        String msg = "已经收藏过了";
        System.out.println(userId);
        System.out.println(productId);
        financeCollection = new FinanceCollection(userId, productId);
        if(financeCollectionService.selectFinanceCollectionByIdPid(financeCollection)==null)
            return toAjax(financeCollectionService.insertFinanceCollection(financeCollection));
        return error(msg);
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

    /**
     * K线图
     */
    @RequestMapping("/k_line_graph")
    public String showChart(){
        return prefix + "/k_line_graph";
    }

    /**
     * 统计报表
     */
    @RequestMapping("/chart")

    public String showChart(ModelMap mmap){
        List<FinanceQuery> list = financeQueryService.selectFinanceQueryList(new FinanceQuery());
        Map<String, Double> map = new TreeMap<String, Double>();

        for(FinanceQuery f: list){
            String pId = f.getName();
            Double rate = f.getIncrease();
            //改这里，找涨幅最高的十只股票
            map.put(pId, rate);

        }

        List<Map.Entry<String, Double>> list1= new ArrayList<Map.Entry<String, Double>>(map.entrySet());
        Collections.sort(list1,new Comparator<Map.Entry<String, Double>>() {
            //降序排序
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        List<String> pIdList = new ArrayList<String>();   //= Arrays.asList(pIdSet);
        List<Double> cntList = new ArrayList<Double>();
        int tot=0;

        for (Map.Entry<String, Double> a: list1) {
            tot++;
            if(tot==10) break;
            pIdList.add(a.getKey());
            cntList.add(a.getValue());
        }

        System.out.println(pIdList.toString());
        System.out.println(cntList.toString());
        mmap.put("pIdList", pIdList);
        mmap.put("cntList", cntList);
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
    public TableDataInfo printToHtml(FinanceQuery financeQuery)
    {
//        startPage();
        List<FinanceQuery> list = financeQueryService.selectFinanceQueryList(financeQuery);
        return getDataTable(list);
    }

    /** 小程序列表 */
    @RequestMapping(value = "/wxGet/{Navtab}", method = RequestMethod.POST)
    @ResponseBody
    public List<FinanceQuery> get(FinanceQuery financeQuery, @PathVariable("Navtab") String tab){
        List<FinanceQuery> list = financeQueryService.selectFinanceQueryList(financeQuery);
        List<FinanceQuery> ansList = new ArrayList<>();
        int cnt = 0;
        for(FinanceQuery t: list){
            if (cnt>500) break;
            if(tab.equals("0") && t.getType().equals("0")){//A股
                ansList.add(t);
                cnt++;
            }
            else if(tab.equals("1") && t.getType().equals("1")){//B股
                ansList.add(t);
                cnt++;
            }
            else if(tab.equals("2") && t.getType().equals("2")){//债券
                ansList.add(t);
                cnt++;
            }
            else if(tab.equals("3") && t.getType().equals("3")){//基金
                ansList.add(t);
                cnt++;
            }
        }
        System.out.println(ansList.toString());
        return ansList;
    }

    @RequestMapping(value = "/wxEdit/{id}", method = RequestMethod.POST)
    @ResponseBody
    public FinanceQuery wxEdit( @PathVariable("id") String id){
        FinanceQuery item = financeQueryService.selectFinanceQueryById(Integer.valueOf(id));
        return item;
    }

}
