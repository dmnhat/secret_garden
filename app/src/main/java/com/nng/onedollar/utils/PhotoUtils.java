package com.nng.onedollar.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;

public class PhotoUtils {

	public static String capturePhoto(FragmentActivity activity, int requestCode) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			String photoPath = "";
			try {
				photoFile = createImageFile();
				photoPath = photoFile.getAbsolutePath();
			} catch (IOException ex) {
				// hope that it will not throw this exception! >:)
				ex.printStackTrace();
			}

			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				activity.startActivityForResult(takePictureIntent, requestCode);
			}

			return photoPath;
		}

		return "";
	}

	public static void choosePhoto(FragmentActivity activity, int requestCode) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		activity.startActivityForResult(photoPickerIntent, requestCode);
	}

	public static String getPhotoPath(FragmentActivity activity, Intent data) {
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String photoPath = cursor.getString(columnIndex);
		cursor.close();

		return photoPath;
	}

	public static void addPhotoToGallery(FragmentActivity activity, String photoPath) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri contentUri = Uri.fromFile(new File("file:" + photoPath));
		mediaScanIntent.setData(contentUri);
		activity.sendBroadcast(mediaScanIntent);
	}

	public static String scaleBitmapFile(String path) throws Exception {
		int targetW = 480;
		int targetH = 800;

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// get the orientation of bitmap
		ExifInterface ei = new ExifInterface(path);
		int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

		// Determine how much to scale down the image
		int scaleFactor;
		if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_180) {
			scaleFactor = Math.min(photoH / targetW, photoW / targetH);
		} else {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;

		Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);

		// rotate bitmap if needed
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			bitmap = rotateBitmap(bitmap, 90);
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			bitmap = rotateBitmap(bitmap, 180);
			break;
		}

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File scaledImageFile = File.createTempFile("SCALED_" + generateFileName(), ".jpg", storageDir);
		FileOutputStream fo = new FileOutputStream(scaledImageFile);
		fo.write(bytes.toByteArray());
		fo.close();

		bitmap.recycle();

		return scaledImageFile.getAbsolutePath();
	}

	public static String getBase64FromBitmap(String path) {
		Bitmap bmp = BitmapFactory.decodeFile(path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] byteArrayImage = baos.toByteArray();
		String base64 = Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
		return base64;
	}

	private static String generateFileName() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		return "JPEG_" + timeStamp + "_";
	}

	private static File createImageFile() throws IOException {
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(generateFileName(), ".jpg", storageDir);
		return image;
	}

	private static Bitmap rotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	public static class CircleBitmapDisplayer implements BitmapDisplayer {

		@Override
		public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.RGB_565);

			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);

			// --CROP THE IMAGE
			canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2 - 1, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);

			imageAware.setImageBitmap(output);
		}

	}

}
