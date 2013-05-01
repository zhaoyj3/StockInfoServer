package com.guangfa.stockinfoserver.model;

public class Stock {

	/** 股票代码 */
    private String code;
    /** 股票名称 */
    private String name;
    /** 股票简写 */
    private String symbol;
    /** 交易市场：sh-上海;sz-深圳 */
    private String market;

    /** 最新价格 */
    private float latestPrice;

    /** 开盘价 */
    private float openingPrice;
    /** 收盘价 */
    private float closingPrice;
    /** 前收盘价 */
    private float lastClosingPrice;

    /** 最高价 */
    private float highPrice;
    /** 最低价 */
    private float lowPrice;

    /** 成交量（单位：股数） */
    private int tradeVolume;
    /** 成交金额（单位：元） */
    private float tradeMoney;

    /** 换手率（单位：%） */
    private float turnoverRate;
    /** 日期格式：01/29/2008 15:05:32 */
    private String date;


    /** 涨跌额 */
    public float getChangePrice() {
    	return latestPrice - lastClosingPrice;
    }
    /** 涨跌幅（单位：%） */
    public float getChangeRatio() {
    	return 100 * getChangePrice() / lastClosingPrice;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMarket() {
		return (market.equals("sh") ? "上海" : "深圳");
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public float getOpeningPrice() {
		return openingPrice;
	}

	public void setOpeningPrice(float openingPrice) {
		this.openingPrice = openingPrice;
	}

	public float getClosingPrice() {
		return closingPrice;
	}

	public void setClosingPrice(float closingPrice) {
		this.closingPrice = closingPrice;
	}

	public float getLastClosingPrice() {
		return lastClosingPrice;
	}

	public void setLastClosingPrice(float lastClosingPrice) {
		this.lastClosingPrice = lastClosingPrice;
	}

	public float getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(float highPrice) {
		this.highPrice = highPrice;
	}

	public float getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(float lowPrice) {
		this.lowPrice = lowPrice;
	}

	public int getTradeVolume() {
		return tradeVolume;
	}

	public void setTradeVolume(int tradeVolume) {
		this.tradeVolume = tradeVolume;
	}

	public float getTradeMoney() {
		return tradeMoney;
	}

	public void setTradeMoney(float tradeMoney) {
		this.tradeMoney = tradeMoney;
	}

	public float getTurnoverRate() {
		return turnoverRate;
	}

	public void setTurnoverRate(float turnoverRate) {
		this.turnoverRate = turnoverRate;
	}
	public float getLatestPrice() {
		return latestPrice;
	}
	public void setLatestPrice(float latestPrice) {
		this.latestPrice = latestPrice;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
