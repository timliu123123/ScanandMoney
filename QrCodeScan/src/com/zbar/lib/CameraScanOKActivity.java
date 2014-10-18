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


import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CameraScanOKActivity extends Activity {

	private Button view_loginCancel;


	TranslateAnimation mAnimation;
	long	MoneyOrPaper;
	
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
			setContentView(R.layout.zbar_scanok_small);
		else
			setContentView(R.layout.zbar_scanok);
		
		savedInstanceState = this.getIntent().getExtras();
		MoneyOrPaper = savedInstanceState.getLong("MoneyOrPaper");
		long responsecode = savedInstanceState.getLong("response_code");
		if (MoneyOrPaper == 0)
		{
			String couponContent = savedInstanceState.getString("couponContent");
			String couponCode = savedInstanceState.getString("couponCode");
			String beginDate = savedInstanceState.getString("beginDate");
			String endDate = savedInstanceState.getString("endDate");
			String sendShopName = savedInstanceState.getString("sendShopName");
			String couponRule = savedInstanceState.getString("couponRule");
			String couponInfo = savedInstanceState.getString("couponInfo");
			
			TextView scanText2 = (TextView)findViewById(R.id.scanText2);
			
			
			if (responsecode == 0)
				scanText2.setText("未领取");
			if (responsecode == 2)
				scanText2.setText("当前门店不支持");
			if (responsecode == 4)
				scanText2.setText("已使用");	
			if (responsecode == 8)
				scanText2.setText("已过期");	
			if (responsecode == 12)
				scanText2.setText("核销不支持");	
			
			
			Log.d("tttttt", couponContent + couponCode + beginDate +endDate +
					sendShopName +couponRule +couponInfo );
			
			
			
			TextView nText = (TextView)findViewById(R.id.netText3);
			nText.setText(couponContent + "        ");
			nText = (TextView)findViewById(R.id.netText4);
			nText.setText(couponCode +"        ");		
			
			nText = (TextView)findViewById(R.id.netText5);
			if (endDate != null)
				nText.setText(getStrTime(endDate) + "        ");
			else
				nText.setText("");
			
			nText = (TextView)findViewById(R.id.netText6);
			nText.setText(sendShopName +"        ");
	
			
			nText = (TextView)findViewById(R.id.netText8);
			nText.setText(couponRule +"        ");	
			nText = (TextView)findViewById(R.id.netText9);
			nText.setText(couponInfo +"        ");			
		}
		else
		{
			TextView nText = (TextView)findViewById(R.id.scanText1);
			nText.setText("扫码销红包");
			
			TextView scanText2 = (TextView)findViewById(R.id.scanText2);
			if (responsecode == 1)
				scanText2.setText("不可使用");
			if (responsecode == 2)
				scanText2.setText("当前门店不支持");
			
			nText = (TextView)findViewById(R.id.netText3);
			String name = savedInstanceState.getString("name");
			nText.setText(name + "        ");
			String couponCode = savedInstanceState.getString("couponCode");
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
		

		
		view_loginCancel = (Button)findViewById(R.id.clientbutton);
		view_loginCancel.setOnClickListener(cancelListener);

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




}




	
	


	
	



