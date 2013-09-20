package com.dk.helloworld;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import javax.net.ssl.SSLContext;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Context context;
	private TextView textView;
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				String string = bundle.getString(DownloadService.FILEPATH);
				int resultCode = bundle.getInt(DownloadService.RESULT);
				if (resultCode == RESULT_OK) {
					Toast.makeText(MainActivity.this,
							"Download complete. Download URI: " + string,
							Toast.LENGTH_LONG).show();
					textView.setText("Download done");
				} else {
					Toast.makeText(MainActivity.this, "Download failed",
							Toast.LENGTH_LONG).show();
					textView.setText("Download failed");
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.status);

		// context = getApplicationContext();
		// Intent i = new Intent(MyApplication.getAppContext(),
		// MyService.class);
		// i.putExtra("KEY1", "Value to be used by service");
		// context.startService(i);
		//
		// Calendar cal = Calendar.getInstance();
		//
		// Intent intent = new Intent(this, MyService.class);
		// PendingIntent pIntent = PendingIntent.getService(this, 0, intent, 0);
		//
		// AlarmManager alarm = (AlarmManager)
		// getSystemService(Context.ALARM_SERVICE);
		// alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		// 30*1000, pIntent);
		//

		// Run socket.io client
		// MyTask rth = new MyTask(this);
		// rth.execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(receiver, new IntentFilter(
				DownloadService.NOTIFICATION));
	}

	public void onClick(View view) {
		Intent intent = new Intent(this, DownloadService.class);
		intent.putExtra(DownloadService.FILENAME, "index.html");
		intent.putExtra(DownloadService.URL,
				"http://www.vogella.com/index.html");
		startService(intent);
		textView.setText("Service started");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class MyTask extends AsyncTask<Void, Void, Void> {
		private MainActivity ma;
		private String value = "";

		public MyTask(MainActivity ma) {
			this.ma = ma;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				SocketIO.setDefaultSSLSocketFactory(SSLContext.getDefault());
				SocketIO socket = new SocketIO(
						"https://socketiotest-c9-kushdilip.c9.io");

				socket.connect(new IOCallback() {

					@Override
					public void onMessage(JSONObject json, IOAcknowledge ack) {
						try {
							System.out.println("Server said:"
									+ json.toString(2));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onMessage(String data, IOAcknowledge ack) {
						value = data;
						System.out.println("Server said: " + data);
					}

					@Override
					public void onError(SocketIOException socketIOException) {
						System.out.println("an Error occured");
						socketIOException.printStackTrace();
					}

					@Override
					public void onDisconnect() {
						System.out.println("Connection terminated.");
					}

					@Override
					public void onConnect() {
						value = "Connected";
						System.out.println("Connection established");
					}

					@Override
					public void on(String event, IOAcknowledge ack,
							Object... args) {
						System.out.println("Server triggered event '" + event
								+ "'");
					}
				});

				socket.send("Hello Server");
				System.out.println(value);
				if (socket.isConnected()) {
					Log.d("com.dk.HelloWorld", "socket Connected");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;
		}

	}
}
