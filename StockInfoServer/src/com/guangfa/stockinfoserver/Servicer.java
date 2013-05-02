package com.guangfa.stockinfoserver;

import com.guangfa.stockinfoserver.model.Company;
import com.guangfa.stockinfoserver.model.Stock;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-25
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
public class Servicer implements Runnable {
    private final Logger logger = Logger.getLogger(AppServer.class);
    private Socket s;
    private int eventCode = 0;
    private String strWord;
    private JsonConfig config;

    public Servicer(Socket s) {
        this.s = s;
        config = new JsonConfig();
        config.setIgnoreDefaultExcludes(false);
        config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        config.setExcludes(new String[]{//只要设置这个数组，指定过滤哪些字段。
                "market",
                "openingPrice",
                "closingPrice",
                "lastClosingPrice",
                "highPrice",
                "lowPrice",
                "tradeVolume",
                "tradeMoney",
                "turnoverRate",
                "changePrice",
                "changeRatio",
                "name",
                "symbol"
        });

    }

    public void run() {
        try {
            s.setTcpNoDelay(true);
            InputStream ips = s.getInputStream();
            OutputStream ops = s.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(ips));
            DataOutputStream dos = new DataOutputStream(ops);
            BufferedOutputStream writer = new BufferedOutputStream(dos);
            while (true) {
                if (eventCode == 0) {
                    strWord = br.readLine();
                    if (StringUtils.isBlank(strWord)) continue;
                }
                //处理实时消息推送
                if (strWord.equalsIgnoreCase("request send data now!")) {
                    eventCode = 1;

                }
                //http发起获得某个股票信息
                if (strWord.indexOf("/stock/") != -1) {
                    eventCode = 2;
                    String array[] = strWord.split("\\/");
                    List<Stock> stockList = AllTaskResult.getInstance().getHistoryStockList(array[2]);
                    if (stockList != null && stockList.size() > 0) {
                        System.out.println(array[2] + " current size=" + stockList.size());
                        String json = JSONArray.fromObject(stockList, config).toString();
                        writer.write(json.getBytes("utf-8"));
                        writer.write("\r\n\r\n".getBytes("utf-8"));
                        writer.flush();

                    } else {
                        writer.write("[]".getBytes("utf-8"));
                        writer.write("\r\n\r\n".getBytes("utf-8"));
                        writer.flush();
                    }

                    strWord = br.readLine();
                    continue;

                }
                //http发起获得公司列表信息
                if (strWord.indexOf("/companyList") != -1) {
                    eventCode = 3;
                    Collection<Company> companyList = AllTaskResult.getInstance().getCompanyList();
                    if (companyList != null && companyList.size() > 0) {
                        System.out.println("companyList current size=" + companyList.size());
                        String json = JSONArray.fromObject(companyList).toString();
                        writer.write(json.getBytes("utf-8"));
                        writer.write("\r\n\r\n".getBytes("utf-8"));
                        writer.flush();

                    } else {
                        writer.write("[]".getBytes("utf-8"));
                        writer.write("\r\n\r\n".getBytes("utf-8"));
                        writer.flush();
                    }

                    strWord = br.readLine();
                    continue;

                }
                if (strWord.equalsIgnoreCase("quit!")) {
                    break;
                }

                if (eventCode == 1) {
                    ConcurrentHashMap<String, Stock> stockList = AllTaskResult.getInstance().getRealTimeStockMap();
                    if (stockList.size() > 0) {
                        String json = JSONArray.fromObject(stockList).toString();
                        dos.writeUTF(json);
                        dos.flush();
                        AllTaskResult.getInstance().clearRealTimeStockMap();
                        Thread.sleep(900);
                    }
                }

            }
            br.close();
            dos.close();
            writer.close();
            s.close();

        } catch (Exception e) {
            logger.error("socketServer write data to client occur error:",e);
        }
    }
}

