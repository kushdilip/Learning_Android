package com.dk.helloworld;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

public class DownloadService extends IntentService {

	private int result = Activity.RESULT_CANCELED;
	public static final String URL = "urlpath";
	public static final String FILENAME = "filename";
	public static final String FILEPATH = "filepath";
	public static final String RESULT = "result";
	public static final String NOTIFICATION = "com.dk.helloworld";

	public DownloadService() {
		super("DownloadService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		//android.os.Debug.waitForDebugger();
		String urlPath = intent.getStringExtra(URL);
		String fileName = intent.getStringExtra(FILENAME);
		File output = new File(Environment.getExternalStorageDirectory(),
				fileName);
		if (output.exists()) {
			output.delete();
		}

		InputStream stream = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(urlPath);
			stream = url.openConnection().getInputStream();
			InputStreamReader reader = new InputStreamReader(stream);
			fos = new FileOutputStream(output.getPath());
			int next = -1;
			while ((next = reader.read()) != -1) {
				fos.write(next);
			}

			result = Activity.RESULT_OK;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		publishResults(output.getAbsolutePath(), result);

	}

	private void publishResults(String absolutePath, int result) {
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(FILEPATH, absolutePath);
		intent.putExtra(RESULT, result);
		sendBroadcast(intent);
	}

}
