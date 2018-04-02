package com.hnair.consumer.wallet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TTTT {
	public static void main(String[] args) {
		Double amount = 33.33;
		Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");  
        //被校验的字符串  
        Matcher match=pattern.matcher(String.valueOf(amount.doubleValue()));  
        System.out.println(match.matches());
	}
}
