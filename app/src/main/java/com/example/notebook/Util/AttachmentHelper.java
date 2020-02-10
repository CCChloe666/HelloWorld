package com.example.notebook.Util;

import android.content.Context;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by hzhuqi on 2019/4/14
 */
public class AttachmentHelper {

    public static Uri getFileProviderUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".authority", file);
    }

    public static File createNewAttachmentFile(Context context, String subDirName, String extension) {
        File file = new File(context.getExternalFilesDir(subDirName), createNewAttachmentName(extension));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    private static synchronized String createNewAttachmentName(String extension) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String name = sdf.format(calendar.getTime());
        name += extension != null ? extension : "";
        return name;
    }
}
