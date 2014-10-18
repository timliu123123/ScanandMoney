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

public class CameraScanFailureActivity extends Activity {

	Button view_loginInput;



	TranslateAnimation mAnimation;
	
	String myResult = null;

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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Util.scryDP < 600)
			setContentView(R.layout.zbar_scanfailure_small);
		else
			setContentView(R.layout.zbar_scanfailure);


		view_loginInput = (Button)findViewById(R.id.inputbutton);
		view_loginInput.setOnClickListener(inputListener);
		
		savedInstanceState = this.getIntent().getExtras();

		send_sin= savedInstanceState.getLong("sin");
		MoneyOrPaper = savedInstanceState.getLong("MoneyOrPaper");
		send_sid= savedInstanceState.getString("sid");
		send_key= savedInstanceState.getString("key");
		myResult= savedInstanceState.getString("myResult");
		
		send_selleruin = savedInstanceState.getString("selleruin");
		send_employeename = savedInstanceState.getString("oprshopname");
		send_oprshopname = savedInstanceState.getString("employeename");;
		send_oprshopid= savedInstanceState.getString("oprshopid");

		//Button scanButton = (Button)findViewById(R.id.clientbutton);
		//scanButton.Set

	}

	private OnClickListener inputListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
		
			finish();
			
			Intent intent = new Intent();
			intent.setClass(CameraScanFailureActivity.this, CameraScanInputActivity.class);
			Bundle bundle = new Bundle();
			
				bundle.putLong("sin", send_sin);
				bundle.putLong("MoneyOrPaper", MoneyOrPaper);
				bundle.putString("sid", send_sid);
				bundle.putString("key", send_key);
				bundle.putString("myResult", myResult);
				bundle.putString("selleruin", send_selleruin);
				bundle.putString("employeename", send_employeename);		
				bundle.putString("oprshopname", send_oprshopname);		
				bundle.putString("oprshopid", send_oprshopid);	
				Log.d("ttttttt", "send_selleruin" + send_selleruin);
				intent.putExtras(bundle);
				startActivity(intent);
		}
	};

	public void onPause() {
		super.onPause();

	}




}




	
	


	
	



