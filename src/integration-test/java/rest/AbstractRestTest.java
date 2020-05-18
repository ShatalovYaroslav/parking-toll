
package rest;

import org.springframework.beans.factory.annotation.Value;



public class AbstractRestTest {
    @Value("${local.server.port}")
    protected int serverPort;
}
