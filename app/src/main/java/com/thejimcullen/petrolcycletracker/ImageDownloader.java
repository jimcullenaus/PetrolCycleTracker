package com.thejimcullen.petrolcycletracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

	private final CityPetrolState state;
	private ImageView mImage;

	ImageDownloader(ImageView mImage, CityPetrolState state) {
		this.mImage = mImage;
		this.state = state;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		String urlDisplay = urls[0];
		Bitmap image = null;
		try {
			InputStream in = new java.net.URL(urlDisplay).openStream();
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
		mImage.setContentDescription(state.getImgAlt());
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
}
