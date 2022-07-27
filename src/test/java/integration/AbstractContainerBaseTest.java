package integration;

import com.abinbev.ontaptestutils.mongo.SingleMongoDbRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@ActiveProfiles("test")
@SpringBootTest(properties = "abi.toggle.countries=BR" )
public abstract class AbstractContainerBaseTest {

    @DynamicPropertySource
    public static void addProperties(final DynamicPropertyRegistry registry) {
        SingleMongoDbRule.addDynamicProperties(registry);
    }
}
