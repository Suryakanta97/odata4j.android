package org.odata4j.android;

import android.util.Log;

public class AndroidLogger {

  private final String tag;

  private AndroidLogger(Class<?> clazz) {
    tag = clazz.getSimpleName();
  }

  public static AndroidLogger get(Class<?> clazz) {
    return new AndroidLogger(clazz);
  }

  public void info(String format, Object... args) {
    Log.i(tag, String.format(format, args));
  }

  public void info(String message) {
    Log.i(tag, message);
  }

  public void warn(String message, Exception ex) {
    Log.w(tag, message, ex);
  }

}
