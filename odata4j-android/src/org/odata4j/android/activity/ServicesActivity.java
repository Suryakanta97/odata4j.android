package org.odata4j.android.activity;

import org.odata4j.android.AndroidLogger;
import org.odata4j.android.R;
import org.odata4j.android.model.ServiceVM;
import org.odata4j.android.model.ServicesVM;
import org.odata4j.examples.AndroidTests;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

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
    menu.add("Run Tests");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    AndroidTests.run();
    return true;
  }
}
