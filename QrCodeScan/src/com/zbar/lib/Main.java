package com.zbar.lib;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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

import android.view.KeyEvent;



public class Main extends Activity {
	private Button view_loginSubmit;
	private Button view_Set;
	private Button view_suggest;
    private Button view_money;
	
	String  send_selleruin;
	String  send_employeename;
	String  send_oprshopname;
	String  send_oprshopid;
	long 	send_sin;
	String  send_sid;
	String 	send_key;
	
	long mExitTime = 0;
	
	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Main.this, CaptureActivity.class);
			
			Bundle bundle = new Bundle();
			
			bundle.putString("selleruin", send_selleruin);
			bundle.putString("employeename", send_employeename);		
			bundle.putString("oprshopname", send_oprshopname);		
			bundle.putString("oprshopid", send_oprshopid);	
			bundle.putLong("sin", send_sin);
			bundle.putString("sid", send_sid);
			bundle.putString("key", send_key);
			bundle.putLong("MoneyOrPaper", 0);			
			intent.putExtras(bundle);

		    Log.d("ttttttt" , "333333  sin=" + send_sin + "   " + "sid =" + send_sid 
		          + "key = " + send_key );			
			
			startActivity(intent);
		}
	};

	private OnClickListener setListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Main.this, CameraCheckAboutActivity.class);
			Bundle bundle = new Bundle();
			
			bundle.putLong("sin", send_sin);
			bundle.putString("sid", send_sid);
			bundle.putString("key", send_key);
			intent.putExtras(bundle);			
			
			startActivity(intent);
		}
	};
	
	
		private OnClickListener suggestListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Main.this, SuggestActivity.class);
			Bundle bundle = new Bundle();
			
			bundle.putLong("sin", send_sin);
			bundle.putString("sid", send_sid);
			bundle.putString("key", send_key);
			intent.putExtras(bundle);			
			
			startActivity(intent);
		}
	};
	
	private OnClickListener moneyListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(Main.this, CaptureActivity.class);
		
		Bundle bundle = new Bundle();
		
		bundle.putString("selleruin", send_selleruin);
		bundle.putString("employeename", send_employeename);		
		bundle.putString("oprshopname", send_oprshopname);		
		bundle.putString("oprshopid", send_oprshopid);	
		bundle.putLong("sin", send_sin);
		bundle.putString("sid", send_sid);
		bundle.putString("key", send_key);
		bundle.putLong("MoneyOrPaper", 1);
		intent.putExtras(bundle);

	    Log.d("ttttttt" , "333333  sin=" + send_sin + "   " + "sid =" + send_sid 
	          + "key = " + send_key );			
		
		startActivity(intent);
	}
};
	
private Bitmap createCircleImage(Bitmap source, int min)  
{  
    final Paint paint = new Paint();  
    paint.setAntiAlias(true);  
    Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);  
    /** 
     * 产生一个同样大小的画布 
     */  
    Canvas canvas = new Canvas(target);  
    /** 
     * 首先绘制圆形 
     */  
    canvas.drawCircle(min / 2, min / 2, min / 2, paint);  
    /** 
     * 使用SRC_IN 
     */  
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  
    /** 
     * 绘制图片 
     */  
    canvas.drawBitmap(source, 0, 0, paint);  
    return target;  
}  

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		if (Util.scryDP < 600)
		{
			setContentView(R.layout.main_small);
		}
		else
			setContentView(R.layout.main);
		//findViewsById();
		//initView(false);
		// 需要去submitListener里面设置URL
		//setListener();
		bundle = this.getIntent().getExtras();
		
		send_selleruin = bundle.getString("selleruin");
		send_employeename = bundle.getString("oprshopname");
		send_oprshopname = bundle.getString("employeename");;
		send_oprshopid= bundle.getString("oprshopid");
		send_sin= bundle.getLong("sin");
		send_sid= bundle.getString("sid");
		send_key= bundle.getString("key");
		
		Log.d("ttttttt" , "222222  sin=" + send_sin + "   " + "sid =" + send_sid 
		          + "key = " + send_key );
		
		// 扫描按钮
		TextView tv1 = (TextView) this.findViewById(R.id.welcome);
		tv1.setText("马上有钱");

		TextView tv2 = (TextView) this.findViewById(R.id.TextView01);
		tv2.setText("  " + bundle.getString("oprshopname"));
		
		TextView tv3 = (TextView) this.findViewById(R.id.textView1);
		tv3.setText("    " + bundle.getString("employeename"));		
		
		byte[] myget = bundle.getByteArray("oprshoplogo");
		try{
		
			Bitmap bitmap = BitmapFactory.decodeByteArray(myget, 0, myget.length); 
			Bitmap newbitmap = null;
			Log.d("ttttttt", "bitmap.width = " + bitmap.getWidth());
			if (bitmap.getWidth() < 200)
				newbitmap = bitmap;
			else
				newbitmap = createCircleImage(bitmap, (int) (130 * Util.density));
			ImageView imageView = (ImageView) this.findViewById(R.id.imageView1);
	        imageView.setImageBitmap(newbitmap);     
		}
		catch (Exception e)
		{
		
		}
		
		view_loginSubmit = (Button)findViewById(R.id.certibutton);
		view_loginSubmit.setOnClickListener(submitListener);
		
		
		view_Set = (Button)findViewById(R.id.setbutton);
		view_Set.setOnClickListener(setListener);
		
		view_suggest= (Button)findViewById(R.id.todobutton);
		view_suggest.setOnClickListener(suggestListener);

		view_money = (Button)findViewById(R.id.moneyorpaperbutton);
		view_money.setOnClickListener(moneyListener);
		
	}


        public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if ((System.currentTimeMillis() - mExitTime) > 2000) {
                                Object mHelperUtils;
                                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                                mExitTime = System.currentTimeMillis();

                        } else {
                                finish();
                        }
                        return true;
                }
                return super.onKeyDown(keyCode, event);
        }



}



