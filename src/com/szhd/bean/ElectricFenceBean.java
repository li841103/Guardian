package com.szhd.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 卢文王
 * 电子围栏
 *
 */
public class ElectricFenceBean implements Serializable{

	/**
	 * 电子围栏范围
	 */
	private String RANGE = "1000";
	
	/**
	 * 时间段
	 */
	private String TIMEPARAGRAPH ="00:00-00:00";
	
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


	public String getTIMEPARAGRAPH() {
		return TIMEPARAGRAPH;
	}
	
	
	public void setTIMEPARAGRAPH(String tIMEPARAGRAPH) {
		TIMEPARAGRAPH = tIMEPARAGRAPH;
	}
	
	
	public boolean isOPERATIONFLAG() {
		return OPERATIONFLAG;
	}


	public void setOPERATIONFLAG(boolean oPERATIONFLAG) {
		OPERATIONFLAG = oPERATIONFLAG;
	}


	public int getPOSITION() {
		return POSITION;
	}


	public void setPOSITION(int pOSITION) {
		POSITION = pOSITION;
	}


	public String getRANGE() {
		return RANGE;
	}


	public void setRANGE(String rANGE) {
		RANGE = rANGE;
	}


	public List<String> getDATE() {
		return DATE;
	}


	public void setDATE(List<String> dATE) {
		DATE = dATE;
	}


	
}
