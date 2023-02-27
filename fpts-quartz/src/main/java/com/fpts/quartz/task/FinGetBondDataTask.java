package com.fpts.quartz.task;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fpts.finance_warehouse.domain.FinanceWarehouse;
import com.fpts.finance_warehouse.service.impl.FinanceWarehouseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("finGetBondDataTask")
//上面是重要引入，将这个Task类声明为bean，因为调用的那些service都是用bean注入的，反射的调用方式是没办法在非bean的对象里面注入bean的

public class FinGetBondDataTask {

    @Autowired
    FinanceWarehouseServiceImpl financeWarehouseService;

    public void GetData(){

        List<String> samples = new ArrayList<>();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String time=dateFormat.format(new Date());
        System.out.println(time);
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject("", String.class);

        JSONArray array = JSONArray.parseArray(forObject);

        FinanceWarehouse bond = new FinanceWarehouse();
        List<FinanceWarehouse> stocks = new ArrayList<>();
        for(int i=0;i<array.size();i++){
            JSONObject object=(JSONObject)array.get(i);
            if(object.getString("symbol")!=null){
                String product_id =  object.getString("symbol");
                bond.setProductId(product_id);
                //System.out.println(product_id);
            }
            if(object.getString("name")!=null){
                String name =  object.getString("name"); //key参数为string
                bond.setName(name);
            }
            bond.setType("2");
            if(object.getString("buy")!=null){
                double new_price=Double.valueOf(object.getString("buy"));
                bond.setNewPrice(new_price);
            }
            if(object.getString("open")!=null){
                double open_price=Double.valueOf(object.getString("open"));
                bond.setOpenPrice(open_price);
            }
            if(object.getString("settlement")!=null){
                double yesterday_price=Double.valueOf(object.getString("settlement"));
                bond.setYesterdayPrice(yesterday_price);
            }
            if(object.getString("high")!=null){
                double max_price=Double.valueOf(object.getString("high"));
                bond.setMaxPrice(max_price);
            }
            if(object.getString("low")!=null){
                double min_price=Double.valueOf(object.getString("low"));
                bond.setMinPrice(min_price);
            }
            if(object.getString("changepercent")!=null){
                double increase=Double.valueOf(object.getString("changepercent"));
                bond.setIncrease(increase);
            }
            /*
            if(object.getString("涨速")!=null){
                double increase_rate=Double.valueOf(object.getString("涨速"));
                bond.setIncreaseRate(increase_rate);
            }

            if(object.getString("换手率")!=null){
                double turnover_rate=Double.valueOf(object.getString("换手率"));
                bond.setTurnoverRate(turnover_rate);
            }*/
//            System.out.println(stock.toString());
//            System.out.println(financeWarehouseService.toString());
            if(null!=bond){
                FinanceWarehouse f = financeWarehouseService.selectFinanceWarehouseByProductId(bond.getProductId());
                if(null==f) financeWarehouseService.insertFinanceWarehouse(bond);
                else financeWarehouseService.updateFinanceWarehouse2(bond);
            }
            //stocks.add(stock);

        }

    }
}
