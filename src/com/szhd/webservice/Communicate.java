package com.szhd.webservice;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.szhd.util.MatchString;

public class Communicate extends Thread {

	public static String IMSI;
	public static boolean mark = false;
	public static List<String> sss = new ArrayList<String>();
	public static String xmlclassdata;
	public static List<String> RESULT;
	public static boolean STATE = false;

	@Override
	public void run() {
		try {
			while (true) {
				if (mark) {
					RESULT = getRealIp();
					mark = false;
					sss.clear();
					STATE = true;
					System.out.println("" + STATE);
				}
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getRealIp() throws Exception {
		List<String> vlist = new ArrayList<String>();
		byte[] entity = xmlclassdata.getBytes();

		HttpURLConnection con = (HttpURLConnection) new URL(
				"http://114.55.219.115:13118/Communicate.asmx")
				.openConnection();

		// HttpURLConnection con = (HttpURLConnection) new URL(
		// "http://localhost:13118/Communicate.asmx").openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setConnectTimeout(500);
		con.setRequestProperty("Content-Type",
				"application/soap+xml; charset=utf-8");
		con.setRequestProperty("Content-Length", String.valueOf(entity.length));
		con.getOutputStream().write(entity);
		if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
			vlist = getByteArrayFromInputStream(con.getInputStream());
			return vlist;
		} else {
			vlist.add(String.valueOf(con.getResponseCode()));
			return vlist;
		}

	}

	public static List<String> getByteArrayFromInputStream(InputStream ism)
			throws Exception {
		return MatchString.matchAllIntex(ism, sss);
	}

}
