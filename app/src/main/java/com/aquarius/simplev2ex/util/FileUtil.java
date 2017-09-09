package com.aquarius.simplev2ex.util;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by aquarius on 2017/9/5.
 */
public class FileUtil {

    private FileUtil() {
    }

    private static final String TAG = "FileUtil";

    /**
     * 写入文本到文件中，保存在/data/data/package_name/files目录下
     */
    public static void write(Context context, String fileName, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化对象到内部文件中
     */
    public static void write(Context context, String fileName, Object object) {
        if (object == null) {
            return;
        }

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            fos.close();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 反序列化
     */
    public static Object readObject(Context context, String fileName) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(fileName);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(ois != null) try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void readFile(Context context, String fileName) {

    }


    /**
     * 检查文件是否存在
     */
    public static boolean checkFileExist(String path) {
        return new File(path).exists();
    }

    // 获得某个目录下文件的大小
    public static long getFileSizeOfDir(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }

        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            }else if (file.isDirectory()){
                dirSize += getFileSizeOfDir(file);
            }
        }
        return dirSize;
    }

    public static String getFileSize(long size) {
        if(size < 0) return "0K";
        java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
        float count = (float) size / 1024;
        if (count >= 1024) {
            return df.format(count / 1024) + "M";
        } else {
            return df.format(count) + "K";
        }
    }

    public static long getCacheSize(Context context){
        long fileSize = 0;
        File cacheDir = context.getCacheDir();
        fileSize += getFileSizeOfDir(cacheDir);
        return fileSize;
    }

    public static long getFileCache(Context context) {
        long fileSize = 0;
        File filesDir = context.getFilesDir();
        fileSize += getFileSizeOfDir(filesDir);
        return fileSize;
    }

    public static void clearFileCache(Context context) {
        clearCacheFolder(context.getFilesDir(),System.currentTimeMillis());
    }

    public static int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir!= null && dir.isDirectory()) {
            try {
                for (File child:dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }
}
