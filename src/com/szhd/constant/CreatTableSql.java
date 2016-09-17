package com.szhd.constant;

public class CreatTableSql {

	
	/**
	 * 补充申请审核表
	 */
	public static final String BD_APPLYAUDIT ="CREATE TABLE [bd_applyaudit] ("+
			  "[pk_applyaudit] INTEGER PRIMARY KEY AUTOINCREMENT, "+
			  "[ts] CHAR, "+
			  "[dr] CHAR, "+
			  "[remarks] CHAR, "+
			  "[nogun] CHAR, "+
			  "[nobullet] CHAR, "+
			  "[yesgun] CHAR, "+
			  "[yesbullet] CHAR, "+
			  "[def1] CHAR, "+
			  "[def2] CHAR, "+
			  "[def3] CHAR, "+
			  "[def4] CHAR, "+
			  "[def5] CHAR, "+
			  "[def6] CHAR, "+
			  "[def7] CHAR, "+
			  "[def8] CHAR, "+
			  "[def9] CHAR, "+
			  "[def10] CHAR);";
	
}
