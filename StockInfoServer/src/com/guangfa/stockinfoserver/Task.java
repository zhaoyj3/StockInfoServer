package com.guangfa.stockinfoserver;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.guangfa.stockinfoserver.model.Company;
import com.guangfa.stockinfoserver.model.Stock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

public class Task implements Runnable {
    private final static Logger logger = Logger.getLogger(Task.class);
    private final static TimeUnit timeUnit = TimeUnit.MILLISECONDS;
    private final static SimpleDateFormat dateFmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private String name = "";
    private long period = 0;
    private String companyCode;
    private Map<String, Company> companysMap;
    private List<Stock> stockList;

    public Task() {
        stockList = new Vector<Stock>();
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final long getPeriod() {
        return this.period;
    }

    public final String getCompanyCode() {
        return companyCode;
    }

    public final void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public final void setPeriod(long period, TimeUnit unit) {
        this.period = timeUnit.convert(period, unit);
    }


    public final void setCompanysMap(Map<String, Company> companysMap) {
        this.companysMap = companysMap;
    }

    public Company getCompany() {
        return (Company) companysMap.get(this.getCompanyCode());
    }

    public long getNextScheduleDelay() {
        final Date now = new Date(System.currentTimeMillis());
        final Date nextTime = getNextScheduleTime(now);
        return nextTime.getTime() - now.getTime();
    }

    public Date getNextScheduleTime(Date now) {
        final long tzOffset = TimeZone.getDefault().getRawOffset();
        final long t = now.getTime();
        long next = (t + tzOffset) / period * period - tzOffset;
        if (next < t) {
            next += period;
        }
        return new Date(next);
    }

    public Date getLastSchedulePeriod() {
        return getLastSchedulePeriod(new Date(System.currentTimeMillis()));
    }

    public Date getLastSchedulePeriod(Date now) {
        final long tzOffset = TimeZone.getDefault().getRawOffset();
        final long last = (now.getTime() + tzOffset - period) / period
                * period - tzOffset;
        return new Date(last);
    }


    public boolean isOpening() {
        return (stockList.size() == 0) ? true : false;
    }

    public Stock getLastStock() {
        int count = stockList.size();
        return stockList.get(count - 1);
    }

    public Stock getNewLastStock() throws Exception {
        return (Stock) BeanUtils.cloneBean(getLastStock());
    }

    public float getRandomPrice() {
        Random r1 = new Random(System.currentTimeMillis());
        return (float) (r1.nextInt(100) + Math.random());
    }

    public float getRandomOpt(float price, float offset) {
        Random r2 = new Random(System.currentTimeMillis());
        int temp = r2.nextInt(100);
        //50个数字的区间，50%的几率
        if (temp > 50) {
            return (price + offset);
        } else {
            return Math.abs(price - offset);
        }
    }

    public float getNextPrice(float price) {
        Random r1 = new Random(System.currentTimeMillis());
        int ran = (int) ((price * 0.1) < 1 ? 1 : price * 0.1);
        float offset = (float) (r1.nextInt(ran) + Math.random());
        offset = offset > (price * 0.1) ? (float) (price * 0.1) : offset;
        return getRandomOpt(price, offset);
    }

    public int getRandomTradeVolume() {
        Random r2 = new Random(System.currentTimeMillis());
        return r2.nextInt(10000);
    }

    public void runTask(final Date beginTime) {
        logger.info("Task[" + getName() + "] start");
        try {
            Company company = getCompany();
            Stock stock;
            if (isOpening()) {
                stock = new Stock();
                float openPrice = getRandomPrice();
                int openVolume = getRandomTradeVolume();
                stock.setOpeningPrice(openPrice);
                stock.setClosingPrice(openPrice);
                stock.setLastClosingPrice(openPrice);
                stock.setLatestPrice(openPrice);
                stock.setHighPrice(openPrice);
                stock.setLowPrice(openPrice);

                stock.setTradeVolume(openVolume);
                stock.setTradeMoney(openPrice * openVolume);
                stock.setTurnoverRate((float) Math.random());

                stock.setSymbol(company.getSymbol());
                stock.setName(company.getName());
                stock.setCode(company.getCode());
                stock.setMarket(company.getMarket());
            } else {
                stock = getNewLastStock();
                float newPrice = getNextPrice(stock.getLatestPrice());
                int newVolume = getRandomTradeVolume();
                if (newPrice > stock.getHighPrice()) {
                    stock.setHighPrice(newPrice);
                }
                if (newPrice < stock.getLowPrice()) {
                    stock.setLowPrice(newPrice);
                }
                stock.setLatestPrice(newPrice);
                stock.setClosingPrice(newPrice);
                stock.setLastClosingPrice(newPrice);

                stock.setTradeVolume(newVolume);
                stock.setTradeMoney(newPrice * newVolume);
                stock.setTurnoverRate((float) Math.random());

            }
            stock.setDate(dateFmt.format(beginTime));
            stockList.add((Stock) BeanUtils.cloneBean(stock));

            AllTaskResult.getInstance().addRealTimeStockMap(stock);
            //JSONArray jsonObject = JSONArray.fromObject(stockList);

            //System.out.println(jsonObject.toString());

            logger.info("Task[" + getName() + "] success");
        } catch (Exception e) {
            logger.error("Task[" + getName() + "] failure", e);
        }
    }

    public List<Stock> getCurrentResult() {
        return stockList;
    }

    @Override
    public void run() {
        runTask(getLastSchedulePeriod());
    }
}
