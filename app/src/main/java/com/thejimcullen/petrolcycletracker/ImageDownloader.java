package com.thejimcullen.petrolcycletracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

	private ImageView mImage;

	public ImageDownloader(ImageView mImage) {
		this.mImage = mImage;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap image = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			image = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
			cancel(true);
		}
		return image;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		mImage.setImageBitmap(bitmap);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
}
