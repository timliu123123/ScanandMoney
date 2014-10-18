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
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class MoneyScanInputActivity extends Activity {



	TranslateAnimation mAnimation;
	
	String myResult = null;
	Long   mycode;
	
	Bundle bundle = new Bundle();
	
		long 	send_sin;
		long	MoneyOrPaper;
		String  send_sid;
		String 	send_key;
		String  send_selleruin;
		String  send_employeename;
		String  send_oprshopname;
		String  send_oprshopid;
		String  shopIds = "";
		long  	response_code = 0;
		String  response_str;
		EditText	myedit;

		private Button view_loginSubmit;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Util.scryDP < 600)
			setContentView(R.layout.zbar_moneyinput_small);
		else
			setContentView(R.layout.zbar_moneyinput);

		Button clientbutton = (Button)findViewById(R.id.clientbutton);
		clientbutton.setBackgroundResource(R.drawable.client_default);		

		clientbutton = (Button)findViewById(R.id.client_inputbutton);
		clientbutton.setBackgroundResource(R.drawable.client_input_activity);	
		
		if (Util.scryDP > 600)
		{
			LinearLayout linearout = (LinearLayout)findViewById(R.id.linearlayoutnull);
			linearout.setBackgroundColor(0xFF2F2F2F);
			linearout = (LinearLayout)findViewById(R.id.linearlayoutbutton);
			linearout.setBackgroundColor(0xFF2F2F2F);
		}
		
		savedInstanceState = this.getIntent().getExtras();
		send_sin= savedInstanceState.getLong("sin");
		MoneyOrPaper= savedInstanceState.getLong("MoneyOrPaper");
		send_sid= savedInstanceState.getString("sid");
		send_key= savedInstanceState.getString("key");
		myResult= savedInstanceState.getString("myResult");
		
		send_selleruin = savedInstanceState.getString("selleruin");
		send_employeename = savedInstanceState.getString("oprshopname");
		send_oprshopname = savedInstanceState.getString("employeename");;
		send_oprshopid= savedInstanceState.getString("oprshopid");

		view_loginSubmit = (Button)findViewById(R.id.inputbutton);
		view_loginSubmit.setOnClickListener(submitListener);
		
		TextView textTitle = (TextView)findViewById(R.id.scanText1);
		textTitle.setText("手工销红包");
		
		myedit = (EditText)findViewById(R.id.editText1);
		myedit.setText("");
		myedit.setFocusableInTouchMode(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

	}

	public void onPause() {
		super.onPause();

	}

	private OnClickListener submitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		
			myResult = "0000000" + myedit.getText().toString(); 
			if (myResult.length() > 0)
			{
				Thread MoneyOrPaper = new Thread(new MoneyOrPaperHandler());
				MoneyOrPaper.start();
				// 需要传输数据到登陆后的界面,
			
			}
			else
			{
		
				Toast.makeText(getApplicationContext(), "输入信息不能为空",
				     Toast.LENGTH_SHORT).show();	
			}
		}
	};

	
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
					//取得HTTP response
		

					httpResponse1 = new DefaultHttpClient().execute(httpRequest1);
		
					//若状态码为200 ok

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

			//这里换成你的验证地址
			String validateURL=Util.mainUrl;
			boolean loginState = validateMoneyOrPaper(validateURL);
			Log.d("tttttt", "validateLogin");
			

			
			// 登陆成功
			if  ((response_code == 0) )  {
				// 需要传输数据到登陆后的界面,
				Intent intent = new Intent();
				intent.setClass(MoneyScanInputActivity.this, CameraScanConfirmActivity.class);

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
				// 转向登陆后的页面
				startActivity(intent);

			} 
			else if  ( (response_code == 1) || (response_code == 2) ) 
			{
				// 通过调用handler来通知UI主线程更新UI,
				Intent intent = new Intent();
				intent.setClass(MoneyScanInputActivity.this, CameraScanOKActivity.class);
				
				bundle.putLong("MoneyOrPaper", MoneyOrPaper);
				

				bundle.putLong("response_code", response_code);

				

				
				intent.putExtras(bundle);
				finish();
				// 转向登陆后的页面
				startActivity(intent);
			} 
			else
			{
			
			}
		}

	}

	
	
	
	
	

	
	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			long isNetError = msg.getData().getLong("isNetError");

			if (isNetError == -1) {
				Toast.makeText(MoneyScanInputActivity.this, "通讯错误",
						Toast.LENGTH_SHORT).show();
				finish();
			}

			if (isNetError != 0) {
				//Toast.makeText(MoneyScanInputActivity.this, response_str,
				//		Toast.LENGTH_SHORT).show();
					String message1 = Util.returnErrorMessage(response_code);
					Dialog alertDialog = new AlertDialog.Builder(MoneyScanInputActivity.this). 
			                setTitle("核销错误"). 
			                setMessage(message1). 
			                setIcon(R.drawable.icon).
			                setNegativeButton("确定", new DialogInterface.OnClickListener() { 
			                     
			                    @Override 
			                    public void onClick(DialogInterface dialog, int which) { 
			                        finish();  
			                    }}).create(); 
			        alertDialog.show(); 				
				
			}
			
			if (isNetError == 0) {
				finish();
				Dialog alertDialog = new AlertDialog.Builder(MoneyScanInputActivity.this). 
		                setTitle("核销成功"). 
		                setMessage("红包代码输入正确，核销已成功"). 
		                setIcon(R.drawable.icon).
		                setNegativeButton("确定", new DialogInterface.OnClickListener() { 
		                     
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) { 
		                        finish();  
		                    }}).create(); 
		        alertDialog.show(); 				

			}
			
			
			

		}
	};

	
	

}




	
	


	
	



