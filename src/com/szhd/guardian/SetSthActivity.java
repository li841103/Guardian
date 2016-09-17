package com.szhd.guardian;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joooonho.SelectableRoundedImageView;
import com.szhd.ui.RoundImageView;
import com.szhd.util.ImageTools;
import com.szhd.util.MatchString;
import com.szhd.util.PreventContinuousClick;
import com.szhd.webservice.Communicate;

public class SetSthActivity extends Activity{

	private RoundImageView xcroundrectimageview;
	private EditText setsthname;
	private EditText belonging;
	private EditText setsthspecial;
	private EditText detailed;
	private EditText setsthphonenum;
	private Button button1;
	private ImageView button2;
	private TextView textView3;
	private String[] function = new String[]{"拍照","从相册选择"};
	private AlertDialog ad = null;
	private Boolean mark = false;
	private int position = -1;
	private String goods_name = null, belongpeople = null, goods_descript = null, img_descript_c = null, beguard_phonenum = null;
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int SCALE = 5;//照片缩小比例
	private int CHOISESATAE;//区分明显体征点击的（2）还是头像控件点击的（1）归属人点击为（0）
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setsth_activity);
		init();
		setOnClickListener();
	}


	private void init() {
		xcroundrectimageview = (RoundImageView) findViewById(R.id.xcroundrectimageview);
		setsthname = (EditText) findViewById(R.id.setsthname);
		belonging = (EditText) findViewById(R.id.belonging);
		setsthspecial = (EditText) findViewById(R.id.setsthspecial);
		detailed = (EditText) findViewById(R.id.detailed);
		setsthphonenum = (EditText) findViewById(R.id.setsthphonenum);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (ImageView) findViewById(R.id.button2);
		textView3 = (TextView) findViewById(R.id.textView3);
		Intent intent = getIntent(); //用于激活它的意图对象
		if(intent.getStringExtra("iconRes") != null)
			xcroundrectimageview.setImageResource(Integer.valueOf(intent.getStringExtra("iconRes")));
		if(intent.getStringExtra("sthname") != null)
			setsthname.setText(intent.getStringExtra("sthname"));
		if(intent.getStringExtra("belongname") != null)
			belonging.setText(intent.getStringExtra("belongname"));
		if(intent.getStringExtra("sthdetail") != null)
			detailed.setText(intent.getStringExtra("sthdetail"));
		if(intent.getStringExtra("mark") != null){
			//点击修改按钮跳转过来的
			mark = true;
			position = Integer.valueOf(intent.getStringExtra("mark"));
		}else{
			//点击增加跳转过来的
			mark = false;
			position = -1;
		}
	}
	
	
	private void setOnClickListener() {
		xcroundrectimageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(SetSthActivity.this)
				.setItems(function, new DialogInterface.OnClickListener(){  
					public void onClick(DialogInterface dialog, int which){
						switch (which) {
						case TAKE_PICTURE:
							CHOISESATAE = 1;
							Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
							//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
							openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
							startActivityForResult(openCameraIntent, TAKE_PICTURE);
							break;
						case CHOOSE_PICTURE:
							CHOISESATAE = 1;
							Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
							openAlbumIntent.setType("image/*");
							startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
							break;

						default:
							break;
						}
					}  
				}).show();
			}
		});
		
		belonging.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//防止连续点击，增强稳定性
				if (!PreventContinuousClick.isFastClick()) {
					if (event.getX() >= (belonging.getWidth() - belonging
						.getCompoundDrawables()[2].getBounds().width())){
						//判断是否已经弹出Dialog
						if(ad == null || !ad.isShowing()){
							ad = new AlertDialog.Builder(SetSthActivity.this)
							.setItems(function, new DialogInterface.OnClickListener(){  
								public void onClick(DialogInterface dialog, int which){  
									switch (which) {
									case TAKE_PICTURE:
										CHOISESATAE = 0;
										Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
										Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
										//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
										openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
										startActivityForResult(openCameraIntent, TAKE_PICTURE);
										break;
									case CHOOSE_PICTURE:
										CHOISESATAE = 0;
										Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
										openAlbumIntent.setType("image/*");
										startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
										break;
										
									default:
										break;
									}
								}  
							}).show();
						}
						belonging.clearFocus();
						belonging.setFocusable(false);
					}else{
						belonging.setFocusable(true);
						belonging.setFocusableInTouchMode(true);
						belonging.requestFocus();
						belonging.requestFocusFromTouch();
					}
				} 
				return false;
			}
		});
		
		setsthspecial.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//防止连续点击，增强稳定性
				if (!PreventContinuousClick.isFastClick()) {
					if (event.getX() >= (belonging.getWidth() - belonging
						.getCompoundDrawables()[2].getBounds().width())){
						//判断是否已经弹出Dialog
						if(ad == null || !ad.isShowing()){
							ad = new AlertDialog.Builder(SetSthActivity.this)
							.setItems(function, new DialogInterface.OnClickListener(){  
								public void onClick(DialogInterface dialog, int which){  
									switch (which) {
									case TAKE_PICTURE:
										CHOISESATAE = 2;
										Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
										Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
										//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
										openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
										startActivityForResult(openCameraIntent, TAKE_PICTURE);
										break;
									case CHOOSE_PICTURE:
										CHOISESATAE = 2;
										Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
										openAlbumIntent.setType("image/*");
										startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
										break;
										
									default:
										break;
									}
								}  
							}).show();
						}
						belonging.clearFocus();
						belonging.setFocusable(false);
					}else{
						belonging.setFocusable(true);
						belonging.setFocusableInTouchMode(true);
						belonging.requestFocus();
						belonging.requestFocusFromTouch();
					}
				} 
				return false;
			}
		});
		
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				intent.setClass(SetSthActivity.this, MainActivity.class);
				intent.putExtra("iconRes", String.valueOf(R.drawable.sth));
				
				goods_name = setsthname.getText().toString();
				belongpeople = belonging.getText().toString();
				goods_descript = setsthspecial.getText().toString();
				img_descript_c = detailed.getText().toString();
				beguard_phonenum = setsthphonenum.getText().toString();
				
	    		if(goods_name != null && belongpeople != null && goods_descript != null && img_descript_c != null
	    				&& beguard_phonenum != null){	
	    			//发送添加人信息
					String add = "goods|A|";
					JSONObject ja = new JSONObject();
					try {
						//物品照片
						ja.put("beguard_img_url", "//");
						//物品名称
						ja.put("goods_name", goods_name);
						//所有人
						ja.put("belongpeople", belongpeople);
						//所有人图片
						ja.put("be_peopleimg", "//");
						//明显特征
						ja.put("goods_descript", goods_descript);
						//明显体征图片(url加文件名)
						ja.put("img_url_c1", "//");
						ja.put("img_url_c2", "//");
						ja.put("img_url_c3", "//");
						ja.put("img_url_c4", "//");
						ja.put("img_url_c5", "//");
						//详细描述
						ja.put("img_descript_c", img_descript_c);
						//电话号码
						ja.put("beguard_phonenum", beguard_phonenum);
						//
						//ja.put("beguard_status", "");
						//设备ID
						ja.put("device_no", "1524");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					//设置要插入的数据
					List<String> s = new ArrayList<String>();
					s.add(add+ja.toString());
					//设置插入的标签
					List<String> starttags = new ArrayList<String>();
					starttags.add("<content>");
	    			//设置解析的标签
					Communicate.sss.add("<DealbeguardResult>");
					Communicate.sss.add("</DealbeguardResult>");
					//设置发送数据
					Communicate.xmlclassdata = MatchString.addString(SetSthActivity.this, "Dealbeguard.xml", s, starttags);
					Communicate cc = new Communicate();
		    	    cc.start();
					//开始发送
				    Communicate.mark = true;
	    			
				    intent.putExtra("name", setsthname.getText().toString());
		    		intent.putExtra("age", belonging.getText().toString());
		    		intent.putExtra("gender", detailed.getText().toString());
				    
				    if(mark == true){
				    	intent.putExtra("mark", String.valueOf(position));
				    }else{
				    	intent.putExtra("mark", "mark");
				    }
	    			startActivity(intent);
	    			finish();
	    		}else{
	    			Toast.makeText(SetSthActivity.this,
	    					"名字不能为空！",Toast.LENGTH_LONG).show();
	    		}
	    		
			}
		});
		
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		textView3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//更新intent
		setIntent(intent);
		//共享intent
		getIntent().putExtras(intent);
	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				//将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				//由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				switch(CHOISESATAE){
					case 0:
						
					break;
					case 1:
						//将处理过的图片显示在界面上，并保存到本地
						xcroundrectimageview.setImageBitmap(newBitmap);
					break;
					case 2:
						
					break;
				}
				ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				//照片的原始资源地址
				Uri originalUri = data.getData(); 
	            try {
	            	//使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
					if (photo != null) {
						//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
						//释放原始图片占用的内存，防止out of memory异常发生
						photo.recycle();
						switch(CHOISESATAE){
						case 0:
							
						break;
						case 1:
							xcroundrectimageview.setImageBitmap(smallBitmap);
						break;
						case 2:
							
						break;
					}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}  
				break;
			
			default:
				break;
			}
		}
	}
	
	
	
	
	
	
}
