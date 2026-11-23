import io.polygon.kotlin.sdk.DefaultJvmHttpClientProvider;
import io.polygon.kotlin.sdk.rest.AggregatesDTO;
import io.polygon.kotlin.sdk.rest.AggregatesParametersBuilder;
import io.polygon.kotlin.sdk.rest.PolygonRestClient;

public class Main {
  public static void main(String[] args) {
    PolygonRestClient client = new PolygonRestClient(System.getenv("MASSIVE_API_KEY"),
            new DefaultJvmHttpClientProvider(),
            "api.massive.com");

    AggregatesDTO aggs = client.getAggregatesBlocking(
            new AggregatesParametersBuilder()
                    .ticker("AAPL")
                    .timespan("day")
                    .fromDate("2026-02-01")
                    .toDate("2026-04-01")
                    .build()
    );

    System.out.println("Got " + aggs.getResults().size() + " results");
    System.exit(0);
  }
}