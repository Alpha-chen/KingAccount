package com.account.king.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.account.king.R;


/**
 * @author King'
 */
public class ImportDataDialog extends Dialog {


    private ProgressBar progressBar;

    public ImportDataDialog(Context context) {
        this(context, R.style.dialog_transparent);
    }

    public ImportDataDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    int gress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_import_data);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gress = 0;
                    while (gress <= 80) {
                        progressBar.setProgress(gress++);
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
