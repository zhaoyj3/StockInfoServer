package com.guangfa.stockinfoserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import com.guangfa.stockinfoserver.model.Company;
import org.apache.log4j.Logger;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AppConfig {

    private final static Logger logger = Logger.getLogger(AppConfig.class);
    private ConfigVO config;

     private static class CompanyVO {
        public String name;
        public String code;
        public String symbol;
        public String market;
        public String desc;
    }

    private static class TaskVO {
        public String name;
        public String desc;
        public String company;
        public String timeUnit = "SECONDS";
        public int period = 0;
    }

    private static class GlobalVO {
        public int threadPoolSize = 5;
        public int listenPort=9999;
    }

    private static class ConfigVO {
        public GlobalVO global = new GlobalVO();
        public List<CompanyVO> companys = new ArrayList<CompanyVO>();
        public List<TaskVO> tasks = new ArrayList<TaskVO>();

    }

    public AppConfig(String filename) throws Exception {
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("company", CompanyVO.class);
        xstream.alias("task", TaskVO.class);
        xstream.alias("global", GlobalVO.class);
        xstream.alias("config", ConfigVO.class);

        config = (ConfigVO) xstream.fromXML(Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(filename));
        logger.trace("load configuration success:\n" + xstream.toXML(config));
    }


    public ScheduledExecutorService initScheduledExecutorServer() {
        ScheduledExecutorService ses = Executors
                .newScheduledThreadPool(config.global.threadPoolSize);
        return ses;
    }

    public Map<String, Company> initCompanys() {
        Map<String, Company> map = new HashMap<String,Company>();
        for (CompanyVO dsvo : config.companys) {
            Company company=null;
            try{
            company = new Company();
            company.setCode(dsvo.code);
            company.setName(dsvo.name);
            company.setSymbol(dsvo.symbol);
            company.setMarket(dsvo.market);
            company.setDesc(dsvo.desc);

            }catch(Exception e){
                  logger.error("create Company " + dsvo.name + " [" + dsvo.desc
                    + "] failed");
                e.printStackTrace();
            }
             Company com = map.get(dsvo.code);
            if (com == null) {
                map.put(dsvo.code,company);
            }

            logger.info("Company " + dsvo.name + " [" + dsvo.desc
                    + "] created");
        }
        return map;
    }

    public Map<String, Task> initTasks(Map<String, Company> companysMap) throws Exception {
        Map<String, Task> lt = new HashMap<String, Task>();
        for (TaskVO tvo : config.tasks) {
            Task task = new Task();
            task.setName(tvo.name);
            task.setCompanyCode(tvo.company);
            TimeUnit unit = TimeUnit.valueOf(tvo.timeUnit);
            task.setPeriod(tvo.period, unit);
            task.setCompanysMap(companysMap);

            lt.put(tvo.company, task);

            logger.info("Task " + tvo.name + " [" + tvo.desc + "] created");
        }
        return lt;
    }

    public int getListenPort(){
     return config.global.listenPort;
    }
}
