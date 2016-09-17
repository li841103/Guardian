package com.szhd.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author 卢文王
 * 
 * @time 2016.08.29
 * 
 * @description 图片的上传下载
 * 
 */
public class ImgHttpClient {

	private static String uploadUrl = "http://192.168.1.200:9009/Upload/UploadFile/";// Post请求地址
	private static String downUrl = "http://192.168.1.200:9009/Upload/DownFile/";// Post请求地址
	private static String updateVersionUrl = "http://192.168.1.200:9009/Upload/UpdateVersion/";// Post请求地址
	private static String queryAllUserUrl = "http://192.168.1.200:9009/Upload/QueryAllUser/";// Post请求地址
	private static String queryAllUserInfo = "http://192.168.1.200:9009/Upload/GetUserInfo/";// Post请求地址
	private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";// Post请求需要拼接的头地址

	private Context ctx;

	public ImgHttpClient(Context ctx) {
		this.ctx = ctx;
	}

	/**
	 * 
	 * @param params
	 *            传递的普通参数
	 * @param uploadFile
	 *            需要上传的文件名
	 * @param fileFormName
	 *            需要上传文件表单中的名字
	 * @param newFileName
	 *            上传的文件名称，不填写将为uploadFile的名称
	 * @param urlStr
	 *            上传的服务器的路径
	 * @throws IOException
	 */
	public String uploadForm(Map<String, String> params, String fileFormName,
			File uploadFile, String newFileName) throws IOException {
		if (newFileName == null || newFileName.trim().equals("")) {
			newFileName = uploadFile.getName();
		}

		StringBuilder sb = new StringBuilder();
		/**
		 * 普通的表单数据
		 */
		if (params != null)
			for (String key : params.keySet()) {
				sb.append("--" + BOUNDARY + "\r\n");
				sb.append("Content-Disposition: form-data; name=\"" + key
						+ "\"" + "\r\n");
				sb.append("\r\n");
				sb.append(params.get(key) + "\r\n");
			}
		/**
		 * 上传文件的头
		 */
		sb.append("--" + BOUNDARY + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"" + fileFormName
				+ "\"; filename=\"" + newFileName + "\"" + "\r\n");
		sb.append("Content-Type: image/jpeg" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
		sb.append("\r\n");

		byte[] headerInfo = sb.toString().getBytes("UTF-8");
		byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
		System.out.println(sb.toString());
		URL url = new URL(uploadUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + BOUNDARY);
		conn.setRequestProperty(
				"Content-Length",
				String.valueOf(headerInfo.length + uploadFile.length()
						+ endInfo.length));
		conn.setDoOutput(true);

		OutputStream out = conn.getOutputStream();
		InputStream in = new FileInputStream(uploadFile);
		out.write(headerInfo);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1)
			out.write(buf, 0, len);

		out.write(endInfo);
		in.close();
		out.close();

		if (conn.getResponseCode() == 200) {
			Log.e("main", "上传成功");

		}
		return null;
	}

	/**
	 * 
	 * @param params
	 *            传递的普通参数
	 * @param newFileName
	 *            下载到本地保存的文件名称
	 * @throws IOException
	 */
	public int downForm(String newFileName) throws IOException {

		byte[] headerInfo = ("filename=" + newFileName).getBytes("UTF-8");
		URL url = new URL(downUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(headerInfo.length));
		conn.setDoOutput(true);
		conn.setDoInput(true);

		OutputStream out = conn.getOutputStream();
		out.write(headerInfo);
		out.close();

		if (conn.getResponseCode() != 200) {
			return 0;
		}
		if (conn.getResponseCode() == 200) {
			File file = new File(ctx.getExternalCacheDir(), newFileName);
			FileOutputStream os = new FileOutputStream(file);

			Log.e("main", "下载成功");
			// 调用下载图片的方法
			InputStream is = conn.getInputStream();
			byte[] buffer = new byte[1024 * 4];
			int n_rx = 0;
			while ((n_rx = is.read(buffer)) > 0) {
				os.write(buffer, 0, n_rx);
			}
			os.flush();
			os.close();
			return 1;// 成功
		}
		return 0;

	}

	/**
	 * 
	 * @param params
	 *            传递的普通参数
	 * @param newFileName
	 *            下载到本地保存的文件名称 例 version_1.0.apk
	 * @throws IOException
	 */
	public int UpdateVersion(String newFileName) throws IOException {
		Log.e("main", "newVersion=" + newFileName);
		byte[] headerInfo = ("version=" + newFileName).getBytes("UTF-8");
		URL url = new URL(updateVersionUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(headerInfo.length));
		conn.setDoOutput(true);
		conn.setDoInput(true);

		OutputStream out = conn.getOutputStream();
		out.write(headerInfo);
		out.close();
		File dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File file = new File(dir, newFileName);
		FileOutputStream os = new FileOutputStream(file);
		if (conn.getResponseCode() == 200) {
			Log.e("main", "下载成功");
			// 下载APK
			InputStream is = conn.getInputStream();
			byte[] buffer = new byte[1024 * 4];
			int n_rx = 0;
			while ((n_rx = is.read(buffer)) > 0) {
				os.write(buffer, 0, n_rx);
			}
			os.flush();
			os.close();
			return 1;// 成功
		}
		return 0;

	}

	/**
	 * 
	 * @param params
	 *            传递的普通参数
	 * @param uploadFile
	 *            需要上传的文件名
	 * @param fileFormName
	 *            需要上传文件表单中的名字
	 * @param newFileName
	 *            上传的文件名称，不填写将为uploadFile的名称
	 * @param urlStr
	 *            上传的服务器的路径
	 * @throws IOException
	 */
	public void queryAllUser(Map<String, String> params, String fileFormName,
			File uploadFile, String newFileName) throws IOException {
		if (newFileName == null || newFileName.trim().equals("")) {
			newFileName = uploadFile.getName();
		}

		StringBuilder sb = new StringBuilder();
		/**
		 * 普通的表单数据
		 */
		if (params != null)
			for (String key : params.keySet()) {
				sb.append("--" + BOUNDARY + "\r\n");
				sb.append("Content-Disposition: form-data; name=\"" + key
						+ "\"" + "\r\n");
				sb.append("\r\n");
				sb.append(params.get(key) + "\r\n");
			}
		/**
		 * 上传文件的头
		 */
		sb.append("--" + BOUNDARY + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"" + fileFormName
				+ "\"; filename=\"" + newFileName + "\"" + "\r\n");
		sb.append("Content-Type: image/jpeg" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
		sb.append("\r\n");

		byte[] headerInfo = sb.toString().getBytes("UTF-8");
		byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
		System.out.println(sb.toString());
		URL url = new URL(queryAllUserUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + BOUNDARY);
		conn.setRequestProperty(
				"Content-Length",
				String.valueOf(headerInfo.length + uploadFile.length()
						+ endInfo.length));
		conn.setDoOutput(true);

		OutputStream out = conn.getOutputStream();
		InputStream in = new FileInputStream(uploadFile);
		out.write(headerInfo);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1)
			out.write(buf, 0, len);

		out.write(endInfo);
		in.close();
		out.close();

		if (conn.getResponseCode() == 200) {
			Log.e("main", "上传成功");

		}
	}

	/**
	 * 
	 * @param params
	 *            传递的普通参数
	 * @param newFileName
	 *            查询所有被监护人的信息
	 * @throws IOException
	 */
	public void queryAllUserInfo(Map<String, String> params,
			String fileFormName, File uploadFile, String newFileName)
			throws IOException {
		if (newFileName == null || newFileName.trim().equals("")) {
			newFileName = uploadFile.getName();
		}

		StringBuilder sb = new StringBuilder();
		/**
		 * 普通的表单数据
		 */
		if (params != null)
			for (String key : params.keySet()) {
				sb.append("--" + BOUNDARY + "\r\n");
				sb.append("Content-Disposition: form-data; name=\"" + key
						+ "\"" + "\r\n");
				sb.append("\r\n");
				sb.append(params.get(key) + "\r\n");
			}
		/**
		 * 上传文件的头
		 */
		sb.append("--" + BOUNDARY + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"" + fileFormName
				+ "\"; filename=\"" + newFileName + "\"" + "\r\n");
		sb.append("Content-Type: image/jpeg" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
		sb.append("\r\n");

		byte[] headerInfo = sb.toString().getBytes("UTF-8");
		byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
		System.out.println(sb.toString());
		URL url = new URL(queryAllUserUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + BOUNDARY);
		conn.setRequestProperty(
				"Content-Length",
				String.valueOf(headerInfo.length + uploadFile.length()
						+ endInfo.length));
		conn.setDoOutput(true);

		OutputStream out = conn.getOutputStream();
		InputStream in = new FileInputStream(uploadFile);
		out.write(headerInfo);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1)
			out.write(buf, 0, len);

		out.write(endInfo);
		in.close();
		out.close();

		if (conn.getResponseCode() == 200) {
			Log.e("main", "上传成功");

		}
	}

}
