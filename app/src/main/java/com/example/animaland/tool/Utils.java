package com.example.animaland.tool;

import static java.util.regex.Pattern.compile;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


    //正则匹配手机号码
    public static boolean checkTel(String tel){
        Pattern p = compile("^[1][3,4,5,7,8,9][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }

    //
    public static boolean isContainAll(String s){
        boolean isDigit =false;
        boolean isLowerCase = false;
        boolean isUpperCase = false;
        for(int i = 0;i < s.length();i++) {
            if(Character.isDigit(s.charAt(i))) {
                isDigit = true;
            }else if(Character.isLowerCase(s.charAt(i))){
                isLowerCase = true;
            }else if(Character.isUpperCase(s.charAt(i))){
                isUpperCase = true;
            }
        }
        String regex ="^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLowerCase &&isUpperCase && s.matches(regex);
        return isRight;
    }

    public String random(){//生成随机的四位字符
        int i=(int) (Math.random()*9000+1000);//random:随机选一个0-1的小数
        String ran = i +"";
        return ran;
    }

    public String random2(){//生成随机的3位字符
        int i=(int) (Math.random()*900+100);//random:随机选一个0-1的小数
        String ran = i +"";
        return ran;
    }

    public String shellId(){//海底id
        return "3"+ random();
    }

    public  String treeId(){//树屋id
        return "2"+ random();
    }

    public String undergroundId(){//地底id
        return "1"+ random();
    }

    public static boolean isNumberic(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public boolean checkRoomName(String s){//检验房间名字是否合格
        String CHINESE_LETTER_DIGIT_REGEX = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
        if(s.length()>=2 && s.length()<=10){//判断是否在2-10之间
            if(s.matches(CHINESE_LETTER_DIGIT_REGEX)){//只准出现英文 中文和数字
                return true;
            }
        }
        return false;
    }

    public boolean checkRoomPwd(String s){//如果只有大小写或数字且为4为
        String LETTER_DIGIT_REGEX = "^[a-z0-9A-Z]+$";
        if(s.length()==4){
            if(s.matches(LETTER_DIGIT_REGEX))
                return true;
        }
        return false;
    }

    public String livingId(){
        return random();
    }

    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     */
    public int[] randomCommon(int min, int max, int n){
        int len = max-min+1;

        if(max < min || n > len){
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min+len; i++){
            source[i-min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;

    }


    public boolean isFee(String s){


        if(s.equals(""))
            return false;
        else {
            int i= Integer.parseInt(s);
            if ((i > 0) && (i < 11))
                return true;
        }
        return false;

    }




}

