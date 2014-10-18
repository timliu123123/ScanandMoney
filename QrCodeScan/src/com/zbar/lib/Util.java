package com.zbar.lib;

import com.zbar.lib.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

public final class Util {

	//static final String mainUrl = "http://203.195.202.12:8060/WwbzCoupon/hexiao/api/main";
	//static final String ssoUrl = "http://203.195.202.12:8060/WwbzCoupon/hexiao/api/sso";

	public static final String mainUrl = "http://m.318318.cn/WwbzCoupon/hexiao/api/main";
	public static final String ssoUrl = "http://m.318318.cn/WwbzCoupon/hexiao/api/sso";	
	
	static int screenWidth = 1280;
	static int screenHeight = 1920;
	static float density = 3;
	static float xdpi = 360;
	static float ydpi = 640;
	static float scrxDP = xdpi;
	public static float scryDP = ydpi;
	
	public static void getDisplayMetrics(Context cx) {
		String str = "";
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		density = dm.density;
		xdpi = dm.xdpi;
		ydpi = dm.ydpi;
		Log.d("tttttt", "xdpi=" + xdpi + "ydi = " + ydpi + "dm.density=" + dm.density);
		
		scrxDP = (screenWidth / density);
		scryDP = (screenHeight/density);
		
		Log.d("tttttt", "screenWidth=" + screenWidth + " ßscreenHeight = " + screenHeight);
		
		Log.d("tttttt", "scrxDP=" + scrxDP + " scryDP = " + scryDP);
		
	}
	
	public static String changeMainString(Context mycontext, String mainBody)
	{
		String strBook = mainBody;

        String mybook = "{\"body\":{\"selleruin\":\"10029\",\"couponid\":\"0025199992554\",\"oprshopid\":\"78\",\"oprshopname\":\"sfsdfs\",\"oprshopclerkname\":\"\"},\"reqhead\":{\"sdkversion\":\"17\",\"cmd\":\"couponuse\",\"cpuserial\":\"4b9760af4d0012c2\",\"devicename\":\"Lenovot-A789\",\"guid\":\"AEDFE29A52AA44EB9A0CDEA6157B7BD9\",\"mac\":\"40:F3:08:F4:6C:84\",\"imei\":\"358851054502087\",\"imsi\":\"460018828603788\",\"uin\":3,\"nettype\":3,\"ostype\":1,\"appcode\":1,\"seq\":3,\"height\":1920,\"width\":1080}}";

        TelephonyManager tm = (TelephonyManager)mycontext.getSystemService(Context.TELEPHONY_SERVICE);  
        
        strBook = mainBody.replaceAll("\"sdkversion\":\"17\"", "\"sdkversion\":" + "\"" + 19 + "\"");
        
		int verCode = 1;
		try{
			verCode = mycontext.getPackageManager().getPackageInfo("com.php", 0).versionCode;	
		}catch(Exception e){
			Log.d("ttttt", "get version error" + e.getMessage());
		}
        

        strBook = strBook.replaceAll("\"appcode\":1", "\"appcode\":" + String.valueOf(verCode));
        
        //strBook = mainBody.replaceAll("358851054502087",  tm.getDeviceId() );  //imei
        strBook = strBook.replaceAll("Lenovot-A789",   android.os.Build.BRAND);  //device name
        //strBook = mainBody.replaceAll("460018828603788",   tm.getSubscriberId());  //imsi
        strBook = strBook.replaceAll("1080",   String.valueOf(screenWidth));  //imsi
        strBook = strBook.replaceAll("1920",   String.valueOf(screenHeight));  //imsi      
		return strBook;
	}
	
	public static String returnErrorMessage(long response_code)
	{
		String message1 = "";
		long mycode = response_code;
		if (mycode == 256)
			message1 = "核销异常，请重试";
		if (mycode == 513)
			message1 = "系统错误";
		if (mycode == 514)
			message1 = "请求参数错误";
		if (mycode == 515)
			message1 = "您输入的优惠券格式错误";
		if (mycode == 516)
			message1 = "没有找到该优惠券的批次信息";
		if (mycode == 517)
			message1 = "没有找到优惠券。请确认优惠码是否正确";								
		if (mycode == 518)
			message1 = "抱歉优惠券发放失败";
		if (mycode == 519)
			message1 = "抱歉，优惠券核销失败";
		if (mycode == 520)
			message1 = "无效的优惠券批次";	
		if (mycode == 521)
			message1 = "本优惠券不适用当前门店";
		if (mycode == 528)
			message1 = "优惠券状态无效，请确认是否已被使用";								
		if (mycode == 529)
			message1 = "优惠券不在有效期内，无法使用";
		if (mycode == 530)
			message1 = "本优惠券已绑定，只能被所有者使用";
		if (mycode == 769)
			message1 = "没有找到对应的门店信息";			
		return message1;
	}
	
	
}
