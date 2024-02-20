package com.example.util;

import java.util.Random;

public class VerificationCodeUtil {

    public static String getRandomCode(){
        Random random=new Random();
        return String.valueOf(random.nextInt(100000,999999)+1);
    }
}
