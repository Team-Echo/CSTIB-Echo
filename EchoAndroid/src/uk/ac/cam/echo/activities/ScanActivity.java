package uk.ac.cam.echo.activities;

import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends Activity implements OnClickListener{
	
	TextView resultText;
	Button scanBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		
		resultText = (TextView)findViewById(R.id.qrResultText);
		scanBtn = (Button)findViewById(R.id.scanButton);
		
		scanBtn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.scanButton) {
			Log.d("CLICK","received");
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
			
		}
			
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrieve scan result
		IntentResult scanningResult =
				IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if(scanningResult != null) {
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			resultText.setText(scanContent + " - " + scanFormat);
		}else {
			Toaster.displayLong(this, "No scan data received!");
		}
	}

}
