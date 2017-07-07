package com.david.mytest.utils;

import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密工具类，对于（MD5，AES，DES，文件加密）项目中使用可以拷贝相应代码即可
 * Created by weixi on 2017/7/7.
 */
public class CryptoUtils {
    /**
     * 1.MD5加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encryptMD5(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    //初始化向量，随意填写
    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    /**
     * 2.DES加密解密
     *
     * @param encryptString 明文
     * @param encryptKey    密钥
     * @return 加密后的密文
     */
    public static String encryptDES(String encryptString, String encryptKey) {
        try {
            //实例化IvParameterSpec对象，使用指定的初始化向量
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            //实例化SecretKeySpec，根据传入的密钥获得字节数组来构造SecretKeySpec
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
            //创建密码器
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            //用密钥初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            //执行加密操作
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
            return Base64.encodeToString(encryptedData, 0);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密的过程与加密的过程大致相同
     *
     * @param decryptString 密文
     * @param decryptKey    密钥
     * @return 返回明文
     */
    public static String decryptDES(String decryptString, String decryptKey) {
        try {
            //先使用Base64解密
            byte[] byteMi = Base64.decode(decryptString, 0);
            //实例化IvParameterSpec对象使用指定的初始化向量
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            //实例化SecretKeySpec，根据传入的密钥获得字节数组来构造SecretKeySpec,
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
            //创建密码器
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            //用密钥初始化Cipher对象,上面是加密，这是解密模式
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            //获取解密后的数据
            byte[] decryptedData = cipher.doFinal(byteMi);
            return new String(decryptedData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES 加密
     *
     * @param seed      密钥
     * @param cleartext 明文
     * @return 密文
     */
    public static String encryptAES(String seed, String cleartext) {
        //对密钥进行加密
        byte[] rawKey = getRawKey(seed.getBytes());
        //加密数据
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        //将十进制数转换为十六进制数
        return toHex(result);
    }


    /**
     * 3.AES 解密
     *
     * @param seed      密钥
     * @param encrypted 密文
     * @return 明文
     */
    public static String decryptAES(String seed, String encrypted) {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return result == null ? null : new String(result);
    }

    private static byte[] getRawKey(byte[] seed) {

        try {
            //获取密钥生成器
            KeyGenerator kGen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(seed);
            //生成位的AES密码生成器
            kGen.init(128, sr);
            //生成密钥
            SecretKey sKey = kGen.generateKey();
            //编码格式
            return sKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static byte[] encrypt(byte[] raw, byte[] clear) {
        try {
            //生成一系列扩展密钥，并放入一个数组中
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            //使用ENCRYPT_MODE模式，用skeySpec密码组，生成AES加密方法
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            //得到加密数据
            return cipher.doFinal(clear);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) {
        try {
            //生成一系列扩展密钥，并放入一个数组中
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            //使用DECRYPT_MODE模式，用skeySpec密码组，生成AES解密方法
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            //得到加密数据
            return cipher.doFinal(encrypted);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将十六进制字符串转为十进制字节数组
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    /**
     * 将十进制字节数组转换为十六进制
     *
     * @param buf 字节数组
     * @return 十六进制字符串
     */
    private static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (byte data : buf) {
            appendHex(result, data);
        }
        return result.toString();
    }


    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }


    /**
     * 4.文件加密解密
     * <p>
     * 加密后的文件的后缀 （用于标识是否是加密后的文件）
     */
    public static final String CIPHER_TEXT_SUFFIX = ".cipher";

    /**
     * 加解密时以32K个字节为单位进行加解密计算
     */
    private static final int CIPHER_BUFFER_LENGHT = 32 * 1024;

    /**
     * 加密，这里主要是演示加密的原理，没有用什么实际的加密算法
     *
     * @param filePath 明文文件绝对路径
     * @return 是否加密成功
     */
    public static boolean encrypt(String filePath, CipherListener listener) {
        try {
            long startTime = System.currentTimeMillis();
            File f = new File(filePath);
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            long totalLenght = raf.length();
            FileChannel channel = raf.getChannel();

            long multiples = totalLenght / CIPHER_BUFFER_LENGHT;
            long remainder = totalLenght % CIPHER_BUFFER_LENGHT;

            MappedByteBuffer buffer = null;
            byte tmp;
            byte rawByte;

            //先对整除部分加密
            for (int i = 0; i < multiples; i++) {
                buffer = channel.map(
                        FileChannel.MapMode.READ_WRITE, i * CIPHER_BUFFER_LENGHT, (i + 1) * CIPHER_BUFFER_LENGHT);

                //此处的加密方法很简单，只是简单的异或计算
                for (int j = 0; j < CIPHER_BUFFER_LENGHT; ++j) {
                    rawByte = buffer.get(j);
                    tmp = (byte) (rawByte ^ j);
                    buffer.put(j, tmp);

                    if (null != listener) {
                        listener.onProgress(i * CIPHER_BUFFER_LENGHT + j, totalLenght);
                    }
                }
                buffer.force();
                buffer.clear();
            }

            //对余数部分加密
            buffer = channel.map(
                    FileChannel.MapMode.READ_WRITE, multiples * CIPHER_BUFFER_LENGHT, multiples * CIPHER_BUFFER_LENGHT + remainder);

            for (int j = 0; j < remainder; ++j) {
                rawByte = buffer.get(j);
                tmp = (byte) (rawByte ^ j);
                buffer.put(j, tmp);

                if (null != listener) {
                    listener.onProgress(multiples * CIPHER_BUFFER_LENGHT + j, totalLenght);
                }
            }
            buffer.force();
            buffer.clear();

            channel.close();
            raf.close();

            //对加密后的文件重命名，增加.cipher后缀
            f.renameTo(new File(f.getPath() + CIPHER_TEXT_SUFFIX));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 解密，这里主要是演示加密的原理，没有用什么实际的加密算法
     *
     * @param filePath 密文文件绝对路径，文件需要以.cipher结尾才会认为其实可解密密文
     * @return 是否解密成功
     */
    public static boolean decrypt(String filePath, CipherListener listener) {
        try {
            long startTime = System.currentTimeMillis();
            File f = new File(filePath);
            if (!f.getPath().toLowerCase().endsWith(CIPHER_TEXT_SUFFIX)) {
                //后缀不同，认为是不可解密的密文
                return false;
            }

            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            long totalLenght = raf.length();
            FileChannel channel = raf.getChannel();

            long multiples = totalLenght / CIPHER_BUFFER_LENGHT;
            long remainder = totalLenght % CIPHER_BUFFER_LENGHT;

            MappedByteBuffer buffer = null;
            byte tmp;
            byte rawByte;

            //先对整除部分解密
            for (int i = 0; i < multiples; i++) {
                buffer = channel.map(
                        FileChannel.MapMode.READ_WRITE, i * CIPHER_BUFFER_LENGHT, (i + 1) * CIPHER_BUFFER_LENGHT);

                //此处的解密方法很简单，只是简单的异或计算
                for (int j = 0; j < CIPHER_BUFFER_LENGHT; ++j) {
                    rawByte = buffer.get(j);
                    tmp = (byte) (rawByte ^ j);
                    buffer.put(j, tmp);

                    if (null != listener) {
                        listener.onProgress(i * CIPHER_BUFFER_LENGHT + j, totalLenght);
                    }
                }
                buffer.force();
                buffer.clear();
            }

            //对余数部分解密
            buffer = channel.map(
                    FileChannel.MapMode.READ_WRITE, multiples * CIPHER_BUFFER_LENGHT, multiples * CIPHER_BUFFER_LENGHT + remainder);

            for (int j = 0; j < remainder; ++j) {
                rawByte = buffer.get(j);
                tmp = (byte) (rawByte ^ j);
                buffer.put(j, tmp);

                if (null != listener) {
                    listener.onProgress(multiples * CIPHER_BUFFER_LENGHT + j, totalLenght);
                }
            }
            buffer.force();
            buffer.clear();

            channel.close();
            raf.close();

            //对加密后的文件重命名，增加.cipher后缀
//      f.renameTo(new File(f.getPath().substring(f.getPath().toLowerCase().indexOf(CIPHER_TEXT_SUFFIX))));

            Log.d("解密用时：", (System.currentTimeMillis() - startTime) / 1000 + "s");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 用于加解密进度的监听器
     */
    public interface CipherListener {
        void onProgress(long current, long total);
    }

}
