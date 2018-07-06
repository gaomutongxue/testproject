package com.example.doraemon.mymanytest.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.widget.ImageView;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.regex.Pattern;


/**
 * Created by cl on 2015/9/23.
 */
public class DefaultUtils {
    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param  （DisplayMetrics类中属性scaledDensity）
     * @returnx
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param  （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        try {
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (spValue * fontScale + 0.5f);
        } catch (Exception e) {
            return 12;
        }
    }

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            return false;
        }

        return true;
    }

    public static boolean isGrantExternalGuidePicRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
            return false;
        }
        return true;
    }

    public static boolean isGrantCamaraRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.CAMERA
            }, 1);
            return false;
        }

        return true;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static File getAlbumDir(Context context) {
        File dir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "ohm");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static Bitmap readBitMap(Context context, int resId) {

        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;

            Activity activity = (Activity) context;
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay()
                    .getMetrics(mDisplayMetrics);
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            opt.inSampleSize = calculateInSampleSize(opt, 100, 100);
            InputStream is = context.getResources().openRawResource(resId);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(is, null, opt);
        } catch (Resources.NotFoundException e) {
        } catch (Exception e) {
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图片的bitmap
     *
     * @param activity
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(Activity activity, String filePath) {
        try {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay()
                    .getMetrics(mDisplayMetrics);
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;// 同时设置才会有效
            options.inInputShareable = true;//当系统内存不够时候图片自动被回收
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inTempStorage = new byte[12 * 1024];
            options.inSampleSize = calculateInSampleSize(options,
                    (int) (mDisplayMetrics.widthPixels * 0.5), (int) (mDisplayMetrics.heightPixels * 0.5));
            options.inJustDecodeBounds = false;
            FileInputStream fs = null;
            try {
                fs = new FileInputStream(filePath);
                return BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (OutOfMemoryError e) {
            e.toString();
        }
        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        //
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //压缩图片防止太大内存溢出
    public static byte[] compress(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 50;
        bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
        return baos.toByteArray();
    }

    public static int readPictureDegree(String path) {
        if (path != null) {
            int degree = 0;
            try {
                ExifInterface exifInterface = new ExifInterface(path);
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
        return 0;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        Bitmap resize = null;
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            resize = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);

            if (resize != bitmap) {
                bitmap.recycle();
            }
            return resize;
        }
        return bitmap;
    }

    public static String getPath(Activity ctx, Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String ss = cursor.getString(column_index);
            cursor.close();
            return ss;
        }
        return uri.getPath();
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable, int w, int h) {
        // 取 drawable 的长宽
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
//   TODO   creatShortcut 是做什么的
//    public static void createShortcut(Context context) {
//        if (!Setting.getNormalBooleanValue("shortcut", false)) {
//            Intent intent = new Intent();
//            intent.setClass(context, context.getClass());
//                /*以下两句是为了在卸载应用的时候同时删除桌面快捷方式*/
//            intent.setAction("android.intent.action.MAIN");
//            intent.addCategory("android.intent.category.LAUNCHER");
//            Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
//            //不允许重复创建
//            shortcutintent.putExtra("duplicate", false);
//            //需要现实的名称
//            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
//            //快捷图片
//            Parcelable icon = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), R.drawable.app_icon);
//            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
//            //点击快捷图片，运行的程序主入口
//            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
//            //发送广播。OK
//            context.sendBroadcast(shortcutintent);
//            Setting.setNormalBooleanValue("shortcut", true);
//        }
//    }

//    public static void deleteShortCut(Context context) {
//        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
//        //快捷方式的名称
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
//        //在网上看到到的基本都是一下几句，测试的时候发现并不能删除快捷方式。
//        //String appClass = activity.getPackageName()+"."+ activity.getLocalClassName();
//        //ComponentName comp = new ComponentName( activity.getPackageName(), appClass);
//        //shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
//        /**改成以下方式能够成功删除，估计是删除和创建需要对应才能找到快捷方式并成功删除**/
//        Intent intent = new Intent();
//        intent.setClass(context, context.getClass());
//        intent.setAction("android.intent.action.MAIN");
//        intent.addCategory("android.intent.category.LAUNCHER");
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
//        context.sendBroadcast(shortcut);
//    }

    //数字  特殊符号仅限输入：*#+
//    public static boolean isFormatPhone(String phone) {
//        String format = "*#+-_";
//        String formatCount = "0123456789";
//        boolean isHaveNumber = false;
//        if (phone == null || phone.trim().equals("")) return false;
//        for (int i = 0; i < phone.length(); i++) {
//            char c = phone.charAt(i);
//            if (!formatCount.contains(String.valueOf(c))) {
//                if (!format.contains(String.valueOf(c))) {
//                    return false;
//                }
//            }
//            if (formatCount.contains(String.valueOf(c))) {
//                isHaveNumber = true;
//            }
//
//        }
//        if (!isHaveNumber) {
//            return false;
//        }
//        return true;
//    }
    public static boolean isPass(String pass) {
        if (pass == null || pass.equals("")) {
            return false;
        }
        Pattern p = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
        Pattern p1 = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
        if (p1.matcher(pass).find() || p.matcher(pass).find()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMoneyFormat(String phone) {
        if (phone == null || phone.equals("") || phone.contains(" ")) {
            return false;
        }
        if (phone.startsWith(".")) return false;
        String format = "01234567809.";
        boolean boo = true;
        for (int i = 0; i < phone.length(); i++) {
            char c = phone.charAt(i);
            if (!format.contains(String.valueOf(c))) {
                boo = false;
                break;
            }
        }
        return boo;
    }

    public static String getDoubleMoney(double money) {
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return formater.format(money);
    }

    public static boolean isPhoneFormat(String phone) {
        if (phone.trim().length() == 11) {
            return true;
        } else {
            return false;
        }
    }

    public static String getFormatDouble(double count) {
//        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");//格式化设置
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置
        return decimalFormat.format(count);
//        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
//        nf.setGroupingUsed(false);
//        return nf.format(count);
    }

    public static boolean isPswFormat(String phone) {
        if (phone.trim().length() != phone.length()) {
            return false;
        } else if (phone.length() >= 6 && phone.length() <= 16) {
            return true;
        } else {
            return false;
        }
    }

    //大于4位 就显示前后两位 其他用4个*代替  小于4位显示第一位
    public static String getFilterName(String name) {
        String filter = "";
        if (name == null || name.equals("") || name.contains(" ")) {
            return "****";
        } else if (name.length() < 4) {
            return name.substring(0, 1) + "****";
        } else {
            try {
                filter = name.substring(0, 2) + "****" + name.substring(name.length() - 2, name.length());
                return filter;
            } catch (Exception e) {
                return "";
            }
        }
    }

    //注册用户名只能是字母数字和下划线
    public static boolean isRegisterNameFormat(String s1) {
        if (s1.length() == 0) return false;
        String format = "01234567809_";
        boolean boo = false;
        for (int i = 0; i < s1.length(); i++) {
            String ss = String.valueOf(s1.charAt(i));
            if (ss.matches("[\\u4E00-\\u9FA5]+")) {
                boo = false;
                break;
            }
            if (Character.isLetter(s1.charAt(i))) {
                boo = true;
            } else if (format.contains(ss)) {
                boo = true;
            } else {
                boo = false;
                break;
            }
        }
        return boo;
    }

    //判断首字母是否是中文或字母
    public static boolean firstCharChineseOrEng(String s1) {
        if (s1.length() == 0) return false;
        String format = "01234567809";
        String ss = String.valueOf(s1.charAt(0));
        if (ss.matches("[\\u4E00-\\u9FA5]+")) {
            return true;
        }
        if (Character.isLetter(s1.charAt(0))) {
            return true;
        }
        if (format.contains(ss)) {
            return true;
        }
        return false;

    }

    public static boolean isChinese(String s1) {
        if (s1.contains(" ")) {
            return false;
        }
        if (s1.matches("[\\u4E00-\\u9FA5]+")) {
            return true;
        } else {
            return false;
        }
    }

    public static String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    public static String sha1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String hmacSign(String aValue, String aKey) {
        byte k_ipad[] = new byte[64];
        byte k_opad[] = new byte[64];
        byte keyb[];
        byte value[];
        try {
            keyb = aKey.getBytes("UTF-8");
            value = aValue.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e) {
            keyb = aKey.getBytes();
            value = aValue.getBytes();
        }
        Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
        Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
        for (int i = 0; i < keyb.length; i++) {
            k_ipad[i] = (byte) (keyb[i] ^ 0x36);
            k_opad[i] = (byte) (keyb[i] ^ 0x5c);

        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");

        } catch (NoSuchAlgorithmException e) {
            return null;

        }
        md.update(k_ipad);
        md.update(value);
        byte dg[] = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();
        return toHex(dg);
    }

    public static String toHex(byte input[]) {
        if (input == null)
            return null;
        StringBuffer output = new StringBuffer(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            int current = input[i] & 255;
            if (current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));

        }
        return output.toString();

    }


    /**
     * 在二维码中间添加Logo图案
//     */
//    private static Bitmap addLogo(Bitmap src, int drableId, int pix) {
//        if (src == null) {
//            return null;
//        }
//        if (drableId == 0) {
//            return src;
//        }
////        Drawable drawable = DbuniApplication.getInstance().getDrawable(drableId);
//        Drawable drawable = DbuniApplication.getInstance().getResources().getDrawable(drableId);
////        BitmapDrawable bd = (BitmapDrawable) drawable;
////        Bitmap logo = drawableToBitmap(drawable, dp2px(DbuniApplication.baseContext, pix) / 20, dp2px(DbuniApplication.baseContext, pix) / 20);
//        Bitmap logo = drawableToBitmap(drawable, 48, 48);
//        if (logo == null) {
//            return src;
//        }
//
//        //获取图片的宽高
//        int srcWidth = src.getWidth();
//        int srcHeight = src.getHeight();
//        int logoWidth = logo.getWidth();
//        int logoHeight = logo.getHeight();
//
//        if (srcWidth == 0 || srcHeight == 0) {
//            return null;
//        }
//
//        if (logoWidth == 0 || logoHeight == 0) {
//            return src;
//        }
//        //logo大小为二维码整体大小的1/5
////        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
//        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
//        try {
//            Canvas canvas = new Canvas(bitmap);
//            canvas.drawBitmap(src, 0, 0, null);
////            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
//            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
//            canvas.save(Canvas.ALL_SAVE_FLAG);
//            canvas.restore();
//        } catch (Exception e) {
//            bitmap = null;
//            e.getStackTrace();
//        }
//        return bitmap;
//    }

//
//    public static void createQRImage(String url, ImageView ivDimen, int logoBm) {
//        final int QR_WIDTH = 300;
//        final int QR_HEIGHT = 300;
//        try {
//            //判断URL合法性
//            if (url == null || "".equals(url) || url.length() < 1) {
//                return;
//            }
//            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            //图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
//            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
//            //下面这里按照二维码的算法，逐个生成二维码的图片，
//            //两个for循环是图片横列扫描的结果
//            for (int y = 0; y < QR_HEIGHT; y++) {
//                for (int x = 0; x < QR_WIDTH; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * QR_WIDTH + x] = 0xff000000;
//                    }
////                      else
////                      {
////                          pixels[y * QR_WIDTH + x] = 0xffffffff;
////                      }
////                    if (bitMatrix.get(x, y))
////                    {
////                        pixels[y * QR_WIDTH + x] = 0xff000000;
////                    }
////                    else
////                    {
////                        pixels[y * QR_WIDTH + x] = 0xffffffff;
////                    }
//                }
//            }
//            //生成二维码图片的格式，使用ARGB_8888
//            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
//            if (logoBm != 0) {
//                bitmap = addLogo(bitmap, logoBm, QR_WIDTH);
//                //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
////                return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
//                ivDimen.setImageBitmap(bitmap);
//            } else {
//                ivDimen.setImageBitmap(bitmap);
//            }
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//    }

}
