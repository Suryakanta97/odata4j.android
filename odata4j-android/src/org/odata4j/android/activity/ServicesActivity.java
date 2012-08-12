package org.odata4j.android.activity;

import org.odata4j.android.AndroidLogger;
import org.odata4j.android.ProgressDialogTask;
import org.odata4j.android.R;
import org.odata4j.android.model.ServiceVM;
import org.odata4j.android.model.ServicesVM;
import org.odata4j.examples.AndroidTests;
import org.odata4j.examples.consumer.AppEngineConsumerExample;
import org.odata4j.examples.consumer.NetflixConsumerExample;
import org.odata4j.examples.consumer.ODataTestServiceReadWriteExample;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ServicesActivity extends ListActivity {

  private final AndroidLogger log = AndroidLogger.get(getClass());

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ServicesVM app = new ServicesVM();
    setListAdapter(new ArrayAdapter<ServiceVM>(this, R.layout.service, app.getServices()));
    getListView().setTextFilterEnabled(true);
    getListView().setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        log.info("onItemClick(%s,%s)", position, id);
        ServiceVM service = (ServiceVM) parent.getItemAtPosition(position);
        startActivity(new Intent(ServicesActivity.this, EntitySetsActivity.class).putExtra("service", service));
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    addMenuItem(menu, "Run tests", "Running tests...", "Tests ran successfully", new Runnable() {
      public void run() {
        AndroidTests.run();
      }});
    addMenuItem(menu, "Run ODataTestServiceReadWriteExample", "Running ODataTestServiceReadWriteExample...", "ODataTestServiceReadWriteExample ran successfully", new Runnable() {
      public void run() {
        ODataTestServiceReadWriteExample.main(null);
      }});
    addMenuItem(menu, "Run NetflixConsumerExample", "Running NetflixConsumerExample...", "NetflixConsumerExample ran successfully", new Runnable() {
      public void run() {
        NetflixConsumerExample.main((String[]) null);
      }});
    addMenuItem(menu, "Run AppEngineConsumerExample", "Running AppEngineConsumerExample...", "AppEngineConsumerExample ran successfully", new Runnable() {
      public void run() {
        AppEngineConsumerExample.main(null);
      }});
    return true;
  }

  private void addMenuItem(Menu menu, String menuCaption, final String dialogCaption, final String toastCaption, final Runnable work) {
    menu.add(menuCaption).setOnMenuItemClickListener(new OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem item) {
        new ProgressDialogTask<Void>(ServicesActivity.this, log, dialogCaption) {
          protected Void doStuff() {
            work.run();
            return null;
          }
          protected void showStuff(Void result) {
            Toast.makeText(ServicesActivity.this, toastCaption, Toast.LENGTH_LONG).show();
          }
          protected void finish() {}}.execute();
          return true;
      }});
  }

}
