package com.bionx.res.activity;

import java.io.DataOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bionx.res.R;

public class AdvancezRAM extends Activity implements OnClickListener {

	private EditText input;
	private Button apply;
	private Button back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zram);

		back = (Button) findViewById(R.id.back);
		input = (EditText) findViewById(R.id.userInput);
		apply = (Button) findViewById(R.id.apply);
		apply.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		int value = Integer.parseInt(input.getText().toString());

		// Arbitrary value, I don't know the maximum zRam compression (if
		// there's one)
		if (value > 200) {

			Toast.makeText(
					this,
					"The value you entered is too high and may cause problems! Please reduce it",
					Toast.LENGTH_LONG).show();
		} else {

			try {
				Process process = null;
				process = Runtime.getRuntime().exec("su");

				DataOutputStream os = new DataOutputStream(
						process.getOutputStream());

				os.writeBytes("busybox mkswap /dev/block/zram0" + "\n");
				os.writeBytes("busybox swapon /dev/block/zram0" + "\n");
				os.writeBytes("echo $((1024*1024*" + value
						+ ")) > /sys/block/zram0/disksize" + "\n");

				os.writeBytes("exit\n");
				os.flush();
				process.waitFor();

				Log.v("zRam", "Compression level set to " + value);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		back.setOnClickListener(new OnClickListener() 
        {
                @Override
                public void onClick(View arg0) {
                	finish();
                }
        });
	}
}