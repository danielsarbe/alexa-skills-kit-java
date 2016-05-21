import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class JiraREST {

    public static void executePost(String[] args) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("http://localhost", 8090, "http");

            // mock data
            String data =
                    "{\n" +
                            "    \"fields\": {\n" +
                            "       \"project\":\n" +
                            "       { \n" +
                            "          \"key\": \"TEST\"\n" +
                            "       },\n" +
                            "       \"summary\": \"REST ye merry gentlemen.\",\n" +
                            "       \"description\": \"Creating of an issue using project keys and issue type names using the REST API\",\n" +
                            "       \"issuetype\": {\n" +
                            "          \"name\": \"Bug\"\n" +
                            "       }\n" +
                            "   }\n" +
                            "}";

            // specify the post request
            String post_request = "curl -D- -u fred:fred -X POST --data {"+ data +"} -H \"Content-Type: application/json\" http://localhost:8090/rest/api/2/issue/";

            HttpPost postRequest = new HttpPost(post_request);

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            Header[] headers = httpResponse.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
            System.out.println("----------------------------------------");

            if (entity != null) {
                System.out.println(EntityUtils.toString(entity));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }
}