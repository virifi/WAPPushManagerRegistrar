package net.virifi.android.wappushregistrar;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.internal.telephony.IWapPushManager;


public class WAPPushManagerRegistrarActivity extends Activity {
    IWapPushManager mWapPushManager = null;
    
    private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mWapPushManager = IWapPushManager.Stub.asInterface(service);
			Toast.makeText(getApplicationContext(), "ServiceConnected", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mWapPushManager = null;
			Toast.makeText(getApplicationContext(), "ServiceDisconnected", Toast.LENGTH_SHORT).show();
		}
    	
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bindService(new Intent(IWapPushManager.class.getName()),
        		mConnection, Context.BIND_AUTO_CREATE);
        
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean success = false;
				try {
					success = mWapPushManager.addPackage("36956", "application/vnd.wap.emn+wbxml",
							"jp.co.nttdocomo.carriermail", "jp.co.nttdocomo.carriermail.SMSService",
							1, false, false);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				if (success) {
					Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        Button unregisterButton = (Button) findViewById(R.id.unregister_button);
        unregisterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean success = false;
				try {
					success = mWapPushManager.deletePackage("36956", "application/vnd.wap.emn+wbxml",
							"jp.co.nttdocomo.carriermail", "jp.co.nttdocomo.carriermail.SMSService");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				if (success) {
					Toast.makeText(getApplicationContext(), "Unregistered", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mConnection);
	}
}