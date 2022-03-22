package com.bcad.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MasterLogBean {

	static final Logger log = LoggerFactory.getLogger(MasterLogBean.class.getName());

	private String clientCode;
	private String clientName;
	private String strategyName;
	private String distName;
	private String rmName;
	private String tStatus;
	private String strategyStatus;
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getDistName() {
		return distName;
	}
	public void setDistName(String distName) {
		this.distName = distName;
	}
	public String getRmName() {
		return rmName;
	}
	public void setRmName(String rmName) {
		this.rmName = rmName;
	}
	public String gettStatus() {
		return tStatus;
	}
	public void settStatus(String tStatus) {
		this.tStatus = tStatus;
	}

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getStrategyStatus() {
        return strategyStatus;
    }

    public void setStrategyStatus(String strategyStatus) {
        this.strategyStatus = strategyStatus;
    }
}
