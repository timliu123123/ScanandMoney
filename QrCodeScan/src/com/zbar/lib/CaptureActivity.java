package com.zbar.lib;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.view.SurfaceView;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zbar.lib.R;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * 
 * 时间: 2014年5月9日 下午12:25:31
 * 
 * 版本: V_1.0.0
 * 
 * 描述: 扫描界面
 */
public class CaptureActivity extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.50f;
	private boolean vibrate;
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private RelativeLayout mContainer = null;
	private RelativeLayout mCropLayout = null;
	private boolean isNeedCapture = false;
	
	String myResult = null;
	String  send_selleruin;
	String  send_employeename;
	String  send_oprshopname;
	String  send_oprshopid;
	String  shopIds = "";
	long 	send_sin;
	long	MoneyOrPaper;
	String  send_sid;
	String 	send_key;

	long mycode = 0;

	TranslateAnimation mAnimation;
	
    long     response_code = 0;
    String   response_str = null; 

	Bundle bundle = new Bundle();

	private OnClickListener inputListener = new OnClickListener() {
		@Override
		public void onClick(View v) {


			
			finish();
			
			Intent intent = new Intent();
			if (MoneyOrPaper == 0)
				intent.setClass(CaptureActivity.this, CameraScanInputActivity.class);
			else
				intent.setClass(CaptureActivity.this, MoneyScanInputActivity.class);
			
			Bundle bundle = new Bundle();
			
				bundle.putLong("sin", send_sin);
				bundle.putLong("MoneyOrPaper", MoneyOrPaper);
				bundle.putString("sid", send_sid);
				bundle.putString("key", send_key);
				bundle.putString("myResult", "");
				bundle.putString("selleruin", send_selleruin);
				bundle.putString("employeename", send_employeename);		
				bundle.putString("oprshopname", send_oprshopname);		
				bundle.putString("oprshopid", send_oprshopid);	
				Log.d("ttttttt", "send_selleruin" + send_selleruin);
				intent.putExtras(bundle);
				startActivity(intent);
		}
	};

	
	
	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.activity_qr_scan);
		// 初始化 CameraManager
		
		if (Util.scryDP < 600)
			setContentView(R.layout.activity_qr_scan);
		else
			setContentView(R.layout.activity_qr_scan);

		savedInstanceState = this.getIntent().getExtras();

		send_selleruin = savedInstanceState.getString("selleruin");
		send_employeename = savedInstanceState.getString("oprshopname");
		send_oprshopname = savedInstanceState.getString("employeename");;
		send_oprshopid= savedInstanceState.getString("oprshopid");
		send_sin= savedInstanceState.getLong("sin");
		MoneyOrPaper = savedInstanceState.getLong("MoneyOrPaper");
		send_sid= savedInstanceState.getString("sid");
		send_key= savedInstanceState.getString("key");
		
		
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);

		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
	    mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		mAnimation.setDuration(1500);
		mAnimation.setRepeatCount(-1);
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);
		
		Button scanButton = (Button)findViewById(R.id.inputbutton);
		scanButton.setOnClickListener(inputListener);
		
		if (MoneyOrPaper == 1)
		{
			TextView nText = (TextView)findViewById(R.id.scanText1);
			nText.setText("扫码销红包");
		}	
	}

	boolean flag = true;

	protected void light() {
		if (flag == true) {
			flag = false;
			// 开闪光灯
			CameraManager.get().openLight();
		} else {
			flag = true;
			// 关闪光灯
			CameraManager.get().offLight();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	public void handleDecode(String result) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		mAnimation.cancel();
		
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		inactivityTimer.shutdown();
		
		myResult = result;
		
		Log.d("tttttt","wewqeweqewq   myResult= " + myResult);
		
		//TextView mytext = (TextView) findViewById(R.id.scanText);
		//mytext.setText("已扫描，正在处理......" + myResult);
		
		if (MoneyOrPaper == 0)
		{	
			Thread Certificate = new Thread(new CertificateHandler());
			Certificate.start();
		}
		else
		{
			Thread MoneyOrPaper = new Thread(new MoneyOrPaperHandler());
			MoneyOrPaper.start();						
		}
		
		//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

		// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
		// handler.sendEmptyMessage(R.id.restart_preview);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;

			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();

			int cropWidth = mCropLayout.getWidth() * width / mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height / mContainer.getHeight();

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			// 设置是否需要截图
			setNeedCapture(true);
			

		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(CaptureActivity.this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public Handler getHandler() {
		return handler;
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	
	
	private boolean validateCertificate(String validateUrl) {

		
        String mybook = "{\"body\":{\"selleruin\":\"10029\",\"couponid\":\"0025199992554\",\"oprshopid\":\"78\",\"oprshopname\":\"sfsdfs\",\"oprshopclerkname\":\"\"},\"reqhead\":{\"sdkversion\":\"17\",\"cmd\":\"couponuse\",\"cpuserial\":\"4b9760af4d0012c2\",\"devicename\":\"Lenovot-A789\",\"guid\":\"AEDFE29A52AA44EB9A0CDEA6157B7BD9\",\"mac\":\"40:F3:08:F4:6C:84\",\"imei\":\"358851054502087\",\"imsi\":\"460018828603788\",\"uin\":3,\"nettype\":3,\"ostype\":1,\"appcode\":1,\"seq\":3,\"height\":1920,\"width\":1080}}";
	
		
		
		mybook = mybook.replaceAll("10029", send_selleruin);
		
		Log.d("tttttt", "source000 = " + mybook);
		mybook = mybook.replaceAll("0025199992554", myResult);
		
		Log.d("tttttt", "source001 = " + mybook);
		
		//mybook = mybook.replaceAll("0025199992554", "0026399994980");
		
		mybook = mybook.replaceAll("oprshopid\":\"78\"", "oprshopid\":" + "\"" + send_oprshopid + "\"");
		
		mybook = mybook.replaceAll("sfsdfs", send_oprshopname);
		mybook = mybook.replaceAll("\"oprshopclerkname\":\"\"", "\"oprshopclerkname\":\"" +  send_employeename + "\"");
		mybook = mybook.replaceAll("\"uin\":3", "\"uin\":" + send_sin);
		
		mybook = Util.changeMainString(this, mybook);
		
		Log.d("tttttt", "source = " + mybook);
		

				
				//send another packet
				mybook = mybook.replaceAll("couponuse","couponquery");
				
				HttpPost httpRequest1 =new HttpPost(Util.mainUrl);
				HttpResponse httpResponse1 = null;
				
				
				try{
					httpRequest1.setHeader("WeShop-Version", "1.0");
					httpRequest1.setHeader("WeShop-Sid", send_sid);
		
					byte[] bs1 = mybook.getBytes();
		
					byte[] postData1 =SecureUtil.encodeWithUkey(bs1,send_key);
		
					httpRequest1.setEntity(new ByteArrayEntity(postData1));
					//HTTP response
		

					httpResponse1 = new DefaultHttpClient().execute(httpRequest1);
		
					//200 ok

					if(httpResponse1.getStatusLine().getStatusCode()==200){
						byte bsret1[] = EntityUtils.toByteArray(httpResponse1.getEntity());

						byte[] convertresult1 = SecureUtil.decodeWithUkey(bsret1, send_key); 
				
						String jsonStr1 = new String(convertresult1, "UTF-8");
						
						Log.d("tttttt", "hexiao query = " + jsonStr1);
						
						JSONObject jsonObject1 = new JSONObject(jsonStr1);  
						Log.d("tttttt", "11 2382894238973248934289") ;
						
						String mybody1 = jsonObject1.getString("body");
						Log.d("tttttt", "22 " + mybody1) ;
						
						
						
						jsonObject1 = new JSONObject(mybody1);
						
						mycode = jsonObject1.getLong("code");
						
						if (mycode == 0)
						{
							String mybody0 = jsonObject1.getString("coupon");
							Log.d("tttttt", "22 " + mybody0) ;
							jsonObject1 = new JSONObject(mybody0);

							Log.d("tttttt", "33 2382894238973248934289") ;
							
								bundle.putString("couponContent", jsonObject1.getString("couponContent"));
								Log.d("tttttt", "44 2382894238973248934289") ;
								
								bundle.putString("couponCode", myResult);		
								Log.d("tttttt", "55 2382894238973248934289") ;
								
								bundle.putString("beginDate", jsonObject1.getString("beginDate"));	
								Log.d("tttttt", "66 2382894238973248934289") ;
								
								bundle.putString("endDate", jsonObject1.getString("endDate"));	
								
								Log.d("tttttt", "77 2382894238973248934289") ;
								
								bundle.putString("sendShopName", jsonObject1.getString("sendShopName"));	
								
								Log.d("tttttt", "88 2382894238973248934289") ;
								bundle.putString("couponRule", jsonObject1.getString("couponRule"));	
								Log.d("tttttt", "99 2382894238973248934289") ;
								
								bundle.putString("couponInfo", jsonObject1.getString("couponInfo"));
								bundle.putString("shopIds", jsonObject1.getString("shopIds"));
								shopIds = jsonObject1.getString("shopIds");
						
								Log.d("tttttt", jsonObject1.getString("couponContent") + myResult + jsonObject1.getString("beginDate") +jsonObject1.getString("endDate") +
										jsonObject1.getString("sendShopName") +jsonObject1.getString("couponRule") +jsonObject1.getString("couponInfo") );		
								response_code = jsonObject1.getLong("status");
						}
						else
						{

							String message1 = Util.returnErrorMessage(mycode);
							

							
								bundle.putString("couponContent", "");

								
								bundle.putString("couponCode", myResult);		
;
								
								bundle.putString("beginDate", "");	
								Log.d("tttttt", "66 2382894238973248934289") ;
								
								bundle.putString("endDate", null);	
								
								Log.d("tttttt", "77 2382894238973248934289") ;
								
								bundle.putString("sendShopName", "");	
								
								Log.d("tttttt", "88 2382894238973248934289") ;
								bundle.putString("couponRule", "");	
								Log.d("tttttt", "99 2382894238973248934289") ;
								
								bundle.putString("couponInfo", message1);
								bundle.putString("shopIds", "");
								shopIds = "";								
								response_code = 8;
						}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
				
	

	return true;
}

	
	class CertificateHandler implements Runnable {
		@Override
		public void run() {


			String validateURL=Util.mainUrl;
			boolean loginState = validateCertificate(validateURL);
			Log.d("tttttt", "validateLogin");
			
			//response_code = 1;//test
			boolean currentShopOk = true;

			if ( (shopIds.equals("all")) || shopIds.contains(send_oprshopid))
				currentShopOk = true;
			else
				currentShopOk = false;
			

			if  ((response_code == 1) || (response_code == 2) && (currentShopOk))  {

				Intent intent = new Intent();
				intent.setClass(CaptureActivity.this, CameraScanConfirmActivity.class);

				bundle.putLong("sin", send_sin);
				bundle.putLong("MoneyOrPaper", MoneyOrPaper);
				bundle.putString("sid", send_sid);
				bundle.putString("key", send_key);
				bundle.putString("myResult", myResult);
				bundle.putString("selleruin", send_selleruin);
				bundle.putString("employeename", send_employeename);		
				bundle.putString("oprshopname", send_oprshopname);		
				bundle.putString("oprshopid", shopIds);	

				intent.putExtras(bundle);
				finish();

				startActivity(intent);

			} else if ( ( (response_code == 4) || (response_code == 8) || (response_code == 0)) || (!currentShopOk))
			 {

				Intent intent = new Intent();
				intent.setClass(CaptureActivity.this, CameraScanOKActivity.class);
				
				bundle.putLong("MoneyOrPaper", MoneyOrPaper);
				
				if (mycode == 0)
					bundle.putLong("response_code", response_code);
				else
					bundle.putLong("response_code", 12);
				
				if (currentShopOk)
					bundle.putString("currentShopOk", "true");
				else
					bundle.putString("currentShopOk", "false");
				
				intent.putExtras(bundle);
				finish();

				startActivity(intent);
			}
			else
			{
				Intent intent = new Intent();
				intent.setClass(CaptureActivity.this, CameraScanFailureActivity.class);

				bundle.putLong("sin", send_sin);
				bundle.putLong("MoneyOrPaper", MoneyOrPaper);
				bundle.putString("sid", send_sid);
				bundle.putString("key", send_key);
				bundle.putString("myResult", myResult);
				bundle.putString("selleruin", send_selleruin);
				bundle.putString("employeename", send_employeename);		
				bundle.putString("oprshopname", send_oprshopname);		
				bundle.putString("oprshopid", send_oprshopid);	
				Log.d("ttttttt", "activity send_selleruin" + send_selleruin);

				intent.putExtras(bundle);
				finish();

				startActivity(intent);				
			}
		}

	}
	
	private boolean validateMoneyOrPaper(String validateUrl) {

		
        String mybook = "{\"body\":{\"selleruin\":\"10029\",\"vouchersid\":\"0025199992554\",\"shopid\":\"78\"},\"reqhead\":{\"sdkversion\":\"17\",\"cmd\":\"vouchers_query\",\"cpuserial\":\"4b9760af4d0012c2\",\"devicename\":\"Lenovot-A789\",\"guid\":\"AEDFE29A52AA44EB9A0CDEA6157B7BD9\",\"mac\":\"40:F3:08:F4:6C:84\",\"imei\":\"358851054502087\",\"imsi\":\"460018828603788\",\"uin\":3,\"nettype\":3,\"ostype\":1,\"appcode\":1,\"seq\":3,\"height\":1920,\"width\":1080}}";
	
        response_code = 1;
		
		mybook = mybook.replaceAll("10029", send_selleruin);
		
		Log.d("tttttt", "source000 = " + mybook);
		mybook = mybook.replaceAll("0025199992554", myResult);
		
		Log.d("tttttt", "source001 = " + mybook);
		
		//mybook = mybook.replaceAll("0025199992554", "0026399994980");
		
		mybook = mybook.replaceAll("shopid\":\"78\"", "shopid\":" + "\"" + send_oprshopid + "\"");
		
		mybook = mybook.replaceAll("\"uin\":3", "\"uin\":" + send_sin);
		
		mybook = Util.changeMainString(this, mybook);
		
		Log.d("tttttt", "source = " + mybook);
		

				
				//send another packet
				mybook = mybook.replaceAll("couponuse","vouchers_query");
				
				HttpPost httpRequest1 =new HttpPost(Util.mainUrl);
				HttpResponse httpResponse1 = null;
				
				
				try{
					httpRequest1.setHeader("WeShop-Version", "1.0");
					httpRequest1.setHeader("WeShop-Sid", send_sid);
		
					byte[] bs1 = mybook.getBytes();
		
					byte[] postData1 =SecureUtil.encodeWithUkey(bs1,send_key);
		
					httpRequest1.setEntity(new ByteArrayEntity(postData1));

		

					httpResponse1 = new DefaultHttpClient().execute(httpRequest1);
		


					if(httpResponse1.getStatusLine().getStatusCode()==200){
						byte bsret1[] = EntityUtils.toByteArray(httpResponse1.getEntity());

						byte[] convertresult1 = SecureUtil.decodeWithUkey(bsret1, send_key); 
				
						String jsonStr1 = new String(convertresult1, "UTF-8");
						
						Log.d("tttttt", "hexiao query = " + jsonStr1);
						
						JSONObject jsonObject1 = new JSONObject(jsonStr1);  
						Log.d("tttttt", "11 2382894238973248934289") ;
						
						String mybody1 = jsonObject1.getString("body");
						Log.d("tttttt", "22 " + mybody1) ;
						
						
						
						jsonObject1 = new JSONObject(mybody1);
						
						mycode = jsonObject1.getLong("code");
						
						if (mycode == 0)
						{
							String mybody0 = jsonObject1.getString("vouchers");
							Log.d("tttttt", "22 " + mybody0) ;
							jsonObject1 = new JSONObject(mybody0);

							Log.d("tttttt", "33 2382894238973248934289") ;

							bundle.putString("couponCode", myResult);		
							Log.d("tttttt", "55 2382894238973248934289") ;
							
							bundle.putString("name", jsonObject1.getString("name"));
							Log.d("tttttt", "44 2382894238973248934289") ;
								

							bundle.putLong("status", jsonObject1.getLong("status"));
							Log.d("tttttt", "44 2382894238973248934289") ;
								
							bundle.putString("statusdesc", jsonObject1.getString("statusdesc"));	
								

						
							Log.d("tttttt", "name = " + jsonObject1.getString("name") + " scancode = " + myResult 
											+ " status=" + jsonObject1.getLong("status") +
											" desc =" + jsonObject1.getString("statusdesc") );		
							response_code = jsonObject1.getLong("status");
						}
						else
						{

							String message1 = Util.returnErrorMessage(mycode);						

							
							bundle.putString("name", "");
							Log.d("tttttt", "44 2382894238973248934289") ;
								

							bundle.putLong("status", -1);
							Log.d("tttttt", "44 2382894238973248934289") ;
								
							bundle.putString("statusdesc", message1);	

								
							bundle.putString("couponCode", myResult);		

							
							response_code = 1;
						}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
				
	

	return true;
}

	
	class MoneyOrPaperHandler implements Runnable {
		@Override
		public void run() {


			String validateURL=Util.mainUrl;
			boolean loginState = validateMoneyOrPaper(validateURL);
			Log.d("tttttt", "validateLogin");
			

			

			if  ((response_code == 0) )  {

				Intent intent = new Intent();
				intent.setClass(CaptureActivity.this, CameraScanConfirmActivity.class);

				bundle.putLong("sin", send_sin);
				bundle.putLong("MoneyOrPaper", MoneyOrPaper);
				bundle.putString("sid", send_sid);
				bundle.putString("key", send_key);
				bundle.putString("myResult", myResult);
				bundle.putString("selleruin", send_selleruin);
				bundle.putString("employeename", send_employeename);		
				bundle.putString("oprshopname", send_oprshopname);		
				bundle.putString("oprshopid", shopIds);	

				intent.putExtras(bundle);
				finish();

				startActivity(intent);

			} 
			else if  ( (response_code == 1) || (response_code == 2) ) 
			{

				Intent intent = new Intent();
				intent.setClass(CaptureActivity.this, CameraScanOKActivity.class);
				
				bundle.putLong("MoneyOrPaper", MoneyOrPaper);
				

				bundle.putLong("response_code", response_code);

				

				
				intent.putExtras(bundle);
				finish();

				startActivity(intent);
			} 
			else
			{
			
			}
		}

	}	
	
	
	
}