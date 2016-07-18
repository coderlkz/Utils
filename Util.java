package com.tongtong.ttmall.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tongtong.ttmall.Constants;
import com.tongtong.ttmall.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;


/**
 * 工具类
 *
 * @author LSF
 * @version $Id: Util.java, v 0.1 2014-11-3 下午4:19:51 LSF Exp $
 */
public class Util {

    static AlertDialog dialog;

    /**
     * 显示提示对话框
     */
    public static void ShowAlert(Context context, int textId, int demin) {
        showMessage(context, textId, demin);
    }

    /**
     * 消息提示文本样式
     */
    public static void showMessage(Context context, int textId, int demin) {
        if (null == context) {
            return;
        }
        TextView textView = new TextView(context);
        textView.setTextColor(context.getResources().getColor(R.color.colorAccent));
        textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        textView.setText(context.getResources().getString(textId) + "");
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 0, 0, 20);
        textView.setTextSize(16);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, demin);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(textView);
        toast.show();
    }

    /**
     * 根据资源得到字符串数组
     */
    public final static String[] getArray(Context mContext, int array) {
        String[] STATUS = mContext.getResources().getStringArray(array);
        return STATUS;
    }

    /**
     * 显示消息
     */
    public static void showMessage(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 得到UUID
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String result = uuid.toString().replace("-", "").toUpperCase();
        return result;
    }

    /**
     * 转化为本地时间
     */
    public static String getLocalDataTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // String name = TimeZone.getDefault().getDisplayName();
        format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date;
        String timeStr = "";
        try {
            date = format.parse(time);
            format.setTimeZone(TimeZone.getDefault());
            timeStr = format.format(date);
        } catch (ParseException e) {
            timeStr = time;
        }
        return timeStr;
    }

    /**
     * DIP转化为px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取MD5加密值
     */
    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHashString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

    /**
     * 获取文件的MD5值
     */
    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(
                    FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (value.length() == 32) {
            return value.toUpperCase();
        }
        int n = value.length();
        for (int i = 0; i < 32 - n; i++) {
            value += "0";
        }
        return value.toUpperCase();
    }

    public static Bitmap getScaledBitmap(String fileName, int dstWidth) {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, localOptions);
        int originWidth = localOptions.outWidth;
        int originHeight = localOptions.outHeight;

        localOptions.inSampleSize = originWidth > originHeight ? originWidth
                / dstWidth : originHeight / dstWidth;
        localOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(fileName, localOptions);
    }

    /**
     * 转换成圆形图片
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xFFFFFFFF;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        bitmap.recycle();

        Bitmap newoutput = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas newcanvas = new Canvas(newoutput);
        paint.setColor(0xffffffff);
        paint.setXfermode(null);
        newcanvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        newcanvas.drawBitmap(output, 0, 0, paint);
        output.recycle();

        return newoutput;

    }

    /**
     * 显示等待对话框，一般用于网络请求的时候
     */
    public static void ShowWaiting(Context context) {
        dialog = new AlertDialog(context, R.style.shareDialog);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.show();
    }

    /**
     * 关闭等待对话框
     */
    public static void CloseWaiting() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 根据URL下载图片
     * Get image from newwork
     *
     * @param path The path of image
     * @return InputStream
     * @throws Exception
     */
    public static InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(20 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * Get data from stream
     *
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 保存文件
     *
     * @throws IOException
     */
    public static void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(Constants.ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(Constants.ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    /**
     * 检查当前网络是否可用
     */

    public static boolean isNetworkAvailable(Context mContext) {
        Context context = mContext.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    //获取安装包信息
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 获取当前的时间戳
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 判断是否为手机号
     *
     * @param phoneNum
     * @return
     */
    public static boolean isPhoneNum(String phoneNum) {
        String telRegex = "[1][3578]\\d{9}";
        if (TextUtils.isEmpty(phoneNum) || phoneNum.length() < 11) {
            return false;
        } else {
            return phoneNum.matches(telRegex);
        }
    }

    /**
     * 判断密码格式是否正确
     *
     * @param pwd
     * @return
     */
    public static boolean isPassword(String pwd) {
        String pwdRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";
        return pwd.matches(pwdRegex);
    }

    /**
     * 判断用户名格式是否正确
     *
     * @param nickName
     * @return
     */
    public static boolean isNickName(String nickName) {
        String nickRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{4,8}$";
        return nickName.matches(nickRegex);
    }

    /**
     * 同一个TextView设置不同字体大小
     *
     * @param context
     * @param str
     * @return
     */
    public static SpannableStringBuilder getSizeSpanSpToPx(Context context, String str) {
        if (!str.contains(".")) {
            SpannableStringBuilder sb = new SpannableStringBuilder(str);
            sb.setSpan(new AbsoluteSizeSpan(DisplayUtil.sp2px(context, 20)), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
        String tempStr[] = str.split("\\.");
        SpannableStringBuilder sw = new SpannableStringBuilder(tempStr[0]);
        sw.setSpan(new AbsoluteSizeSpan(DisplayUtil.sp2px(context, 20)), 0, tempStr[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (tempStr.length >= 2) {
            SpannableStringBuilder gw = new SpannableStringBuilder(tempStr[1]);
            gw.setSpan(new AbsoluteSizeSpan(DisplayUtil.sp2px(context, 14)), 0, tempStr[1].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sw.append(".").append(gw);
        }
        return sw;
    }

    /**
     * Get image from newwork
     *
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    public byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(20 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return readStream(inStream);
        }
        return null;
    }

    /**
     * 隐藏输入法
     *
     * @param v
     */
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }


    /**
     * 获取屏幕的高度
     */
    public static int getScreenWidth(Context ctx) {
        WindowManager wm = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width;
    }

    /**
     * 根据text内容设置TextView
     * @param text
     * @param textView
     */
    public static void setTextData(String text, TextView textView) {
        if(text == null || "null".equals(text)) {
            textView.setText("暂无数据");
        }else {
            textView.setText(text);
        }
    }


    /**
     * 检查字符串是否不是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
     * <pre>
     * StringUtil.isBlank(null)      = false
     * StringUtil.isBlank("")        = false
     * StringUtil.isBlank(" ")       = false
     * StringUtil.isBlank("bob")     = true
     * StringUtil.isBlank("  bob  ") = true
     * </pre>
     *
     * @param str 要检查的字符串
     *
     * @return 如果为空白, 则返回<code>true</code>
     */
    public static boolean isNotBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 自定义圆形进度条
     */
    public static CustomProgressDialog mDialog;

    public static void showRequestDialog(Context ctx, String msg) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = CustomProgressDialog.createDialog(ctx).setMessage(msg);
        mDialog.show();
    }

    /**
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    public static void controlKeyboardLayout(final Context ctx,
                                             final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        // 获取root在窗体的可视区域
                        root.getWindowVisibleDisplayFrame(rect);
                        // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                        int rootInvisibleHeight = root.getRootView()
                                .getHeight() - rect.bottom;
                        // 若不可视区域高度大于100，则键盘显示
                        if (rootInvisibleHeight > 100) {
                            int[] location = new int[2];
                            // 获取scrollToView在窗体的坐标
                            scrollToView.getLocationInWindow(location);
                            // 计算root滚动高度，使scrollToView在可见区域
                            int srollHeight = (location[1]
                                    + scrollToView.getHeight() + dip2px(ctx, 10))
                                    - rect.bottom;
                            root.scrollTo(0, srollHeight);
                        } else {
                            // 键盘隐藏
                            root.scrollTo(0, 0);
                        }
                    }
                });
    }

}