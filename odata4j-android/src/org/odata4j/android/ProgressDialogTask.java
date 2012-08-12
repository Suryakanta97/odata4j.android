package org.odata4j.android;

import org.odata4j.exceptions.ODataProducerException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public abstract class ProgressDialogTask<TResult> extends AsyncTask<Void, Void, TResult> {

  private final AndroidLogger log;
  private final Activity activity;
  private final ProgressDialog dialog;
  private Exception ex;

  public ProgressDialogTask(Activity activity, AndroidLogger log, String msg) {
    this.log = log;
    this.activity = activity;
    this.dialog = new ProgressDialog(activity);
    dialog.setMessage(msg);
  }

  abstract protected TResult doStuff();
  abstract protected void showStuff(TResult result);

  @Override
  protected void onPreExecute() {
    dialog.show();
  }

  @Override
  protected void onPostExecute(TResult result) {
    if (dialog.isShowing())
      try { dialog.dismiss(); } catch (Exception e) {}

    if (ex == null) {
      showStuff(result);
    } else {
      String msg = (ex instanceof ODataProducerException) ? ((ODataProducerException) ex).getOError().getMessage() : ex.getMessage();
      Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
      finish();
    }
  }

  protected void finish() {
    activity.finish();
  }

  @Override
  protected TResult doInBackground(Void... args) {
    try {
      return doStuff();
    } catch (Exception e) {
      ex = e;
      log.warn("Error doing background work", ex);
    }
    return null;
  }

}