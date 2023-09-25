import com.example.testurl.WebsiteMonitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebsiteMonitorTest {

    @Test
    public void testWebsiteIsUpWithContent() {
        WebsiteMonitor websiteMonitor = new WebsiteMonitor("config.txt");

        // Simulate a website with content that should return CONNECTION_OK_CONTENT_OK
        WebsiteMonitor.WebsiteStatus status = websiteMonitor.websiteIsUp("http://example.com", "Example Domain");
        assertEquals(WebsiteMonitor.WebsiteStatus.CONNECTION_OK_CONTENT_OK, status);
    }

    @Test
    public void testWebsiteIsUpWithContentProblem() {
        WebsiteMonitor websiteMonitor = new WebsiteMonitor("config.txt");

        // Simulate a website with content that doesn't match, resulting in CONNECTION_OK_CONTENT_PROBLEM
        WebsiteMonitor.WebsiteStatus status = websiteMonitor.websiteIsUp("http://example.com", "NonExistentContent");
        assertEquals(WebsiteMonitor.WebsiteStatus.CONNECTION_OK_CONTENT_PROBLEM, status);
    }

    @Test
    public void testWebsiteIsUpWithConnectionProblem() {
        WebsiteMonitor websiteMonitor = new WebsiteMonitor("config.txt");

        // Simulate a website with a connection problem (non-existent URL), resulting in CONNECTION_PROBLEM
        WebsiteMonitor.WebsiteStatus status = websiteMonitor.websiteIsUp("http://nonexistentwebsite.com", "SomeContent");
        assertEquals(WebsiteMonitor.WebsiteStatus.CONNECTION_PROBLEM, status);
    }
}
