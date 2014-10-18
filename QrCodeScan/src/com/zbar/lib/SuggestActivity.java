/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 * 
 * Created by lisah0 on 2012-02-24
 */
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
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SuggestActivity extends Activity {



	TranslateAnimation mAnimation;
	
	String myResult = null;

	long 	send_sin;
	//long	MoneyOrPaper;
	String  send_sid;
	String 	send_key;	
	
	long    returnOK;
	
	EditText myText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.suggestion);
		
		savedInstanceState = this.getIntent().getExtras();
		send_sin= savedInstanceState.getLong("sin");
		//MoneyOrPaper = savedInstanceState.getLong("MoneyOrPaper");
		send_sid= savedInstanceState.getString("sid");
		send_key= savedInstanceState.getString("key");
		
		myText = (EditText)findViewById(R.id.editText1);
		myText.setFocusableInTouchMode(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		Button view_loginSubmit = (Button)findViewById(R.id.clientbutton);
		view_loginSubmit.setOnClickListener(checknewListener);
		
	

	}

	public void onPause() {
		super.onPause();

	}

	private OnClickListener checknewListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Thread UpdateApk = new Thread(new UpdateHandler());
			UpdateApk.start();
		}
	};
	

	private long validateCertificate() {

	
	
        	String mybook = "{\"body\":{\"suggestion\":\"mnbv\"},\"reqhead\":{\"sdkversion\":\"17\",\"cmd\":\"sys_feedback\",\"cpuserial\":\"4b9760af4d0012c2\",\"devicename\":\"Lenovot-A789\",\"guid\":\"AEDFE29A52AA44EB9A0CDEA6157B7BD9\",\"mac\":\"40:F3:08:F4:6C:84\",\"imei\":\"358851054502087\",\"imsi\":\"460018828603788\",\"uin\":3,\"nettype\":3,\"ostype\":1,\"appcode\":1,\"seq\":3,\"height\":1920,\"width\":1080}}";
				
			HttpPost httpRequest2 =new HttpPost(Util.mainUrl);

			mybook = mybook.replaceAll("\"uin\":3", "\"uin\":" + send_sin);
			mybook = mybook.replaceAll("mnbv" , myText.getText().toString());
			mybook = Util.changeMainString(this, mybook);
			
			
			Log.d("tttttt", "source = " + mybook);
			
			HttpResponse httpResponse2 = null;
			try{
				httpRequest2.setHeader("WeShop-Version", "1.0");
				httpRequest2.setHeader("WeShop-Sid", send_sid);
			
				byte[] bs2 = mybook.getBytes();
			
				byte[] postData2 =SecureUtil.encodeWithUkey(bs2,send_key);
			
				httpRequest2.setEntity(new ByteArrayEntity(postData2));
				//取得HTTP response
			

				httpResponse2=new DefaultHttpClient().execute(httpRequest2);
			
				//若状态码为200 ok

				if(httpResponse2.getStatusLine().getStatusCode()==200){
					byte bsret2[] = EntityUtils.toByteArray(httpResponse2.getEntity());

					returnOK = 1;
					
					byte[] convertresult2 = SecureUtil.decodeWithUkey(bsret2, send_key); 
					
					String jsonStr2 = new String(convertresult2, "UTF-8");
					Log.d("tttttt", "hexiao = " + jsonStr2);
					
	
				}
			}
			
			catch(Exception e){
				e.printStackTrace();
				returnOK = -1;
			}

		

		

		return returnOK;
	}

	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			long isNetError = msg.getData().getLong("isNetError");

			if (isNetError == -1) {
				Toast.makeText(SuggestActivity.this, "通讯错误，谢谢您",
						Toast.LENGTH_SHORT).show();
			}

			if (isNetError == 1) {
				Toast.makeText(SuggestActivity.this, "提交成功，谢谢您......",
						Toast.LENGTH_SHORT).show();

			}
			
			if (isNetError == 0) {
				Toast.makeText(SuggestActivity.this, "谢谢您",
						Toast.LENGTH_SHORT).show();
			}
			
			finish();
			

		}
	};

	class UpdateHandler implements Runnable {
		@Override
		public void run() {

			//这里换成你的验证地址
			long loginState = validateCertificate();
			Log.d("tttttt", "validateLogin");

			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putLong("isNetError", loginState);
			message.setData(bundle);
			loginHandler.sendMessage(message);


		}

	}
	



}




	
	


	
	



