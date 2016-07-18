package com.tongtong.ttmall.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 图片加载
 * 
 * @author Ganker
 * 
 */
public class ImageLoader {

	public static ImageLoader mImageLoader;
	com.nostra13.universalimageloader.core.ImageLoader imageLoader;

	/**
	 * 获取实例
	 * 
	 * @param context
	 * @return
	 */
	public static ImageLoader getImageLoader(Context context) {
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(context);
		}
		return mImageLoader;
	}

	/**
	 * 私有构造函数
	 * 
	 * @param context
	 */
	ImageLoader(Context context) {
		imageLoader = com.nostra13.universalimageloader.core.ImageLoader
				.getInstance();

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)
				// 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Bitmap.Config.RGB_565)
				//.displayer(new FadeInBitmapDisplayer(500))//是否图片加载好后渐入的动画时间
				.build();

		// .showStubImage(R.drawable.default_pic_bg) // 设置图片下载期间显示的图片
		// .showImageForEmptyUri(R.drawable.default_pic_bg) //
		// 设置图片Uri为空或是错误的时候显示的图片
		// .showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
		// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().threadPoolSize(1) // 设置线程池的最高线程数量
				// .discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		imageLoader.init(config);
	}
	/**
	 * ImageView加载图片
	 * 
	 * @param view
	 * @param filePath
	 */
	public void loaderImage(ImageView view, String filePath) {
		imageLoader.displayImage(filePath, view);
	}

}
