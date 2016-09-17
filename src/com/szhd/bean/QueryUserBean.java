package com.szhd.bean;

public class QueryUserBean {
	private String def1;
	private int id;
	private String first_Guardian;
	private String phonenum;
	private String relationship;
	private String name;

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirst_Guardian() {
		return first_Guardian;
	}

	public void setFirst_Guardian(String first_Guardian) {
		this.first_Guardian = first_Guardian;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "QueryUserBean [def1=" + def1 + ", id=" + id
				+ ", first_Guardian=" + first_Guardian + ", phonenum="
				+ phonenum + ", relationship=" + relationship + ", name="
				+ name + "]";
	}

	public QueryUserBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QueryUserBean(String def1, int id, String first_Guardian,
			String phonenum, String relationship, String name) {
		super();
		this.def1 = def1;
		this.id = id;
		this.first_Guardian = first_Guardian;
		this.phonenum = phonenum;
		this.relationship = relationship;
		this.name = name;
	}

}
