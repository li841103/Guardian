package com.szhd.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 卢文王
 * 闹钟
 *
 */
public class ClockBean implements Serializable{

	
	/**
	 * 闹钟时间
	 */
	private String TIME = "00:00";
	
	/**
	 * 星期
	 */
	private List<String> DATE = new ArrayList<String>();

	/**
	 * item的下标
	 */
	private int POSITION = 0;
	
	/**
	 * 操作标志：false为修改，true为添加
	 */
	private boolean OPERATIONFLAG = false;
	

	public int getPOSITION() {
		return POSITION;
	}


	public void setPOSITION(int pOSITION) {
		POSITION = pOSITION;
	}


	public boolean isOPERATIONFLAG() {
		return OPERATIONFLAG;
	}


	public void setOPERATIONFLAG(boolean oPERATIONFLAG) {
		OPERATIONFLAG = oPERATIONFLAG;
	}


	public String getTIME() {
		return TIME;
	}


	public void setTIME(String tIME) {
		TIME = tIME;
	}


	public List<String> getDATE() {
		return DATE;
	}


	public void setDATE(List<String> dATE) {
		DATE = dATE;
	}


	
	
}
