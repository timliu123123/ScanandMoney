/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 * 
 * Created by lisah0 on 2012-02-24
 */
package com.zbar.lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import com.zbar.lib.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CameraCheckAboutActivity extends Activity {

	long 	send_sin;
	//long	MoneyOrPaper;
	String  send_sid;
	String 	send_key;



	TranslateAnimation mAnimation;
	
	String myResult = null;

	Button checknewButton;
	Button AboutButton;
	Button ExitButton;
	
	String versionname;
	String url;
	long   appcode;
	long   size;	
	
	private ProgressDialog pBar;
	
	long updateCode;
	
	private Handler handler = new Handler();
	
	protected void showProgressBar() {
		// TODO Auto-generated method stub
		pBar = new ProgressDialog(CameraCheckAboutActivity.this);
		pBar.setTitle("软件升级");
		pBar.setMessage("软件正在更新，请保持网络畅通...");
		pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		downAppFile(url);
	}
	
	protected void downAppFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					Log.isLoggable("DownTag", (int) length);
					
					Log.d("tttttt", "len = " + (int) length);
					
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is == null) {
						throw new RuntimeException("isStream is null");
					}
					
					Log.d("tttttt", "111111");
					
					long total = 0;
					
					File file = new File(
							Environment.getExternalStorageDirectory(), "hexiao_1.2_android.apk");
					
					Log.d("tttttt", "222222");
					fileOutputStream = new FileOutputStream(file);
					
					Log.d("tttttt", "33333");
					
					byte[] buf = new byte[1024];
					int ch = -1;
					do {
						ch = is.read(buf);
						if (ch <= 0)
							break;
						total = total + ch;
						Log.d("tttttt", "total = " + (int)length + "download = " + total);
						fileOutputStream.write(buf, 0, ch);
					} while (true);
					is.close();
					fileOutputStream.close();
					haveDownLoad();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	// cancel progressBar and start new App
	protected void haveDownLoad() {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				//
				Dialog installDialog = new AlertDialog.Builder(
						CameraCheckAboutActivity.this)
						.setTitle("软件安装")
						.setMessage("正在安装下载的软件")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										installNewApk();
										finish();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										finish();
									}
								}).create();
				installDialog.show();
			}
		});
	}


	protected void installNewApk() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "hexiao_1.2_android.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
	
	
	private OnClickListener checknewListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Thread UpdateApk = new Thread(new UpdateHandler());
			UpdateApk.start();
		}
	};
	

	private long validateCertificate() {

	
	        String mybook = "{\"body\":{\"selleruin\":\"10029\",\"couponid\":\"0025199992554\",\"oprshopid\":\"78\",\"oprshopname\":\"sfsdfs\",\"oprshopclerkname\":\"\"},\"reqhead\":{\"sdkversion\":\"17\",\"cmd\":\"app_checkversion\",\"cpuserial\":\"4b9760af4d0012c2\",\"devicename\":\"Lenovot-A789\",\"guid\":\"AEDFE29A52AA44EB9A0CDEA6157B7BD9\",\"mac\":\"40:F3:08:F4:6C:84\",\"imei\":\"358851054502087\",\"imsi\":\"460018828603788\",\"uin\":3,\"nettype\":3,\"ostype\":1,\"appcode\":1,\"seq\":3,\"height\":1920,\"width\":1080}}";
			
			HttpPost httpRequest2 =new HttpPost(Util.mainUrl);

			mybook = mybook.replaceAll("\"uin\":3", "\"uin\":" + send_sin);
			mybook = Util.changeMainString(this, mybook);
			
			Log.d("tttttt", "source = " + mybook);
			
			HttpResponse httpResponse2 = null;
			try{
				httpRequest2.setHeader("WeShop-Version", "1.0");
				httpRequest2.setHeader("WeShop-Sid", send_sid);
			
				byte[] bs2 = mybook.getBytes();
			
				byte[] postData2 =SecureUtil.encodeWithUkey(bs2,send_key);
			
				httpRequest2.setEntity(new ByteArrayEntity(postData2));
				//HTTP response
			

				httpResponse2=new DefaultHttpClient().execute(httpRequest2);
			
				//200 ok

				if(httpResponse2.getStatusLine().getStatusCode()==200){
					byte bsret2[] = EntityUtils.toByteArray(httpResponse2.getEntity());

					byte[] convertresult2 = SecureUtil.decodeWithUkey(bsret2, send_key); 
					
					String jsonStr2 = new String(convertresult2, "UTF-8");
					Log.d("tttttt", "hexiao = " + jsonStr2);
					
					JSONObject jsonObject = new JSONObject(jsonStr2);  
					Log.d("tttttt", "hexiao = " + "111111");
					String mybody = jsonObject.getString("body");
					Log.d("tttttt", "hexiao = " + "aaaaaa");
					jsonObject = new JSONObject(mybody);
					
					
					Log.d("tttttt", "hexiao = " + "333333");
					updateCode = jsonObject.getLong("update");
					Log.d("tttttt", "hexiao = " + "444444");
					
					String version_info = jsonObject.getString("versioninfo");
					
					Log.d("tttttt", "hexiao = " + "222222");
					
					jsonObject = new JSONObject(version_info);
					Log.d("tttttt", "hexiao = " + "55555");
					versionname = jsonObject.getString("versionname");
					Log.d("tttttt", "hexiao = " + "66666");
					url = jsonObject.getString("url");
					Log.d("tttttt", "hexiao = " + "77777");
					appcode = jsonObject.getLong("appcode");
					Log.d("tttttt", "hexiao = " + "88888");
					size = jsonObject.getLong("size");
					
					Log.d("tttttt", "updateCode = " + updateCode  + "versionname = " + versionname);
					Log.d("tttttt", "url = " + url  + "appcode = " + appcode + "size = " + size);
		
					return updateCode;
				}
			}
			
			catch(Exception e){
				e.printStackTrace();
				updateCode = -1;
			}

		

		

		return updateCode;
	}

	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			long isNetError = msg.getData().getLong("isNetError");

			if (isNetError == -1) {
				Toast.makeText(CameraCheckAboutActivity.this, "通讯错误",
						Toast.LENGTH_SHORT).show();
			}

			if (isNetError == 1) {
				Toast.makeText(CameraCheckAboutActivity.this, "升级中，请等候......",
						Toast.LENGTH_SHORT).show();
				showProgressBar() ;
			}
			
			if (isNetError == 0) {
				Toast.makeText(CameraCheckAboutActivity.this, "无最新版本",
						Toast.LENGTH_SHORT).show();
			}
			
			
			

		}
	};

	class UpdateHandler implements Runnable {
		@Override
		public void run() {


			long loginState = validateCertificate();
			Log.d("tttttt", "validateLogin");

			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putLong("isNetError", loginState);
			message.setData(bundle);
			loginHandler.sendMessage(message);


		}

	}
	
	private OnClickListener aboutListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(CameraCheckAboutActivity.this, AboutActivity.class);
			startActivityForResult(intent, 0);
		}
	};

	private OnClickListener exitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Util.scryDP < 600)
			setContentView(R.layout.zbar_checkabout_small);
		else	
			setContentView(R.layout.zbar_checkabout);

		checknewButton = (Button)findViewById(R.id.checknewbutton);
		checknewButton.setOnClickListener(checknewListener);
		
		
		AboutButton = (Button)findViewById(R.id.aboutbutton);
		AboutButton.setOnClickListener(aboutListener);	

		ExitButton = (Button)findViewById(R.id.exitbutton);
		ExitButton.setOnClickListener(exitListener);	
		
		//savedInstanceState = this.getIntent().getExtras();
		
		//send_sin= savedInstanceState.getLong("sin");
		//send_sid= savedInstanceState.getString("sid");
		//send_key= savedInstanceState.getString("key");		
		

	}

	public void onPause() {
		super.onPause();

	}




}




	
	


	
	



