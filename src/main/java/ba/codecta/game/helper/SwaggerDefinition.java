package ba.codecta.game.helper;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        tags = {
                //@Tag(name="widget", description="Widget operations."),
                //@Tag(name="gasket", description="Operations related to gaskets")
        },
        info = @Info(
                title="Quest for the Orb of Quarkus",
                version = "1.0.0 Alpha",
                contact = @Contact(
                        name = "Amar Ćatović",
                        url = "http://amarcatovic.com/",
                        email = "amar_catovic@hotmail.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class SwaggerDefinition extends Application {
}
