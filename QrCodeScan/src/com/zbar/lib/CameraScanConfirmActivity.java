/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 * 
 * Created by lisah0 on 2012-02-24
 */
package com.zbar.lib;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;



import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import com.zbar.lib.R;

import android.annotation.SuppressLint;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.app.AlertDialog; 
import android.app.Dialog; 

public class CameraScanConfirmActivity extends Activity {

	private Button view_loginSubmit;
	private Button view_loginCancel;

	String couponContent;
	String couponCode;
	String beginDate;
	String endDate;
	String sendShopName;
	String couponRule;
	String couponInfo;

	String name;
	
	long 	send_sin;
	long	MoneyOrPaper;
	String  send_sid;
	String 	send_key;
	String  send_selleruin;
	String  send_employeename;
	String  send_oprshopname;
	String  send_oprshopid;
	long  	response_code = 0;
	String  response_str;
	
	int   returnorexecute = 0;




TranslateAnimation mAnimation;

String myResult = null;

@SuppressLint("SimpleDateFormat")
public static String getStrTime(String cc_time) {
	String re_StrTime = null;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 例如：cc_time=1291778220
	long lcc_time = Long.valueOf(cc_time);
	re_StrTime = sdf.format(new Date(lcc_time));

	return re_StrTime;

}

public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);


	if (Util.scryDP < 600)
		setContentView(R.layout.zbar_scanconfirm_small);
	else
		setContentView(R.layout.zbar_scanconfirm);		
	
	savedInstanceState = this.getIntent().getExtras();
	
	MoneyOrPaper = savedInstanceState.getLong("MoneyOrPaper");
	couponCode = savedInstanceState.getString("couponCode");
	
	if (MoneyOrPaper == 0)
	{
		couponContent = savedInstanceState.getString("couponContent");

		beginDate = savedInstanceState.getString("beginDate");
		endDate = savedInstanceState.getString("endDate");
		sendShopName = savedInstanceState.getString("sendShopName");
		couponRule = savedInstanceState.getString("couponRule");
		couponInfo = savedInstanceState.getString("couponInfo");
		Log.d("tttttt", couponContent + couponCode + beginDate +endDate +
				sendShopName +couponRule +couponInfo );
		
		TextView nText = (TextView)findViewById(R.id.netText3);
		nText.setText(couponContent + "        ");
		nText = (TextView)findViewById(R.id.netText4);
		nText.setText(couponCode +"        ");		
		nText = (TextView)findViewById(R.id.netText5);
		nText.setText(getStrTime(endDate) + "        ");
		nText = (TextView)findViewById(R.id.netText6);
		nText.setText("未核销" +"        ");

	
		nText = (TextView)findViewById(R.id.netText8);
		nText.setText(couponRule +"        ");	
		nText = (TextView)findViewById(R.id.netText9);
		nText.setText(couponInfo +"        ");		
	}
	else
	{
		name = savedInstanceState.getString("name");
		TextView nText = (TextView)findViewById(R.id.scanText1);
		nText.setText("扫码销红包");
		
		nText = (TextView)findViewById(R.id.netText3);
		nText.setText(name + "        ");
		nText = (TextView)findViewById(R.id.netText4);
		nText.setText(couponCode +"        ");	

		nText = (TextView)findViewById(R.id.scanText3);
		nText.setText("红包");
		nText = (TextView)findViewById(R.id.scanText4);
		nText.setText("号码");	
		
		nText = (TextView)findViewById(R.id.netText5);
		nText.setVisibility(View.GONE);
		nText = (TextView)findViewById(R.id.netText6);
		nText.setVisibility(View.GONE);
		nText = (TextView)findViewById(R.id.netText8);
		nText.setVisibility(View.GONE);
		nText = (TextView)findViewById(R.id.netText9);
		nText.setVisibility(View.GONE);	
		nText = (TextView)findViewById(R.id.scanText5);
		nText.setVisibility(View.GONE);
		nText = (TextView)findViewById(R.id.scanText6);
		nText.setVisibility(View.GONE);
		nText = (TextView)findViewById(R.id.scanText8);
		nText.setVisibility(View.GONE);
		nText = (TextView)findViewById(R.id.scanText9);
		nText.setVisibility(View.GONE);					
	}
	

	
	view_loginSubmit = (Button)findViewById(R.id.confirm);
	view_loginSubmit.setOnClickListener(submitListener);
		
	view_loginCancel = (Button)findViewById(R.id.cancel);
	view_loginCancel.setOnClickListener(cancelListener);

	send_sin= savedInstanceState.getLong("sin");

	send_sid= savedInstanceState.getString("sid");
	send_key= savedInstanceState.getString("key");
	myResult= savedInstanceState.getString("myResult");
	
	send_selleruin = savedInstanceState.getString("selleruin");
	send_employeename = savedInstanceState.getString("oprshopname");
	send_oprshopname = savedInstanceState.getString("employeename");;
	send_oprshopid= savedInstanceState.getString("oprshopid");

}

private OnClickListener submitListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
		if (returnorexecute == 0)
		{
			if(MoneyOrPaper == 0)
			{
				Thread Certificate = new Thread(new CerticateHandler());
				Certificate.start();
			}
			else
			{
				Thread Moneythread = new Thread(new MoneyOrPaperHandler());
				Moneythread.start();					
			}
			
		}
		else
		{
			finish();
		}
	}
};


Handler loginHandler = new Handler() {
	public void handleMessage(Message msg) {
		long isNetError = msg.getData().getLong("isNetError");

		if (isNetError == -1) {
			Toast.makeText(CameraScanConfirmActivity.this, "通讯错误",
					Toast.LENGTH_SHORT).show();
			finish();
		}

		if (isNetError != 1) {
			//Toast.makeText(CameraScanConfirmActivity.this, response_str,
			//		Toast.LENGTH_SHORT).show();
			   //finish();
			String message1 = Util.returnErrorMessage(response_code);
			Dialog alertDialog = new AlertDialog.Builder(CameraScanConfirmActivity.this). 
		                setTitle(response_str). 
		                setMessage(message1). 
		                setIcon(R.drawable.icon).
		                setNegativeButton("确定", new DialogInterface.OnClickListener() { 
		                     
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) { 
		                        finish();  
		                    }}).create(); 
		        alertDialog.show(); 
			
			
		}
		
		if (isNetError == 1) {
			
			TextView nText = (TextView)findViewById(R.id.scanText2);
			nText.setText("核销成功");
			view_loginSubmit.setBackgroundResource(R.drawable.return1);
			returnorexecute = 1;
			nText = (TextView)findViewById(R.id.netText6);
			nText.setText(send_employeename +"        ");

			//Toast.makeText(CameraScanConfirmActivity.this, "核销成功",
			//		Toast.LENGTH_SHORT).show();
		}
		
		
		

	}
};

private long validateCertificate() {

	
    String mybook = "{\"body\":{\"selleruin\":\"10029\",\"couponid\":\"0025199992554\",\"oprshopid\":\"78\",\"oprshopname\":\"sfsdfs\",\"oprshopclerkname\":\"\"},\"reqhead\":{\"sdkversion\":\"17\",\"cmd\":\"couponuse\",\"cpuserial\":\"4b9760af4d0012c2\",\"devicename\":\"Lenovot-A789\",\"guid\":\"AEDFE29A52AA44EB9A0CDEA6157B7BD9\",\"mac\":\"40:F3:08:F4:6C:84\",\"imei\":\"358851054502087\",\"imsi\":\"460018828603788\",\"uin\":3,\"nettype\":3,\"ostype\":1,\"appcode\":1,\"seq\":3,\"height\":1920,\"width\":1080}}";
	
	HttpPost httpRequest2 =new HttpPost(Util.mainUrl);
	
	
	
	mybook = mybook.replaceAll("10029", send_selleruin);
	mybook = mybook.replaceAll("0025199992554", myResult);
	//mybook = mybook.replaceAll("78", send_oprshopid);
	mybook = mybook.replaceAll("oprshopid\":\"78\"", "oprshopid\":" + "\"" + send_oprshopid + "\"");
	mybook = mybook.replaceAll("sfsdfs", send_oprshopname); 
	mybook = mybook.replaceAll("\"oprshopclerkname\":\"\"", "\"oprshopclerkname\":\"" +  send_employeename + "\"");
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
		//取得HTTP response
	

		httpResponse2=new DefaultHttpClient().execute(httpRequest2);
	
		//若状态码为200 ok

		if(httpResponse2.getStatusLine().getStatusCode()==200){
			byte bsret2[] = EntityUtils.toByteArray(httpResponse2.getEntity());

			byte[] convertresult2 = SecureUtil.decodeWithUkey(bsret2, send_key); 
			
			String jsonStr2 = new String(convertresult2, "UTF-8");
			Log.d("tttttt", "hexiao = " + jsonStr2);
			
			JSONObject jsonObject = new JSONObject(jsonStr2);  
			String mybody = jsonObject.getString("body");
			jsonObject = new JSONObject(mybody);
			response_code = jsonObject.getLong("code");
			response_str = jsonObject.getString("msg");
			
			Log.d("tttttt", "response_code = " + response_code  + "msg = " + response_str);

			return response_code;
		}
	}
	
	catch(Exception e){
		e.printStackTrace();
		response_code = -1;
	}





return response_code;
}



class CerticateHandler implements Runnable {
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


private OnClickListener cancelListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
		finish();
	}
};

public void onPause() {
	super.onPause();

}



private long MoneyorPaperCertificate() {

	
    String mybook = "{\"body\":{\"selleruin\":\"10029\",\"vouchersid\":\"0025199992554\",\"shopid\":\"78\",\"oprshopname\":\"sfsdfs\",\"oprshopclerkname\":\"\"},\"reqhead\":{\"sdkversion\":\"17\",\"cmd\":\"vouchers_use\",\"cpuserial\":\"4b9760af4d0012c2\",\"devicename\":\"Lenovot-A789\",\"guid\":\"AEDFE29A52AA44EB9A0CDEA6157B7BD9\",\"mac\":\"40:F3:08:F4:6C:84\",\"imei\":\"358851054502087\",\"imsi\":\"460018828603788\",\"uin\":3,\"nettype\":3,\"ostype\":1,\"appcode\":1,\"seq\":3,\"height\":1920,\"width\":1080}}";
	
	HttpPost httpRequest2 =new HttpPost(Util.mainUrl);
	
	
	
	mybook = mybook.replaceAll("10029", send_selleruin);
	mybook = mybook.replaceAll("0025199992554", myResult);
	//mybook = mybook.replaceAll("78", send_oprshopid);
	mybook = mybook.replaceAll("shopid\":\"78\"", "shopid\":" + "\"" + send_oprshopid + "\"");
	mybook = mybook.replaceAll("sfsdfs", send_oprshopname); 
	mybook = mybook.replaceAll("\"oprshopclerkname\":\"\"", "\"oprshopclerkname\":\"" +  send_employeename + "\"");
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
		//取得HTTP response
	

		httpResponse2=new DefaultHttpClient().execute(httpRequest2);
	
		//若状态码为200 ok

		if(httpResponse2.getStatusLine().getStatusCode()==200){
			byte bsret2[] = EntityUtils.toByteArray(httpResponse2.getEntity());

			byte[] convertresult2 = SecureUtil.decodeWithUkey(bsret2, send_key); 
			
			String jsonStr2 = new String(convertresult2, "UTF-8");
			Log.d("tttttt", "hexiao = " + jsonStr2);
			
			JSONObject jsonObject = new JSONObject(jsonStr2);  
			String mybody = jsonObject.getString("body");
			jsonObject = new JSONObject(mybody);
			response_code = jsonObject.getLong("code");
			response_str = jsonObject.getString("msg");
			
			Log.d("tttttt", "response_code = " + response_code  + "msg = " + response_str);

			return response_code;
		}
	}
	
	catch(Exception e){
		e.printStackTrace();
		response_code = -1;
	}





return response_code;
}



class MoneyOrPaperHandler implements Runnable {
	@Override
	public void run() {

		//这里换成你的验证地址
		long loginState = MoneyorPaperCertificate();
		Log.d("tttttt", "validateLogin");

		Message message = new Message();
		Bundle bundle = new Bundle();
		bundle.putLong("isNetError", loginState);
		message.setData(bundle);
		loginHandler.sendMessage(message);


	}

}	



}













