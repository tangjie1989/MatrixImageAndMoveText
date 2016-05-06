package com.tj.matriximageandmovetext.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;


public class SoftInputUtils {
	
	//打开软键盘
	public static void openInput(final Context ctx,EditText etx){
		
		Timer timer = new Timer();  
        timer.schedule(new TimerTask() {  
            @Override  
            public void run() {  
            	InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);  
        		//得到InputMethodManager的实例
        		if (imm.isActive()) {
        			//如果开启
        			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS); 
        			//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        		}
            }  
        }, 100); 
	}   
	
	//关闭软键盘
	public static void closeInput(Context ctx,EditText etx){
		InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);     
        imm.hideSoftInputFromWindow(etx.getWindowToken(), 0);
	}
}  
