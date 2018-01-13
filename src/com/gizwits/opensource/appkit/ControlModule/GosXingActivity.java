package com.gizwits.opensource.appkit.ControlModule;


import java.util.concurrent.ConcurrentHashMap;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.gokit.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GosXingActivity extends GosControlModuleBaseActivity implements OnClickListener{

	private GizWifiDevice mDevice;
	private ConcurrentHashMap<String, Object> deviceStatu;
	private String title;
	
	//设置按钮状态总统变量
	private Boolean tv0;
	private Boolean tv1;
	private Boolean tv2;
	private Boolean tv3;
	//数据信息
	private static final String KEY_0 = "kay_0";
	private static final String KEY_1 = "kay_1";
	private static final String KEY_2 = "kay_2";
	private static final String KEY_3 = "kay_3";
	
	private Button but0;
	private Button but1;
	private Button but2;
	private Button but3;
	private TextView textView0;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	
	private ImageView b;
	
	private EditText eT;
	private LinearLayout eTly;
	private Button eTbut;
	private String eTst;
	
	protected static final int TOAST = 0;
	protected static final int UPDATE_UI = 2;
	protected static final int LOG = 3;
	/** 连接断开 提示 */
	protected static final int DISCONNECT = 6;
	
	private Runnable mRunnable = new Runnable() {
		public void run() {
			if (isDeviceCanBeControlled()) {
				progressDialog.cancel();
			} else {
				toastDeviceNoReadyAndExit();
			}
		}
	};
	
	
	//消息机制
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case UPDATE_UI:
				//更新ui  ui跟新不能再主线程内
				updateUI();
				break;
				case DISCONNECT:
				toastDeviceDisconnectAndExit();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhinengdeng);
		//跳转接受参数
		initDevice();
		setActionBar(true, true, title);
		
		intiView();
		intiOn();
		
		mDevice.setListener(gizWifiDeviceListener);
	}
	
	//事件处理  处理长按事件 修改名字
	private void intiOn() {
		// TODO Auto-generated method stub
		//按钮长按 修改名字
		but0.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				//设置布局为可见性
				eTly.setVisibility(View.VISIBLE);
				//设置输入框焦点
				eT.requestFocus();
				//获得输入框后弹出键盘
				InputMethodManager inputManager = (InputMethodManager)eT.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
				inputManager.showSoftInput(eT, 0); 
				return true;
			}
		});
		
		
	}

	private void intiView() {
		// TODO Auto-generated method stub
		
		
		//按钮控件 实现上传数据
		but0 = (Button) findViewById(R.id.but0);
		but1 = (Button) findViewById(R.id.but1);
		but2 = (Button) findViewById(R.id.but2);
		but3 = (Button) findViewById(R.id.but3);
		
		//文本控件 实现坚持数据（true/false） 分别显示 开启与关闭
		textView0 = (TextView) findViewById(R.id.textView0);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		
		//图片控件 检测故障实现跟换图片
		b = (ImageView) findViewById(R.id.b);
		
		//布局输入框
		eTly = (LinearLayout) findViewById(R.id.eTly);
		eT = (EditText) findViewById(R.id.eT);
		eTbut = (Button) findViewById(R.id.eTbut);
		
		//添加按钮点击事件
		but0.setOnClickListener(this);
		but1.setOnClickListener(this);
		but2.setOnClickListener(this);
		but3.setOnClickListener(this);
		eTbut.setOnClickListener(this);
		
	}
	
	
	/**
	 * 发送指令
	 * 
	 * @param key
	 *            数据点对应的标识名
	 * @param value
	 *            需要改变的值
	 */
	private void sendCommand(String key, Object value) {
		int sn = 5;
		ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
		hashMap.put(key, value);
		mDevice.write(hashMap, sn);
		Log.i("Apptest", hashMap.toString()+"发送");
	}
	
	

    //跳转传参
	private void initDevice() {
		Intent intent = getIntent();
		mDevice = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
		deviceStatu = new ConcurrentHashMap<String, Object>();

		if (TextUtils.isEmpty(mDevice.getAlias())) {
			title = mDevice.getProductName();
		} else {
			title = mDevice.getAlias();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(tv0 == null)
		{
			Toast.makeText(this, "null == tv0", Toast.LENGTH_LONG).show();
		}
		
		switch (v.getId()) {
		case R.id.but0:
			sendCommand(KEY_0, tv0);
			if(tv0 == true){
				textView0.setText("开启");
			}
			else{
				textView0.setText("关闭");
			}
			tv0 = !tv0;
			break;
		case R.id.but1:
			sendCommand(KEY_1, tv1);
			if(tv1 == true){
				textView1.setText("开启");
			}
			else{
				textView1.setText("关闭");
			}
			tv1 = !tv1;
			break;
		case R.id.but2:
			sendCommand(KEY_2, tv2);
			if(tv2 == true){
				textView2.setText("开启");
			}
			else{
				textView2.setText("关闭");
			}
			tv2 = !tv2;
			break;
		case R.id.but3:
			sendCommand(KEY_3, tv3);
			if(tv3 == true){
				textView3.setText("开启");
			}
			else{
				textView3.setText("关闭");
			}
			tv3 = !tv3;
			break;
			//输入框确定按钮  设置名字
		case R.id.eTbut:
			eTst = eT.getText().toString();
			if(eTst == null){
				if(but0.getText().toString() == null)
				{
					Toast.makeText(this, "请为他命名", Toast.LENGTH_LONG).show();
				}
				but0.setText(but0.getText().toString());
			}
			else{
				but0.setText(eTst);
			}
			
			//为布局设置看见性属性  为不可见
			eTly.setVisibility(View.INVISIBLE);
			//设置输入框取消获取焦点
			eT.clearFocus();
			//隐藏键盘
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);     
			imm.hideSoftInputFromWindow(eT.getWindowToken(), 0);  
		default:
			break;
		}
	}
	
	/*
	 * 设置设备信息回调
	 */
	protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
		Log.i("Apptest","设备信息回调！！！".toString());
		progressDialog.cancel();
		Message msg = new Message();
		msg.what = TOAST;
		String toastText;
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
			toastText = (String) getText(R.string.set_info_successful);
		} else {
			toastText = toastError(result);
		}
		msg.obj = toastText;
		handler.sendMessage(msg);
	}

	
	//更新界面
		private void updateUI() {
			
			if((Boolean) deviceStatu.get(KEY_0) == true){
				textView0.setText("开启");
				tv0 = false;
				Toast.makeText(this, !tv0+"yu"+tv0, Toast.LENGTH_SHORT).show();
			}
			else{
				textView0.setText("关闭");
				tv0 = true;
			}
			if((Boolean) deviceStatu.get(KEY_1) == true){
				textView1.setText("开启");
				tv1 = false;
			}
			else{
				textView1.setText("关闭");
				tv1 = true;
			}
			if((Boolean) deviceStatu.get(KEY_2) == true){
				textView2.setText("开启");
				tv2 = false;
			}
			else{
				textView2.setText("关闭");
				tv2 = true;
			}
			if((Boolean) deviceStatu.get(KEY_3) == true){
				textView3.setText("开启");
				tv3 = false;
			}
			else{
				textView3.setText("关闭");
				tv3 = true;
			}
		}
	
	/*
	 * 设备状态改变回调，只有设备状态为可控才可以下发控制命令
	 */
	protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
		Log.i("Apptest","设备状态改变回调!!!".toString());
		if (device == mDevice) {
			if (GizWifiDeviceNetStatus.GizDeviceUnavailable == netStatus
					|| GizWifiDeviceNetStatus.GizDeviceOffline == netStatus) {
				handler.sendEmptyMessage(DISCONNECT);
			} else {
				handler.removeCallbacks(mRunnable);
				progressDialog.cancel();
				mDevice.getDeviceStatus();
			}
		}
	}
	
	
	/*
	 * 设备上报数据回调
	 */
	protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,java.util.concurrent.ConcurrentHashMap<String, Object> dataMap, int sn) {
		
		Log.i("Apptest", dataMap.get("data")+"上报回调".toString());
		if (dataMap.get("data") != null) {
			Log.i("Apptest", dataMap.get("data").toString());
			Message msg = new Message();
			msg.obj = dataMap.get("data");
			deviceStatu = (ConcurrentHashMap<String, Object>) dataMap.get("data");
			msg.what = UPDATE_UI;
			handler.sendMessage(msg);
		}
	}
	
	
	private boolean isDeviceCanBeControlled() {
		return mDevice.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceControlled;
	}

	private void toastDeviceNoReadyAndExit() {
		Toast.makeText(this, R.string.device_no_ready, Toast.LENGTH_SHORT).show();
		finish();
	}
	
	private void toastDeviceDisconnectAndExit() {
		Toast.makeText(this, R.string.disconnect, Toast.LENGTH_SHORT).show();
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}
}