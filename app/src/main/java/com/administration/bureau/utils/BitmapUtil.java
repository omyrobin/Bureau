package com.administration.bureau.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {
	
	/**
	 * 读取图片的旋转的角度
	 * @param path 图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int readBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	
	/**
	 * 将图片按照某个角度进行旋转
	 * @param bm 需要旋转的图片
	 * @param degree  旋转角度 正数表示向右(顺时针), 负数表示向左(逆时针)
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		
		return returnBm;
	}
	
	/**
	 * 对上传的图片进行压缩,获取压缩后的图片路径
	 */
	public static String compressImage(String beforeFilePath) {
		Bitmap bitmap;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(beforeFilePath, options);
		int w = options.outWidth;
		int y = options.outHeight;
		int b = w/240;
		File f = new File(beforeFilePath);
		long mb = 1024 * 1024;
		long fSize = f.length();
		if (fSize > 2.5 * mb) {
			options.inSampleSize = 8;
		} else if (fSize > 1.8 * mb) {
			options.inSampleSize = 6;
		} else if (fSize > 1.2 * mb) {
			options.inSampleSize = 4;
		} else if (fSize > 0.7 * mb) {
			options.inSampleSize = 2;
		} else if (fSize > 0.3 * mb) {
			options.inSampleSize = 1;
		}
		options.inSampleSize = b;
		options.inJustDecodeBounds = false;
		int degree = readBitmapDegree(beforeFilePath);
		bitmap = BitmapFactory.decodeFile(beforeFilePath, options);
		if(degree > 0)
			bitmap = rotateBitmapByDegree(bitmap, degree);
		try {
			String compressedFile = saveImage(bitmap);
			bitmap.recycle();
			return compressedFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 质量压缩
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>32) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }  
	
	/** 
     * 压缩图片 
     * @param image 
     * @return 
     */  
    public static Bitmap compressImageKb(Bitmap image) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
        int options = 100;  
        // 循环判断如果压缩后图片是否大于32kb,大于继续压缩  
        while (baos.toByteArray().length / 1024 > 32) {  
            // 重置baos  
            baos.reset();  
            // 这里压缩options%，把压缩后的数据存放到baos中  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);  
            // 每次都减少10  
            options -= 10;  
        }  
        // 把压缩后的数据baos存放到ByteArrayInputStream中  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
        // 把ByteArrayInputStream数据生成图片  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);  
        return bitmap;  
    }

	public static String saveImage(Bitmap bmp) throws Exception {
		File file = new File(buildFileName());
		file.createNewFile();
		FileOutputStream oStream = new FileOutputStream(file);
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, oStream); //100是照片质量，0-100，越大越好
		oStream.flush();
		oStream.close();
		return file.getPath();
	}

	public static String buildFileName(){
		String cache = FileUtil.getCacheFile() + "/";
		String basePath = cache + "dier/photo/";
		File dir = new File(basePath);
		dir.mkdirs();
		return basePath + getArc4Random() + ".jpg";
	}

	public static String getArc4Random() {
		return ((int) (Math.random() * 10000)) + "";
	}


}
