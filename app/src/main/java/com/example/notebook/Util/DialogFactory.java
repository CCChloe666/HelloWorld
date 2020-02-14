//package com.example.notebook.Util;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.View;
//import android.widget.EditText;
//
//import androidx.appcompat.widget.AppCompatEditText;
//
//public class DialogFactory {
//    private static final int PADDING = 20;
//    private static final String CANCEL_TIP = "取消";
//    private static final String SURE_TIP = "确定";
//
//    public static void createAndShowDialog(Context context, String title, String content,
//                                           DialogInterface.OnClickListener positiveListener) {
//        createAndShowDialog(context, title, content, CANCEL_TIP, null, SURE_TIP, positiveListener);
//    }
//
//    public static void createAndShowDialog(Context context, String title, String content,
//                                           DialogInterface.OnClickListener negativeListener,
//                                           DialogInterface.OnClickListener positiveListener) {
//        createAndShowDialog(context, title, content, CANCEL_TIP, negativeListener, SURE_TIP, positiveListener);
//    }
//
//    public static void createAndShowDialog(Context context, String title, String content,
//                                           String negativeButtonTip, DialogInterface.OnClickListener negativeListener,
//                                           String positiveButtonTip, DialogInterface.OnClickListener positiveListener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(title);
//        builder.setMessage(content);
//        builder.setNegativeButton(negativeButtonTip, negativeListener);
//        builder.setPositiveButton(positiveButtonTip, positiveListener);
//        builder.show();
//    }
//
//    public static EditText showInputDialog(Context context, String title, String content, DialogInterface.OnClickListener positiveListener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(title);
//        builder.setMessage(content);
//        AppCompatEditText input = new AppCompatEditText(context);
//        builder.setView(input, PADDING, PADDING, PADDING, PADDING);
//        builder.setNegativeButton(CANCEL_TIP, null);
//        builder.setPositiveButton(SURE_TIP, positiveListener);
//        builder.show();
//        return input;
//    }
//
//    public static void showInputDialog(Context context, String title, View contentView, DialogInterface.OnClickListener positiveListener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(title);
//        builder.setMessage(null);
//        builder.setView(contentView, 3 * PADDING, PADDING, 3 * PADDING, PADDING);
//        builder.setNegativeButton(CANCEL_TIP, null);
//        builder.setPositiveButton(SURE_TIP, positiveListener);
//        builder.show();
//    }
//
//
//
//}
