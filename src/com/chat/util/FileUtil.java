package com.chat.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class FileUtil {
	public static void createFile(String name,byte[] b){
		File file = new File("headImage/"+name+".jpg");
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(b);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static byte[] getVoiceBytes(String path){
		File file = new File(path);
		BufferedInputStream bin =  null;
		if(file.exists()){
			try {
				bin = new BufferedInputStream(new FileInputStream(file));
				int len;
				byte[] buffer = new byte[1024];
				while((len = bin.read(buffer)) > 0){
					
				}
				System.out.println(buffer);
				bin.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
			}
		}
		return null;
	}
	public static void saveVoiceFile(String path,byte[] voiceBuffer){
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		try {
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(file));
			bout.write(voiceBuffer);
			bout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
