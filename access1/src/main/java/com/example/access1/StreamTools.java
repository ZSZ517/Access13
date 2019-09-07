package com.example.access1;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
public class StreamTools {
 
	public static String readString(InputStream in) throws Exception {
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		int len=-1;
		byte[]buffer=new byte[1024];//1kb
		while ((len=in.read(buffer))!=-1) {
			baos.write(buffer,0,len);			
		}
		in.close();
		String content=new String(baos.toByteArray());	
		return content;	
	}
}