package com.szhd.bean;

import com.szhd.guardian.SlideView;

public class UserBean {
	public SlideView sv;
	private String iConUrl;
	private String name;
	private int age;
	private String sex;
	private int height;
	private int weight;
	private String device;
	private String address;
	public boolean mesmark;

	public UserBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserBean(SlideView sv, String iConUrl, String name, int age,
			String sex, int height, int weight, String device, String address,
			boolean mesmark) {
		super();
		this.sv = sv;
		this.iConUrl = iConUrl;
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.height = height;
		this.weight = weight;
		this.device = device;
		this.address = address;
		this.mesmark = mesmark;
	}

	@Override
	public String toString() {
		return "UserBean [sv=" + sv + ", iConUrl=" + iConUrl + ", name=" + name
				+ ", age=" + age + ", sex=" + sex + ", height=" + height
				+ ", weight=" + weight + ", device=" + device + ", address="
				+ address + ", mesmark=" + mesmark + "]";
	}

	public SlideView getSv() {
		return sv;
	}

	public void setSv(SlideView sv) {
		this.sv = sv;
	}

	public String getiConUrl() {
		return iConUrl;
	}

	public void setiConUrl(String iConUrl) {
		this.iConUrl = iConUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isMesmark() {
		return mesmark;
	}

	public void setMesmark(boolean mesmark) {
		this.mesmark = mesmark;
	}

}
