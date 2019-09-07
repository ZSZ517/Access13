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
		// һ��get����
		// ʹ�� OkHttp ���� Get ����ֻ��Ҫ�ĸ�����
		//
		// �½� OkHttpClient����
		// ���� Request ����
		// �� Request �����װΪ Call
		// ͨ�� Call ��ִ��ͬ�����첽����
		String url = "http://www.xxxx.com";
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).get() // Ĭ��ΪGET���󣬿��Բ�д
				.build();
		final Call call = client.newCall(request);
		// ͨ�� call.excute() �������ύͬ���������ַ�ʽ�������̣߳���Ϊ�˱��� ANR �쳣��Android3.0
		// ֮���Ѿ������������߳��з���������
		// ���� OkHttp ��ͬ�� get ������Ҫ����һ�����̣߳�

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
		// �����첽����
		// ͨ�� call.enqueue(Callback)�������ύ�첽����

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

		// ����post����
		// RequestBody��һ�������࣬���õ� RequestBody ʵ��������ô���֣�
		// 2.1 FormBody
		// FormBody��RequestBody��ʵ���࣬���ڱ���ʽ������

		OkHttpClient client1 = new OkHttpClient();
		// �������������
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
		// ��һ�������࣬���ǲ���ֱ��ʹ���������ǿ���ͨ���������ľ�̬create��������ȡһ��RequestBody���󣬸÷����ᴴ��������һ��
		// RequestBody �������ڲ���ʵ��
		// �鿴һ�� RequestBody �࣬���������������� create ������
		// ������ý���ʽ�����У�
		//
		// text/html��HTML��ʽ
		// text/pain�����ı���ʽ
		// image/jpeg��jpgͼƬ��ʽ
		// application/json��JSON���ݸ�ʽ
		// application/octet-stream�������������ݣ��糣�����ļ����أ�
		// application/x-www-form-urlencoded��form��encType���Ե�Ĭ�ϸ�ʽ�������ݽ���key/value����ʽ���͵������
		// multipart/form-data�����ϴ��ļ��ĸ�ʽ
		// ʹ�� create �����������������ϴ� String �� File ���󣬾���ʵ�����£�
		// �ϴ�JSON�ַ�����

		OkHttpClient client11 = new OkHttpClient();
		// ָ����ǰ����� contentType Ϊ json ����
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

		// �ϴ��ļ���

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
		// 2.3 ʹ��MultipartBodyͬʱ�ϴ�������������
		// ���ļ��ͼ�ֵ��ͬʱ�ϴ�

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

		// ͬ��������ʹ�����Ӧ������ͬһ�̡߳����Ҵ��̻߳�����Ӧ����֮ǰ��һֱ��������
		// �첽������ʹ�����Ӧ�����ڲ�ͬ�̡߳��������������������һ���̣߳�����ͨ���ص��ķ�ʽ�������߳̽��д�����Ӧ����һ�������̷߳����������̴߳�����Ӧ����
		// Calls�������κ��̱߳�ȡ���������Call��δִ�н���ʱ��ִ��ȡ����������ֱ�ӵ��´�Callʧ�ܣ���һ��Call��ȡ��ʱ��������д������������߶�ȡ��Ӧ����Ĵ�������������׳�һ��IOException�쳣��

	}
	
	//okhttp get����ģ��
    private void sendGetRuquestWithOkHttp() {
        //����okHttpClient����
        OkHttpClient okHttpClient=new OkHttpClient();
        //����request,����Ҫ��һ��url
        Request request=new Request.Builder().url("").build();
        //ͨ��request�Ķ���ȥ����õ�һ��Call����
        // �����ڽ���������װ�������񣬼�Ȼ�����񣬾ͻ���execute()��cancel()�ȷ�����
        Call call=okHttpClient.newCall(request);
        //���첽�ķ�ʽȥִ������,���õ���call.enqueue����call������ȶ��У�
        // Ȼ��ȴ�����ִ����ɣ�������Callback�м��ɵõ������
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //����ʧ�ܵĴ���
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //����ɹ����ؽ��
                //���ϣ�����ص����ַ���
                 final String responseData=response.body().string();
                //���ϣ�����ص��Ƕ������ֽ�����
                byte[] responseBytes=response.body().bytes();
                //���ϣ�����ص���inputStream,��inputStream���ǾͿ���ͨ��IO�ķ�ʽд�ļ�.
                InputStream responseStream=response.body().byteStream();
                //ע�⣬��ʱ���̲߳���ui�̣߳�
                // �����ʱ����Ҫ�÷��ص����ݽ���ui���£������ؼ�����Ҫʹ����ط���
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // ����UI�Ĳ���
                           // textView.setText(responseData);
                    }
                });

            }
        });
        //�����õ���enqueue���첽�ķ�ʽ����ȻҲ����ͬ����
        //ͬ��--Call��һ��execute()��������Ҳ����ֱ�ӵ���call.execute()ͨ������һ��Response��
/*    try {
    Response response = call.execute();
    if(response.isSuccessful()){
    //ͬ����ʽ�µõ����ؽ��
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
 * getName(),RequestBody.createһ��file).addFormDataPart(...) 
 * e.toString()������쳣����ʹ�����Ϣ
 * 
 * e.getMessage():��ô�����Ϣ
 * 
 * e.printStackTrace()���ڿ���̨��ӡ���쳣���࣬������Ϣ�ͳ���λ�õ�
 * 
 *���ó�ʱ,����ͨ��new OkHttpClient�����ķ�ʽ
 *  client = new OkHttpClient.Builder()  

            .connectTimeout(10, TimeUnit.SECONDS)  

            .readTimeout(20, TimeUnit.SECONDS)  

            .build();  

 */

/*Gson��ʹ�ã�Gson gson=new Gson������gson.toJson(ĳһ��object);
 * response.code()����ȡ���ص�״̬��
 * 
 * 
 * post�����content-type���ͣ�application/x-www-form-urlencoded��RequestBodyĬ���������FormBody��Ϊ�̳��ˣ�����Ҳ
 * Ĭ�����������      multipart/form-data��       application/json
 * okhttp������post����new RequestBody����{��д����ķ�����}
 * Gist gist = gson.fromJson(response.body().charStream(), Gist.class);
 */


