package com.example.access1.activity;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

import com.example.access1.R;

public class ShowWaitingProgress {
	private Dialog progressDialog;
	private Activity activity;
	public void show(Activity activity,String string) {
		this.activity=activity;
		progressDialog = new Dialog(activity, R.style.progress_dialog);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setContentView(R.layout.waiting_dialog);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
		msg.setText(string);
		progressDialog.show();
	}
	public Dialog getWaitingDialog() {
		return progressDialog;
	}
	public void dismiss() {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {

				progressDialog.dismiss();
			}
		});
	}
}
