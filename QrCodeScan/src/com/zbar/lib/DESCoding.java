package com.zbar.lib;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * 3DES���瑙ｅ�����宸ュ�风被
 * @author 
 *
 */
public class DESCoding
{
    
    private static final String IV = "s(2L@f!o";
    
    private String fullAlgorithm = "DESede"; // 瀹���寸�� ���瀵�绠�娉�锛�榛�璁や负DESede
    private String algorithm = "DESede"; // ���瀵�绠�娉�锛�榛�璁や负DESede
    private String mode = "ECB"; // ���瀵�妯″��锛�榛�璁�ECB
    private byte[] keyBytes = null;
    
    /**
     * ��ㄩ��璁ょ��绠�娉�DESede锛�浼���ュ����ワ��������宸ュ�风被
     * @param keyBytes     瀵���ワ��蹇�椤绘��24瀛����
     * @throws Exception
     */
    public DESCoding(byte[] keyBytes) throws Exception
    {
        if(keyBytes.length != 24)
        {
            throw new Exception("the keys's length must be 24!");
        }
        this.keyBytes = keyBytes;
    }
    
    /**
     * ��ㄦ��瀹����绠�娉�锛�浼���ュ��瀵����key锛�������宸ュ�风被
     * @param keyBytes     瀵���ワ��蹇�椤绘��24瀛����
     * @param fullAlgorithm    绠�娉�
     * @throws Exception
     */
    public DESCoding(byte[] keyBytes, String fullAlgorithm) throws Exception
    {
        if(keyBytes.length != 24)
        {
            throw new Exception("the keys's length must be 24!");
        }
        this.keyBytes = keyBytes;
        this.fullAlgorithm = fullAlgorithm;
        this.algorithm = fullAlgorithm;
        
        // 澶����杩�绉�褰㈠�����fullAlgorithm锛�DESede/CBC/PKCS5Padding
        int p = fullAlgorithm.indexOf('/');
        if(p > 0)
        {
            algorithm = fullAlgorithm.substring(0, p);
            int q = fullAlgorithm.indexOf('/', p + 1);
            if(q > 0)
            {
                mode = fullAlgorithm.substring(p + 1, q);
            }
        }
    }    
    
    /**
     * 瀵规��瀹����瀛������扮��杩�琛����瀵�
     * @param src   ���瑕�杩�琛����瀵����瀛������扮��
     * @return byte[]     ���瀵�������瀛������扮��锛���ュ��瀵�澶辫触锛����杩����null
     */    
    public byte[] encode(byte[] src) 
    {
        try 
        {
            return process(src, Cipher.ENCRYPT_MODE);
        }catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
       
    }
    
//    /**
//     * ���瀵�骞惰浆��㈡��hex Str
//     * @param src
//     * @return String
//     */
//    public String encode2HexStr(byte[] src)
//    {
//        return HexUtil.bytes2HexStr(encode(src));
//    }
//    
//    /**
//     * ���瀵�骞惰浆��㈡��BASE64缂�������瀛�绗�涓�
//     * @param src
//     * @return String
//     */
//    public String encode2Base64(byte[] src)
//    {
//        return BASE64Coding.encode(encode(src));
//    }

    /**
     * 瑙ｅ��
     * @param src   ���3DES���瀵�������瀛������扮��
     * @return byte[]     瑙ｅ��������瀛������扮��
     */
    public byte[] decode(byte[] src) 
    { 
        try 
        {
            return process(src, Cipher.DECRYPT_MODE);
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }    

    private byte[] process(byte[] src, int type) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        //  ������瀵����
        SecretKey deskey = new SecretKeySpec(this.keyBytes, algorithm);

        //  ���瀵����瑙ｅ��
        Cipher c1 = Cipher.getInstance(fullAlgorithm);
        if(mode.equalsIgnoreCase("ECB"))
        {
            c1.init(type, deskey);
        }
        else
        {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            c1.init(type, deskey, iv);
        }
        return c1.doFinal(src);
    }
    
  
    
}
