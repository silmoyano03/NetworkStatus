package com.trabajofinal.gsm.gsmstatus;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.TextView;

public class GSMStatus extends Activity {

    	   /** Called when the activity is first created. */
    	   @Override
    	   public void onCreate(Bundle savedInstanceState) {
    	       super.onCreate(savedInstanceState);
    	       setContentView(R.layout.layout_gsmstatus);
    	       TextView textGsmCellLocation = (TextView)findViewById(R.id.gsmcelllocation);
    	       TextView textMCC = (TextView)findViewById(R.id.mcc);
    	       TextView textMNC = (TextView)findViewById(R.id.mnc);
    	       TextView textCID = (TextView)findViewById(R.id.cid);
    	 
    	       
    	       //retrieve a reference to an instance of TelephonyManager
    	       TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    	       GsmCellLocation cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
    	       
    	       String networkOperator = telephonyManager.getNetworkOperator();
    	       String mcc = networkOperator.substring(0, 3);
    	       String mnc = networkOperator.substring(3);
    	       textMCC.setText("mcc: " + mcc);
    	       textMNC.setText("mnc: " + mnc);
    	       
    	       int cid = cellLocation.getCid();
    	       int lac = cellLocation.getLac();
    	       textGsmCellLocation.setText(cellLocation.toString());
    	       textCID.setText("gsm cell id: " + String.valueOf(cid));
    	       
    	       TextView Neighboring = (TextView)findViewById(R.id.neighboring);
    	       List<NeighboringCellInfo> NeighboringList = telephonyManager.getNeighboringCellInfo();
    	       
    	       String stringNeighboring = "Neighboring List- Lac : Cid : RSSI\n";
    	       for(int i=0; i < NeighboringList.size(); i++){
    	         
    	        String dBm;
    	        int rssi = NeighboringList.get(i).getRssi();
    	        if(rssi == NeighboringCellInfo.UNKNOWN_RSSI){
    	         dBm = "Unknown RSSI";
    	        }else{
    	         dBm = String.valueOf(-113 + 2 * rssi) + " dBm";
    	        }
    	 
    	        stringNeighboring = stringNeighboring
    	         + String.valueOf(NeighboringList.get(i).getLac()) +" : "
    	         + String.valueOf(NeighboringList.get(i).getCid()) +" : "
    	         + dBm +"\n";
    	       }
    	       
    	       Neighboring.setText(stringNeighboring);
    	   }
}
