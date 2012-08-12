package org.odata4j.android.activity;

import java.util.Iterator;

import org.odata4j.android.AndroidLogger;
import org.odata4j.android.InfiniteAdapter;
import org.odata4j.android.ProgressDialogTask;
import org.odata4j.android.model.ServiceVM;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.OEntity;
import org.odata4j.core.ORelatedEntitiesLink;
import org.odata4j.jersey.consumer.behaviors.AllowSelfSignedCertsBehavior;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class EntitiesActivity extends ListActivity {

  private final AndroidLogger log = AndroidLogger.get(getClass());

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final ServiceVM service = (ServiceVM) getIntent().getExtras().getSerializable("service");
    final String entitySet = (String) getIntent().getExtras().getString("entitySet");
    final ORelatedEntitiesLink link = (ORelatedEntitiesLink) getIntent().getExtras().getSerializable("link");

    setTitle(link != null ? link.getTitle() : entitySet);

    new ProgressDialogTask<Iterator<OEntity>>(this, log, "Loading...") {
      protected Iterator<OEntity> doStuff() {
        ODataConsumer c = ODataConsumers.newBuilder(service.getUri()).setClientBehaviors(AllowSelfSignedCertsBehavior.allowSelfSignedCerts()).build();
        return (link != null ? c.getEntities(link) : c.getEntities(entitySet))
            // .top(10)
          .execute().iterator();
      }
      protected void showStuff(Iterator<OEntity> entities) {
        InfiniteAdapter<OEntity> adapter = new InfiniteAdapter<OEntity>(EntitiesActivity.this, entities, 20) {
          public View getView(int pos, View v, ViewGroup p) {
            OEntity entity = (OEntity) getItem(pos);
            return EntityViews.newEntityTable(getContext(), entity, service);
          }
        };
        setListAdapter(adapter);
        getListView().setOnScrollListener(adapter);
      }}.execute();

    getListView().setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        log.info("onItemClick");
      }
    });
    getListView().setDivider(null);
    getListView().setDividerHeight(0);
  }

}
