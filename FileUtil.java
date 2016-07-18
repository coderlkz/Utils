/**
 * 
 */
package com.tongtong.ttmall.common;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 升级安装包存放地址
 * @author Ganker
 *
 */
public class FileUtil {
	
	public static File updateDir = null;
	public static File updateFile = null;
	/***********保存升级APK的目录***********/
	public static final String TTYP = "ttyp";
	
	public static boolean isCreateFileSucess;

	/** 
	* 方法描述：createFile方法
	*/
	public static void createFile(String app_name) {
		
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			isCreateFileSucess = true;
			
			updateDir = new File(Environment.getExternalStorageDirectory()+ "/" + TTYP +"/");
			updateFile = new File(updateDir + "/" + app_name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					isCreateFileSucess = false;
					e.printStackTrace();
				}
			}

		}else{
			isCreateFileSucess = false;
		}
	}
}
