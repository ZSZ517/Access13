package com.example.access1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LearningActivity extends Activity {
	String TAG;

	protected void onCreate(Bundle bundle) {
		// 一：get请求
		// 使用 OkHttp 进行 Get 请求只需要四个步骤
		//
		// 新建 OkHttpClient对象
		// 构造 Request 对象
		// 将 Request 对象封装为 Call
		// 通过 Call 来执行同步或异步请求
		String url = "http://www.xxxx.com";
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).get() // 默认为GET请求，可以不写
				.build();
		final Call call = client.newCall(request);
		// 通过 call.excute() 方法来提交同步请求，这种方式会阻塞线程，而为了避免 ANR 异常，Android3.0
		// 之后已经不允许在主线程中访问网络了
		// 所以 OkHttp 的同步 get 请求需要开启一个子线程：

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response response = call.execute();
					Log.d(TAG, response.body().toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// 或者异步请求
		// 通过 call.enqueue(Callback)方法来提交异步请求

		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.d(TAG, "onFailure: " + e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Log.d(TAG, "OnResponse: " + response.body().toString());
			}
		});

		// 二、post请求
		// RequestBody是一个抽象类，常用的 RequestBody 实现类有这么几种：
		// 2.1 FormBody
		// FormBody是RequestBody的实现类，用于表单方式的请求

		OkHttpClient client1 = new OkHttpClient();
		// 创建表单请求参数
		FormBody.Builder builder = new FormBody.Builder();
		builder.add("name", "zhangsan");
		builder.add("age", "18");
		FormBody formBody = builder.build();
		Request request1 = new Request.Builder().url(url).post(formBody).build();
		client1.newCall(request1).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
			}
		});
		// 2.2 RequestBody.create(...)
		// RequestBody
		// 是一个抽象类，我们不能直接使用它，但是可以通过调用它的静态create方法来获取一个RequestBody对象，该方法会创建并返回一个
		// RequestBody 的匿名内部类实例
		// 查看一下 RequestBody 类，发现它有这样几个 create 方法。
		// 常见的媒体格式类型有：
		//
		// text/html：HTML格式
		// text/pain：纯文本格式
		// image/jpeg：jpg图片格式
		// application/json：JSON数据格式
		// application/octet-stream：二进制流数据（如常见的文件下载）
		// application/x-www-form-urlencoded：form表单encType属性的默认格式，表单数据将以key/value的形式发送到服务端
		// multipart/form-data：表单上传文件的格式
		// 使用 create 方法可以用来用于上传 String 和 File 对象，具体实现如下：
		// 上传JSON字符串：

		OkHttpClient client11 = new OkHttpClient();
		// 指定当前请求的 contentType 为 json 数据
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		String jsonStr = "{\"name\":\"zhangsan\",\"age\":\"20\"}";
		RequestBody body=RequestBody.create(JSON, jsonStr);
		Request request11 = new Request.Builder().url(url).post(body).build();
		client11.newCall(request11).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
			}
		});

		// 上传文件：

		OkHttpClient client111 = new OkHttpClient();
		File file = new File("d:\\a");
		Request request111 = new Request.Builder().url(url)
				.post(RequestBody.create(MediaType.parse("application/octet-stream"), file)).build();
		client11.newCall(request111).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
			}
		});
		// 2.3 使用MultipartBody同时上传多种类型数据
		// 多文件和键值对同时上传

		OkHttpClient client2 = new OkHttpClient();
		MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("name", "zhangsan").addFormDataPart("age", "20").addFormDataPart("file",
						file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
				.build();

		Request request2 = new Request.Builder().url(url).post(multipartBody).build();
		client.newCall(request2).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
			}
		});

		// 同步：请求和处理响应发生在同一线程。并且此线程会在响应返回之前会一直被堵塞。
		// 异步：请求和处理响应发生在不同线程。将发送请求操作发生在一个线程，并且通过回调的方式在其他线程进行处理响应。（一般在子线程发送请求，主线程处理响应）。
		// Calls可以在任何线程被取消。当这个Call尚未执行结束时，执行取消操作将会直接导致此Call失败！当一个Call被取消时，无论是写入请求主体或者读取响应主体的代码操作，都会抛出一个IOException异常。

	}
	
	//okhttp get请求模板
    private void sendGetRuquestWithOkHttp() {
        //创建okHttpClient对象
        OkHttpClient okHttpClient=new OkHttpClient();
        //创建request,首先要有一个url
        Request request=new Request.Builder().url("").build();
        //通过request的对象去构造得到一个Call对象，
        // 类似于将你的请求封装成了任务，既然是任务，就会有execute()和cancel()等方法。
        Call call=okHttpClient.newCall(request);
        //以异步的方式去执行请求,调用的是call.enqueue，将call加入调度队列，
        // 然后等待任务执行完成，我们在Callback中即可得到结果。
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败的处理
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功返回结果
                //如果希望返回的是字符串
                 final String responseData=response.body().string();
                //如果希望返回的是二进制字节数组
                byte[] responseBytes=response.body().bytes();
                //如果希望返回的是inputStream,有inputStream我们就可以通过IO的方式写文件.
                InputStream responseStream=response.body().byteStream();
                //注意，此时的线程不是ui线程，
                // 如果此时我们要用返回的数据进行ui更新，操作控件，就要使用相关方法
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 更新UI的操作
                           // textView.setText(responseData);
                    }
                });

            }
        });
        //上面用到的enqueue是异步的方式，当然也可以同步，
        //同步--Call有一个execute()方法，你也可以直接调用call.execute()通过返回一个Response。
/*    try {
    Response response = call.execute();
    if(response.isSuccessful()){
    //同步方式下得到返回结果
    String responseByExecute=response.body().string();
    }
     } catch (IOException e) {
          e.printStackTrace();
      }*/
    }
	
}
/*
 * 1.formbody.builder.add(key-value).add(...).build()
 * 2.requestbody.create(MIMEtype,file/string/...)
 * 3.multipartbody.builder.setType(MultipartBody.FORM).addFormDataPart(key,file.
 * getName(),RequestBody.create一个file).addFormDataPart(...) 
 * e.toString()：获得异常种类和错误信息
 * 
 * e.getMessage():获得错误信息
 * 
 * e.printStackTrace()：在控制台打印出异常种类，错误信息和出错位置等
 * 
 *设置超时,不能通过new OkHttpClient（）的方式
 *  client = new OkHttpClient.Builder()  

            .connectTimeout(10, TimeUnit.SECONDS)  

            .readTimeout(20, TimeUnit.SECONDS)  

            .build();  

 */

/*Gson简单使用：Gson gson=new Gson（）；gson.toJson(某一个object);
 * response.code()：获取返回的状态码
 * 
 * 
 * post请求的content-type类型：application/x-www-form-urlencoded【RequestBody默认是这个，FormBody因为继承了，所以也
 * 默认是这个】，      multipart/form-data，       application/json
 * okhttp还可以post流：new RequestBody（）{重写里面的方法。}
 * Gist gist = gson.fromJson(response.body().charStream(), Gist.class);
 */


