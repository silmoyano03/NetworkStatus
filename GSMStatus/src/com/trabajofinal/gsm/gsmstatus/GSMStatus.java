package com.trabajofinal.gsm.gsmstatus;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
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
		TextView textGsmCellLocation = (TextView) findViewById(R.id.gsmcelllocation);
		TextView textMCC = (TextView) findViewById(R.id.mcc);
		TextView textMNC = (TextView) findViewById(R.id.mnc);
		TextView textCID = (TextView) findViewById(R.id.cid);
		TextView textWiFi = (TextView) findViewById(R.id.wifiInfo);

		// Get GSM status

		// retrieve a reference to an instance of TelephonyManager
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(mPhoneStateReceiver,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager
				.getCellLocation();

		String networkOperator = telephonyManager.getNetworkOperator();
		String mcc = networkOperator.substring(0, 3);
		String mnc = networkOperator.substring(3);
		textMCC.setText("mcc: " + mcc);
		textMNC.setText("mnc: " + mnc);

		int cid = cellLocation.getCid();
		int lac = cellLocation.getLac();
		textGsmCellLocation.setText(cellLocation.toString());
		textCID.setText("gsm cell id: " + String.valueOf(cid));

		TextView Neighboring = (TextView) findViewById(R.id.neighboring);
		List<NeighboringCellInfo> NeighboringList = telephonyManager
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
					+ String.valueOf(NeighboringList.get(i).getLac()) + " : "
					+ String.valueOf(NeighboringList.get(i).getCid()) + " : "
					+ dBm + "\n";
		}

		Neighboring.setText(stringNeighboring);

		// Get Wi-Fi status

		WifiManager wifi;
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		// Get WiFi status
		WifiInfo info = wifi.getConnectionInfo();
		textWiFi.append(info.toString());

		// List available networks
		List<WifiConfiguration> configs = wifi.getConfiguredNetworks();
		for (WifiConfiguration config : configs) {
			textWiFi.append("\n\n" + config.toString());
		}
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
