package com.redis_study.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 长url转换成短url的生成器,算法解析
 */
public class ShortUrlGenerator {

    //26+10+10=62，最大下標為61
    public static  final  String[]  chars=new String[]{
     "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
     "0","1","2","3","4","5","6","7","8","9",
     "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
    };


   public static  String[] shortUrl(String longUrl){


       String key="";
       //對地址進行MD5加密
       String Md5EncryptMessage= DigestUtils.md5Hex(key+longUrl);
       String hex=Md5EncryptMessage;

       String[] resUrl=new String[4];

       for (int i = 0; i < 4; i++) {
           //取出8位字符串。將原來的32位MD5的字符串分割成4組，每組8位字符
           String tempSubstr=hex.substring(i*8,(i+1)*8);

           //將tempSubstr转化成16进制的数据与0X3FFFFFFF进行运算，为了格式化截取前30位的数据
           long lHexLog=0X3FFFFFFF&Long.parseLong(tempSubstr,16);

           String outChars="";
           for (int j = 0; j <6 ; j++) {
               //0x0000003D（111101 2進制） 10進制的數據為61,61為chars的最大下標，0x0000003D與lHexLog進行與運算，格式為6位，確保index的值為61以內的數字，下標不會越界
               long index=0x0000003D&lHexLog;
               outChars+=chars[(int)index];
               //每次循環按位右移5位。30位的二進制數據，循環6次，每次移動5位
               lHexLog=lHexLog>>5;
           }
           resUrl[i]=outChars;
       }

       return resUrl;
   }

}
