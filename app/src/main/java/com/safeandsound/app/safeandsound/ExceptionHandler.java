package com.safeandsound.app.safeandsound;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.MainThread;

import com.safeandsound.app.safeandsound.view.MainActivity;

/**
 * Created by Dominic on 22.05.2017.
 */

public class ExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler{

    private final Activity myContext;
    private final String LINE_SEPARATOR = "\n";

    public ExceptionHandler(Activity context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();


        Intent intent = new Intent(myContext, MainActivity.class);
        intent.putExtra("error", errorReport.toString());
        myContext.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
