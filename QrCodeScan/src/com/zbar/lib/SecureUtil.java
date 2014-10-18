package com.zbar.lib;

import android.annotation.SuppressLint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;




public class SecureUtil {
    private static final byte[] keys = "%$#(*N@520PL><NsMvMKhsO*".getBytes();
    private static DESCoding des = null; 
    static
    {
    	try {
    		des = new DESCoding(keys);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    //缂虹�����瀵�
    public static byte[] encode( byte[] bs ){
		byte[] encodeBytes = des.encode( bs );
		return encodeBytes;
    }
    
    //缂虹��瑙ｅ��
    public static byte[] decode( byte[] bs  ){
    	byte[] bs2 = des.decode( bs );
    	return bs2;
    }
    
    //ukey���瀵�
    @SuppressLint("NewApi")
	public static byte[] encodeWithUkey( byte[] bs, String ukey ){
    	try{
    		byte[] encodeBytes = new DESCoding( ukey.getBytes( Charset.forName("UTF-8") ) ).encode( bs );
    		return encodeBytes;
    	}catch( Exception e ){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    //ukey瑙ｅ��
    @SuppressLint("NewApi")
	public static byte[] decodeWithUkey( byte[] bs, String ukey  ){
    	try {
        	byte[] bs2 = new DESCoding( ukey.getBytes( Charset.forName("UTF-8") ) ).decode( bs );
        	return bs2;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
	private static void readAll(InputStream is,byte[] bs,int start,int end) throws IOException {
		while(start<end){
			int bytesRead = is.read(bs,start,end-start);
			if (bytesRead < 0)
				throw new IOException("close by peer.");
			start += bytesRead;
		}
	}
	
	public  static byte[] readBody( String name ) throws IOException {
		File f = new File( name );
		int len = (int)f.length();
		byte[] bs = new byte[ len ];
		readAll( new FileInputStream( f ), bs, 0, bs.length );
		return bs;
	}
	
    public static void main(String[] args) throws Exception{
//		String fname = "C:\\Users\\rocker\\318\\wandian\\webapp_mc\\WEB-INF\\doc\\register.txt";
//		String sname = "C:\\Users\\rocker\\318\\wandian\\webapp_mc\\WEB-INF\\doc\\register.txt.enc";
//		byte[] bs = readBody( fname );
//		FileOutputStream out =  new FileOutputStream( sname );
//		out.write( encode(bs) );
//		out.close();
		
		String fname = "C:\\Users\\rocker\\318\\wandian\\webapp_mc\\WEB-INF\\doc\\getcompanylist.txt";
		String sname = "C:\\Users\\rocker\\318\\wandian\\webapp_mc\\WEB-INF\\doc\\getcompanylist.txt.enc";
		byte[] bs = readBody( fname );
		FileOutputStream out =  new FileOutputStream( sname );
		//out.write( encodeWithUkey(bs, "v0GM77fISXHbNWwkNqkIR6jq") );
		out.close();
		
	}
}