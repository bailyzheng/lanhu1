package com.zys.jym.lanhu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkChangeReceiver extends BroadcastReceiver {
//	private static int NET_NOT = 0x999;
//	private static int NET_CAN = 0x998;
//	public static int NET_WORK_TYPE = NET_NOT;
	public static boolean NET_WORK_TYPE=false;
	public static boolean NET_WORK_WIFI_TYPE=false;
	public static NetWorkChangeReceiver myReceiver;

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobNetInfo != null && wifiNetInfo != null) {
			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
//			MyUtils.showToast(context, "网络不可以用");
				NET_WORK_TYPE = false;
			} else {
				// GlobalUtils.showToast(context, "网络已连接");
				NET_WORK_TYPE = true;
			}
			if (wifiNetInfo.isConnected()) {
				NET_WORK_WIFI_TYPE = true;
			} else {
				NET_WORK_WIFI_TYPE = false;
			}
		}
	}

	public static void registerReceiver(Context context) {
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		myReceiver = new NetWorkChangeReceiver();
		context.registerReceiver(myReceiver, filter);
	}

	public static void unregisterReceiver(Context context) {
		context.unregisterReceiver(myReceiver);
		myReceiver = null;
	}

}
