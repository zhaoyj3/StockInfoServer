package com.guangfa.stockinfoserver;

import com.asiainfo.dm.common.monitor.agent.AgentServer;
import com.asiainfo.dm.common.monitor.management.Manageable;
import com.guangfa.stockinfoserver.model.Company;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-25
 * Time: 上午11:39
 * To change this template use File | Settings | File Templates.
 */
public class AppServer implements Manageable {
    private final Logger logger = Logger.getLogger(AppServer.class);
    private ScheduledExecutorService ses;
    public static Map<String, Company> companysMap;
    public static Map<String, Task> tasksMap;
    private int listenPort = 9999;

    @Override
    public void init() {
        try {
            start();
            logger.info("StockInfoServer is successfully startup");
        } catch (Exception e) {
            logger.error("init() failure:", e);
            System.exit(0);
        }
    }

    public void initAll() throws Exception {
        try {
            AppConfig config = new AppConfig("stockInfoServer.xml");
            ses = config.initScheduledExecutorServer();
            companysMap = config.initCompanys();
            tasksMap = config.initTasks(companysMap);
            listenPort = config.getListenPort();
        } catch (Exception e) {
            logger.error("init stockInfoServer.xml error:", e);
        }
    }

    public void runTask() {
        final TimeUnit unit = TimeUnit.MILLISECONDS;
        for (Task task : tasksMap.values()) {
            ses.scheduleAtFixedRate(task, task.getNextScheduleDelay(),
                    task.getPeriod(), unit);
        }
    }

    public void start() throws Exception {
        try {
            initAll();
            runTask();
            new Thread(new SocketServer(this.listenPort)).start();
        } catch (BindException exception) {
            logger.error("the listenPort[" + this.listenPort + "] already in use!", exception);
            throw exception;
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public void destroy() {
        try {
            logger.info("StockInfoServer shutdown starting...");
            ses.shutdown();
            while (!ses.isTerminated()) {
            }
            logger.info("StockInfoServer is successfully shutdown");
        } catch (Exception e) {
            logger.error("shutdown occur error", e);
        }
    }


    public static void main(String[] args) throws Exception {
        AppServer server = new AppServer();
        runAsDaemon(server);
    }

    public static void runAsDaemon(AppServer am) throws Exception {
        AgentServer server = new AgentServer();
        server.setManageableInstance(am);
        server.startup();
    }


}
