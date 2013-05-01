package com.guangfa.stockinfoserver;

import com.guangfa.stockinfoserver.model.Company;
import com.guangfa.stockinfoserver.model.Stock;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-25
 * Time: 下午7:26
 * To change this template use File | Settings | File Templates.
 */
public class AllTaskResult {
    private ConcurrentHashMap<String, Stock> realTimeStockMap;
    public static AllTaskResult taskResult;

    public AllTaskResult() {
        realTimeStockMap = new ConcurrentHashMap<String, Stock>();
    }

    public void addRealTimeStockMap(Stock stock) {
        realTimeStockMap.put(stock.getCode(), stock);

    }

    public void clearRealTimeStockMap() {
        realTimeStockMap.clear();
    }

    public ConcurrentHashMap<String, Stock> getRealTimeStockMap() {
        return realTimeStockMap;
    }


    public List<Stock> getHistoryStockList(String stockCode) {
        Task task = AppServer.tasksMap.get(stockCode);
        if (task == null) {
            return null;
        }
        return task.getCurrentResult();
    }


    public Collection<Company> getCompanyList() {
        Map<String, Company> companysMap = AppServer.companysMap;
        return companysMap.values();
    }


    public static AllTaskResult getInstance() {
        if (taskResult == null) taskResult = new AllTaskResult();
        return taskResult;
    }

}
