package com.trabajofinal.gsm.gsmstatus;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Intent;
import android.os.CountDownTimer;
import android.net.wifi.ScanResult;
//import android.net.wifi.WifiConfiguration;
//import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.TextView;
import android.widget.Toast;

public class GSMStatus extends Activity {

	private CustomPhoneStateListener mPhoneStateReceiver = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_gsmstatus);
		final TextView textGsmCellLocation = (TextView) findViewById(R.id.gsmcelllocation);
		final TextView textMCC = (TextView) findViewById(R.id.mcc);
		final TextView textMNC = (TextView) findViewById(R.id.mnc);
		final TextView textCID = (TextView) findViewById(R.id.cid);
		final TextView textWiFi = (TextView) findViewById(R.id.wifiInfo);
		final TextView textOperator = (TextView) findViewById(R.id.operator);
		final TextView iterLeft = (TextView) findViewById(R.id.iterleft);
		
		// Get GSM status

		// retrieve a reference to an instance of TelephonyManager
		final TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// telephonyManager.listen(mPhoneStateReceiver,
		// PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		String networkOperator = telephonyManager.getNetworkOperator();
		String operator = telephonyManager.getNetworkOperatorName();
		String mcc = networkOperator.substring(0, 3);
		String mnc = networkOperator.substring(3);
		textMCC.setText("mcc: " + mcc);
		textMNC.setText("mnc: " + mnc);
		textOperator.setText("Operador: " + operator);
		CountDownTimer timer = new CountDownTimer(300000, 3000) {

			@Override
			public void onTick(long millisUntilFinished) {
				TelephonyManager telephonyManager2 = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

				iterLeft.setText("seconds remaining: " +millisUntilFinished/3000);
				GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager2
						.getCellLocation();

				int cid = cellLocation.getCid();
				// int lac = cellLocation.getLac();
				textGsmCellLocation.setText(cellLocation.toString());
				textCID.setText("gsm cell id: " + String.valueOf(cid));

				TextView Neighboring = (TextView) findViewById(R.id.neighboring);
				List<NeighboringCellInfo> NeighboringList = telephonyManager2
						.getNeighboringCellInfo();
				
				String stringNeighboring = "Neighboring List- Lac : Cid : RSSI\n";
				for (int i = 0; i < NeighboringList.size(); i++) {

					String dBm;
					int rssi = NeighboringList.get(i).getRssi();
					if (rssi == NeighboringCellInfo.UNKNOWN_RSSI) {
						dBm = "Unknown RSSI";
					} else {
						dBm = String.valueOf(-113 + 2 * rssi) + " dBm";
					}

					stringNeighboring = stringNeighboring
							+ String.valueOf(NeighboringList.get(i).getLac())
							+ " : "
							+ String.valueOf(NeighboringList.get(i).getCid())
							+ " : " + dBm + "\n";
				}

				Neighboring.setText(stringNeighboring);
			}

			@Override
			public void onFinish() {
			}
		}.start();

//		GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager
//				.getCellLocation();
//
//		String networkOperator = telephonyManager.getNetworkOperator();
//		String operator = telephonyManager.getNetworkOperatorName();
//		String mcc = networkOperator.substring(0, 3);
//		String mnc = networkOperator.substring(3);
//		textMCC.setText("mcc: " + mcc);
//		textMNC.setText("mnc: " + mnc);
//		textOperator.setText("Operador: " + operator);
//
//		int cid = cellLocation.getCid();
//		// int lac = cellLocation.getLac();
//		textGsmCellLocation.setText(cellLocation.toString());
//		textCID.setText("gsm cell id: " + String.valueOf(cid));
//
//		TextView Neighboring = (TextView) findViewById(R.id.neighboring);
//		List<NeighboringCellInfo> NeighboringList = telephonyManager
//				.getNeighboringCellInfo();
//
//		String stringNeighboring = "Neighboring List- Lac : Cid : RSSI\n";
//		for (int i = 0; i < NeighboringList.size(); i++) {
//
//			String dBm;
//			int rssi = NeighboringList.get(i).getRssi();
//			if (rssi == NeighboringCellInfo.UNKNOWN_RSSI) {
//				dBm = "Unknown RSSI";
//			} else {
//				dBm = String.valueOf(-113 + 2 * rssi) + " dBm";
//			}
//
//			stringNeighboring = stringNeighboring
//					+ String.valueOf(NeighboringList.get(i).getLac()) + " : "
//					+ String.valueOf(NeighboringList.get(i).getCid()) + " : "
//					+ dBm + "\n";
//		}
//
//		Neighboring.setText(stringNeighboring);

		// Get Wi-Fi status

		final WifiManager wifi;
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		// Get WiFi status
		// WifiInfo info = wifi.getConnectionInfo();
		textWiFi.setText("Wifis: ");

		// Register a broadcast receiver that listens for scan results.
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				List<ScanResult> results = wifi.getScanResults();
				for (ScanResult result : results) {
					textWiFi.append("\n\n " + result.toString());
				}
			}
		}, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		// Initiate a scan.
		wifi.startScan();

		// if(wifi.startScan()){
		// List available networks
		// List<ScanResult> configs = wifi.getScanResults();
		// for (ScanResult config : configs) {
		// textWiFi.append("\n\n" + config.toString());
		// }
		// }
	}

	// Register receiver to listen service state and signal strength
	private class CustomPhoneStateListener extends PhoneStateListener {
		/*
		 * Get the Signal strength from the provider, each time there is an
		 * update
		 */
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			Toast.makeText(
					getApplicationContext(),
					"Go to Firstdroid!!! GSM Cinr = "
							+ String.valueOf(signalStrength
									.getGsmSignalStrength()),
					Toast.LENGTH_SHORT).show();
		}
	}
}
