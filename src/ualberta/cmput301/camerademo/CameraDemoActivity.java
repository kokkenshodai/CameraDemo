package ualberta.cmput301.camerademo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.logging.Logger;

import ualberta.cmput301.camerodemo.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CameraDemoActivity extends Activity {

	private TextView textView;
	private ImageButton imageButton;
	private Uri imageFileUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camero_demo);
		
		// Retrieve handlers
		textView = (TextView) findViewById(R.id.status);
		imageButton = (ImageButton) findViewById(R.id.image);
		
		// Set up the listener
		OnClickListener listener = new OnClickListener() {
			public void onClick(View view) {
				takeAPhoto(); // implement this method
			}
		};
		// Register a callback to be invoked when this view is clicked
		imageButton.setOnClickListener(listener);
	}


	// Implement takeAPhoto() method to allow you to take a photo when you click the ImageButton.
	// Notice that startActivity() method will not return any result when the launched activity 
	// finishes, while startActivityForResult() method will. To retrieve the returned result, you may 
	// need implement onAcitityResult() method.
	public void takeAPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(this)) ); 
		startActivityForResult(intent,1);	
	}
	
    private File getTempFile(Context context)
    {
    	File file = Environment.getExternalStorageDirectory().getAbsoluteFile();
	    File path = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "temp");
	    if(!path.exists())
	        path.mkdir();
	    String fileString = "debris.jpg";
	    return new File(path, fileString);
    }
    
    private File getPicturePath(Intent intent) {
        Uri uri = (Uri) intent.getExtras().get(MediaStore.EXTRA_OUTPUT);
        return new File(uri.getPath());
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data != null)
		{
			if(resultCode == RESULT_OK){
				try{
					//Bitmap bmp = data.getExtras().getParcelable("data");
					Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(getPicturePath(data)));
					imageButton.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageButton.getWidth(), imageButton.getHeight(), false));
					textView.setText("Photo OK");
				}
				catch(FileNotFoundException ex)
				{
					textView.setText("Photo Could not be read.");
				}
			}
			else if(resultCode == RESULT_CANCELED)
			{
				textView.setText("Photo Canceled");
			}
			else
			{
				textView.setText("Not sure what happened!");
			}
		}
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camero_demo, menu);
		return true;
	}

}
