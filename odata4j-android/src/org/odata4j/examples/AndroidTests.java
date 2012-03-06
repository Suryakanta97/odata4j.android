package org.odata4j.examples;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;
import org.odata4j.format.FormatType;
import org.odata4j.jersey.consumer.ODataJerseyConsumer;

import android.util.Log;

public class AndroidTests {

  public static void run() {
    runScenario(FormatType.ATOM);
    runScenario(FormatType.JSON);
  }

  private static void runScenario(FormatType formatType) {
    ODataConsumer c = ODataJerseyConsumer.newBuilder(ODataEndpoints.ODATA_TEST_SERVICE_READWRITE1).setFormatType(formatType).build();
    String logPrefix = String.format("[%s] ", formatType);

    log(logPrefix + "Categories count: %s", c.getEntities("Categories").execute().count());
    for (OEntity category : c.getEntities("Categories")) {
      log(logPrefix + "Category: %s", category);
    }

    int categoryId = 124;
    log(logPrefix + "Deleting Category...");
    c.deleteEntity("Categories", categoryId).execute();
    log(logPrefix + "Category deleted");

    log(logPrefix + "Creating Category...");
    OEntity category = c.createEntity("Categories").properties(OProperties.int32("ID", categoryId), OProperties.string("Name", "AndroidTests")).execute();
    log(logPrefix + "Created Category");

    log(logPrefix + "Merging Category...");
    c.mergeEntity(category.getEntitySetName(), category.getEntityKey()).properties(OProperties.string("Name", "AndroidTests2")).execute();
    log(logPrefix + "Merged Category");

    log(logPrefix + "Deleting Category...");
    c.deleteEntity("Categories", categoryId).execute();
    log(logPrefix + "Category deleted");
  }

  private static void log(String format, Object... args) {
    Log.d(AndroidTests.class.getSimpleName(), args != null && args.length == 0 ? format : String.format(format, args));
  }
}
