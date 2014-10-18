package com.zbar.lib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;



public class login_new extends Activity implements OnClickListener{
	String phonenumber;
	String password;

	//
	
	String  send_selleruin;
	String  send_employeename;
	String  send_oprshopname;
	byte[]  send_oprshoplogo;
	String  send_oprshopid;
	long 	send_sin;
	String  send_sid;
	String 	send_key;

	private int isNetError;

	private static final int ITEM1 = Menu.FIRST; 

	private int networkcode = 0;
	
	private ProgressDialog pBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Util.getDisplayMetrics(this);	
		
		if(Util.scryDP < 600)
			setContentView(R.layout.login_small);
		else
			setContentView(R.layout.login);
		
		findViewById(R.id.imageButton3).setOnClickListener( this);

	

        SharedPreferences userInfo = getSharedPreferences("user_info", 0);  
        String username = userInfo.getString("name", "");  
        String pass = userInfo.getString("pass", ""); 

		EditText mytext = (EditText)findViewById(R.id.editText1);
		mytext.setText(username);
		mytext = (EditText)findViewById(R.id.editText2);
		mytext.setText(pass);
		
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//Intent intent = new Intent();
		//intent.setClass(this, Main.class);
		//startActivityForResult(intent, 0);
		EditText mytext = (EditText)findViewById(R.id.editText1);
		
		phonenumber = mytext.getText().toString();
		
		mytext = (EditText)findViewById(R.id.editText2);
		password = mytext.getText().toString();
		
		if ((phonenumber.isEmpty()) || (password.isEmpty()))
		{
			
			Toast.makeText(getApplicationContext(), "输入信息不能为空",
				     Toast.LENGTH_SHORT).show();
			
		}
		else	
		{
			//Toast.makeText(login_new.this, "网络连接中，请稍后......",
			//		Toast.LENGTH_SHORT).show();
			pBar = new ProgressDialog(login_new.this);
			pBar.setTitle("正在登陆");
			pBar.setMessage("网络连接中，请稍后......");
			pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);		
			pBar.show();		
			Thread Certificate = new Thread(new LoginHandler());
			Certificate.start();
		}
		
	}

    public static  byte[] readInputStream(InputStream inputStream) throws IOException {  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return bos.toByteArray();  
          
    }  
	
	public static byte[] getImage(String path) throws IOException
	{
	    URL url = new URL(path);  
	    HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	    conn.setRequestMethod("GET");   //设置请求方法为GET  
	    conn.setReadTimeout(5*1000);    //设置请求过时时间为5秒  
	    InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据  
	    byte[] data = readInputStream(inputStream);     //获得图片的二进制数据  
	    return data; 
	}
	
	
	private boolean validateCertificate(String validateUrl) throws JSONException {

		// 用于标记登陆状态
		boolean loginState = false;
		networkcode = -2;
		HttpPost httpRequest =new HttpPost(validateUrl);
		//Post运作传送变数必须用NameValuePair[]阵列储存

		//传参数 服务端获取的方法为request.getParameter("name")
        
		String str = "{\"body\":{\"password\":\"8888\",\"phone\":\"13823338459\",\"bduserid\":\"1\",\"bdchannelid\":\"2\"},\"reqhead\":{\"sdkversion\":\"17\",\"cmd\":\"login\",\"cpuserial\":\"4b9760af4d0012c2\",\"devicename\":\"Lenovot-A789\",\"guid\":\"AEDFE29A52AA44EB9A0CDEA6157B7BD9\",\"mac\":\"40:F3:08:F4:6C:84\",\"imei\":\"358851054502087\",\"imsi\":\"460018828603788\",\"uin\":0,\"nettype\":3,\"ostype\":2,\"appcode\":1,\"seq\":3,\"height\":1920,\"width\":1080}}";
		
		String sendstr = str;
		
		
		
		sendstr= str.replaceAll("8888", password);
		sendstr= str.replaceAll("13823338459", phonenumber);
		sendstr = Util.changeMainString(this, sendstr);
		
		byte[] bs = sendstr.getBytes();
		
		byte[] postData =null;
		
		Log.d("tttttt", sendstr);
		
		postData = SecureUtil.encode(bs);
		
		
		
		//List params=new ArrayList();
		//params.add(new BasicNameValuePair("Username",userName));
		//params.add(new BasicNameValuePair("Pwd",password));	
		HttpResponse httpResponse = null;
		try{

			//发出HTTP request
			

			//httpRequest.setEntity(new UrlEncodedFormEntity(postData,HTTP.UTF_8));

			httpRequest.setHeader("WeShop-Version", "1.0");
			httpRequest.setHeader("WeShop-Sid", "");
			httpRequest.setEntity(new ByteArrayEntity(postData));
			//取得HTTP response
			

		    httpResponse=new DefaultHttpClient().execute(httpRequest);
			
			//若状态码为200 ok

			if(httpResponse.getStatusLine().getStatusCode()==200){
				
			//取出回应字串

			networkcode = 0;

			//strResult=EntityUtils.toString(httpResponse.getEntity());
			
			byte bsret[] = EntityUtils.toByteArray(httpResponse.getEntity());

			byte[] convertresult = SecureUtil.decode(bsret); 
			
			String jsonStr = new String(convertresult, "UTF-8");
			
			Log.d("tttttt", "login = " + jsonStr);
			
			JSONObject jsonObject = new JSONObject(jsonStr);  
			String mybody = jsonObject.getString("body");
			jsonObject = new JSONObject(mybody);
			String mykey = jsonObject.getString("key");
			String sid = jsonObject.getString("sid");
			long   uin = jsonObject.getLong("uin");
			
			Log.d("tttttt", "mykey = " + mykey);
			Log.d("tttttt", "sid = " + sid + " uin =" + uin);
			
			String mybook = sendstr.replaceAll("\"uin\":0", "\"uin\":" + uin);
			mybook = mybook.replaceAll("login", "userinfo");
			/*String mybook = "{\"body\":{\"selleruin\":\"10029\",\"couponid\":\"0025199992554\",\"oprshopid\":\"78\",\"oprshopname\":\"sfsdfs\",\"oprshopclerkname\":\"\"},\"reqhead\":{\"sdkversion\":\"17\",\"cmd\":\"couponuse\",\"cpuserial\":\"4b9760af4d0012c2\",\"devicename\":\"Lenovot-A789\",\"guid\":\"AEDFE29A52AA44EB9A0CDEA6157B7BD9\",\"mac\":\"40:F3:08:F4:6C:84\",\"imei\":\"358851054502087\",\"imsi\":\"460018828603788\",\"uin\":3,\"nettype\":3,\"ostype\":1,\"appcode\":1,\"seq\":3,\"height\":1920,\"width\":1080}}";*/
			
			HttpPost httpRequest2 =new HttpPost(Util.mainUrl);
			
			Log.d("tttttt", "source = " + mybook);
			
			Log.d("tttttt", "xdpi=" + Util.xdpi + "ydi = " + Util.ydpi);
			
			HttpResponse httpResponse2 = null;
			try{
				httpRequest2.setHeader("WeShop-Version", "1.0");
				httpRequest2.setHeader("WeShop-Sid", sid);
			
				byte[] bs2 = mybook.getBytes();
			
				byte[] postData2 =SecureUtil.encodeWithUkey(bs2,mykey);
			
				httpRequest2.setEntity(new ByteArrayEntity(postData2));
				//取得HTTP response
			

				httpResponse2=new DefaultHttpClient().execute(httpRequest2);
			
				//若状态码为200 ok

				if(httpResponse2.getStatusLine().getStatusCode()==200){
					byte bsret2[] = EntityUtils.toByteArray(httpResponse2.getEntity());

					byte[] convertresult2 = SecureUtil.decodeWithUkey(bsret2, mykey); 
					
					String jsonStr2 = new String(convertresult2, "UTF-8");
					
					Log.d("tttttt", "hexiao = " + jsonStr2);
					jsonObject = new JSONObject(jsonStr2);  
					mybody = jsonObject.getString("body");
					jsonObject = new JSONObject(mybody);					
					
					Log.d("tttttt", "selleruin = " + jsonObject.getString("selleruin"));
					


					



					send_selleruin = jsonObject.getString("selleruin");
					Log.d("tttttt", send_selleruin);
					send_employeename = jsonObject.getString("employeename");	
					Log.d("tttttt", send_employeename);
					send_oprshopname = jsonObject.getString("oprshopname");	
					Log.d("tttttt", send_oprshopname);
					String myoprshoplogo = jsonObject.getString("oprshoplogo");	
					Log.d("tttttt", myoprshoplogo);
					send_oprshopid =  jsonObject.getString("oprshopid");	
					Log.d("tttttt", send_oprshopid);
					send_sin = uin;
					Log.d("tttttt", "send_sin" + send_sin);
					send_sid =  sid;
					send_key = mykey;
				
					Log.d("tttttt", "myoprshoplogo" + myoprshoplogo);
					send_oprshoplogo =getImage(myoprshoplogo);
					
					

					
				}
			}
			
			catch(Exception e){
				e.printStackTrace();}
			//Log.d("tttttt","error code=" + httpResponse.getStatusLine().getStatusCode());

			
			//Log.d("tttttt", "ret = " + bsret.length + " " + jsonStr);
			//System.out.println("strResult: "+strResult);
			}
			
		}catch(Exception e){
			e.printStackTrace();}


		return loginState;
	}

	class LoginHandler implements Runnable {
		@Override
		public void run() {

			//这里换成你的验证地址
			networkcode = 0;
			String validateURL=Util.ssoUrl;
			try {
				boolean loginState = validateCertificate(validateURL);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("tttttt", "validateLogin " + validateURL );

			// 登陆成功
			if (send_sin > 0) {
				Intent intent = new Intent();
				intent.setClass(login_new.this, Main.class);
				Bundle bundle = new Bundle();
				
				SharedPreferences userInfo = getSharedPreferences("user_info", 0); 
				SharedPreferences.Editor editor = userInfo.edit(); 
                editor.putString("name", phonenumber);  
                editor.putString("pass", password); 
                editor.commit();
				
				bundle.putString("selleruin", send_selleruin);
				bundle.putString("employeename", send_employeename);		
				bundle.putString("oprshopname", send_oprshopname);		
				bundle.putByteArray("oprshoplogo", send_oprshoplogo);		
				bundle.putString("oprshopid", send_oprshopid);	
				bundle.putLong("sin", send_sin);
				bundle.putString("sid", send_sid);
				bundle.putString("key", send_key);
				Log.d("tttttt", "send_selleruin = " + send_selleruin);
				Log.d("tttttt", "send_selleruin" + send_employeename);
				Log.d("tttttt", "send_selleruin" + send_oprshopname);
				intent.putExtras(bundle);
				finish();
				// 转向登陆后的页面
				startActivity(intent);

			} else {
				// 通过调用handler来通知UI主线程更新UI,
				Message message = new Message();
				Bundle bundle = new Bundle();
				if (networkcode != -2)
					isNetError = 1001;
				else
					isNetError = 1000;
				
				pBar.cancel();	
				bundle.putInt("isNetError", isNetError);
				message.setData(bundle);
				loginHandler.sendMessage(message);
			}
		}

	}
	
	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			isNetError = msg.getData().getInt("isNetError");

			if (isNetError == 1001) {
				Toast.makeText(login_new.this, "登录失败，用户名或者密码错误",
						Toast.LENGTH_SHORT).show();
			}
			
			if (isNetError == 1000) {
				Toast.makeText(login_new.this, "网络错误",
						Toast.LENGTH_SHORT).show();
			}		

		}
	};


	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {  
	    menu.add(0, ITEM1, 0, "清除账户和密码");  

	    return true;  
	}  

	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {  
	   switch (item.getItemId()) {  
		    case ITEM1:  
				SharedPreferences userInfo = getSharedPreferences("user_info", 0); 
				SharedPreferences.Editor editor = userInfo.edit(); 
                editor.putString("name", "");  
                editor.putString("pass", ""); 
                editor.commit();
				EditText mytext = (EditText)findViewById(R.id.editText1);
				mytext.setText("");
				mytext = (EditText)findViewById(R.id.editText2);
				mytext.setText("");                
                
		       break;  
		

		}  
		return true;  
	}
	
	
}



