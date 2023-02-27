package com.fpts.web.controller.system;

import java.text.SimpleDateFormat;
import java.util.*;

import com.fpts.common.utils.poi.ExcelUtil;
import com.fpts.finance_warehouse.domain.FinanceWarehouse;
import com.fpts.todo.domain.TodoList;
import com.fpts.weathers.domain.WeatherStatistics;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fpts.common.annotation.Log;
import com.fpts.common.core.controller.BaseController;
import com.fpts.common.core.domain.AjaxResult;
import com.fpts.common.core.page.TableDataInfo;
import com.fpts.common.enums.BusinessType;
import com.fpts.system.domain.SysNotice;
import com.fpts.system.service.ISysNoticeService;

/**
 * 公告 信息操作处理
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {
    private String prefix = "system/notice";

    @Autowired
    private ISysNoticeService noticeService;

    @RequiresPermissions("system:notice:view")
    @GetMapping()
    public String notice() {
        return prefix + "/notice";
    }

    /**
     * 查询公告列表
     */
    @RequiresPermissions("system:notice:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 微信小程序查询公告列表
     */
    @RequestMapping(value = "/wxGet/{Navtab}", method = RequestMethod.POST)
    @ResponseBody
    public List<SysNotice> get(SysNotice notice, @PathVariable("Navtab") String tab) {
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        List<SysNotice> ansList = new ArrayList<>();
        for (SysNotice t : list) {
//            String detail = t.getDetail();
//            if (detail.contains("<p>")) {
//                t.setDetail(t.getDetail().replace("<p>", ""));
//            }
            if (tab.equals("0") && t.getStatus().equals("0")) { //0正常
                ansList.add(t);
            } else if (tab.equals("1") && t.getStatus().equals("1")) { //1关闭
                ansList.add(t);
            }
        }
        return ansList;
    }

    /**
     * 微信小程序查询指定的公告
     */
    @RequestMapping(value = "/wxView/{id}", method = RequestMethod.POST)
    @ResponseBody
    public SysNotice wxView(@PathVariable("id") String id) {
        SysNotice item = noticeService.selectNoticeById(Long.valueOf(id));
        return item;
    }

    /**
     * 统计报表
     */
    @RequestMapping("/chart")
    public String showChart(ModelMap mmap) {
        List<SysNotice> list = noticeService.selectNoticeList(new SysNotice());
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
        Map<String, Integer> map = new TreeMap<String, Integer>();
        for (SysNotice n : list) {
            Date tempDate = n.getCreateTime();
            String date = dateformat.format(tempDate);

            if (map.containsKey(date)) {
                int cnt = map.get(date);
                map.replace(date, cnt, cnt + 1);
            } else {
                map.put(date, 1);
            }
        }
        String[] dateSet = map.keySet().toArray(new String[0]);
        List<String> dateList = Arrays.asList(dateSet);
        Integer[] cntSet = map.values().toArray(new Integer[0]);
        List<Integer> cntList = Arrays.asList(cntSet);

        System.out.println(dateList.toString());
        System.out.println(cntList.toString());
        mmap.put("dateList", dateList);
        mmap.put("cntList", cntList);
        return prefix + "/chart";
    }

    /**
     * 导出公告
     */
    @Log(title = "通知公告", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:notice:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysNotice notice) {
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        ExcelUtil<SysNotice> util = new ExcelUtil<SysNotice>(SysNotice.class);
        return util.exportExcel(list, "通知公告");
    }

    /**
     * 打印公告
     */
    @PostMapping("/printToHtml")
    @ResponseBody
    public TableDataInfo printToHtml(SysNotice notice) {
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 打印跳转
     */
    @RequestMapping("/print")
    public String print() {
        return prefix + "/print";
    }


    /**
     * 新增公告
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存公告
     */
    @RequiresPermissions("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysNotice notice) {
        notice.setCreateBy(getLoginName());
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 查看公告
     */
    @RequiresPermissions("system:notice:view")
    @GetMapping("/view/{noticeId}")
    public String view(@PathVariable("noticeId") Long noticeId, ModelMap mmap) {
        mmap.put("notice", noticeService.selectNoticeById(noticeId));
        return prefix + "/view";
    }

    /**
     * 修改公告
     */
    @RequiresPermissions("system:notice:edit")
    @GetMapping("/edit/{noticeId}")
    public String edit(@PathVariable("noticeId") Long noticeId, ModelMap mmap) {
        mmap.put("notice", noticeService.selectNoticeById(noticeId));
        return prefix + "/edit";
    }

    /**
     * 修改保存公告
     */
    @RequiresPermissions("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysNotice notice) {
        notice.setUpdateBy(getLoginName());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除公告
     */
    @RequiresPermissions("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(noticeService.deleteNoticeByIds(ids));
    }
}
