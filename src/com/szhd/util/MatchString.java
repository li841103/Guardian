package com.szhd.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class MatchString {

	public static List<String> matchAllIntex(InputStream ism, List<String> tag) {
		List<String> rlist = new ArrayList<String>();
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = ism.read(buffer)) > -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			for (int i = 0; i < (tag.size()) / 2; i++) {
				InputStream stream = new ByteArrayInputStream(
						baos.toByteArray());
				List<String> tlist = new ArrayList<String>();
				tlist.add(tag.get(i * 2));
				tlist.add(tag.get((i * 2) + 1));
				for (String s : matchIntex(stream, tlist)) {
					rlist.add(s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rlist;
	}

	private static List<String> matchIntex(InputStream ism, List<String> tag) {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		try {
			for (int n; (n = ism.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Integer> l = new ArrayList<Integer>();
		List<Integer> r = new ArrayList<Integer>();
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < out.length() - tag.get(j).length(); i++) {
				if (out.substring(i, tag.get(j).length() + i)
						.equals(tag.get(j))) {
					if (j == 0) {
						l.add(i);
					} else if (j == 1) {
						r.add(i);
					}
				}
			}
		}

		List<String> v = new ArrayList<String>();
		for (int k = 0; k < l.size(); k++) {
			v.add(out.substring(l.get(k) + tag.get(0).length(), r.get(k)));
		}

		return v;
	}

	public static String addString(Context ctx, String path, List<String> s,
			List<String> starttags) {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		try {
			InputStream ism = ctx.getAssets().open(path);
			for (int n; (n = ism.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (s != null && starttags != null) {
			for (String tag : starttags) {
				int index = -1;
				for (int i = 0; i < out.length() - tag.length(); i++) {
					if (out.substring(i, tag.length() + i).equals(tag)) {
						index = i;
					}
				}
				out.insert(index + tag.length(), s.get(starttags.indexOf(tag)));
			}
		}
		return out.toString();
	}

	public static String addInt(Context ctx, String path, List<Integer> s,
			List<String> starttags) {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		try {
			InputStream ism = ctx.getAssets().open(path);
			for (int n; (n = ism.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (s != null && starttags != null) {
			for (String tag : starttags) {
				int index = -1;
				for (int i = 0; i < out.length() - tag.length(); i++) {
					if (out.substring(i, tag.length() + i).equals(tag)) {
						index = i;
					}
				}
				out.insert(index + tag.length(), s.get(starttags.indexOf(tag)));
			}
		}
		return out.toString();
	}

}
