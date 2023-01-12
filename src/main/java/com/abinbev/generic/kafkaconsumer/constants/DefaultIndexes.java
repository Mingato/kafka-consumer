package com.abinbev.generic.kafkaconsumer.constants;

import lombok.experimental.UtilityClass;
import org.springframework.data.mongodb.core.index.Index;

import java.util.Map;


import static com.abinbev.generic.kafkaconsumer.constants.ConsumerConstants.UPDATED_AT;
import static java.util.concurrent.TimeUnit.DAYS;
import static org.springframework.data.domain.Sort.Direction.ASC;

@UtilityClass
public class DefaultIndexes {

    public static final String AVAILABLE_COUNTRIES_PROPERTY = "abi.toggle.countries";
    public static final String SUFFIX_COLLECTION = "-OrdersHistory";

    private static final String ACCOUNT_ID = "accountId";
    public static final Map<String, Index> DEFAULT_INDEXES = Map.of(
            //TODO
            "accountId_updatedAt", new Index().named("accountId_updatedAt")
                    .on(UPDATED_AT , ASC)
                    .background().expire(62 , DAYS) ,


            "accountId_1_updatedAt_1", new Index().named("accountId_1_updatedAt_1")
                    .on(ACCOUNT_ID, ASC)
                    .on(UPDATED_AT, ASC)
                    .background(),

            "_id_1_accountId_1_updatedAt_1", new Index().named("_id_1_accountId_1_updatedAt_1")
                    .on("_id", ASC)
                    .on(ACCOUNT_ID, ASC)
                    .on(UPDATED_AT, ASC)
                    .background(),

            "order.orderNumber_1", new Index().named("order.orderNumber_1")
                    .on("order.orderNumber", ASC)
                    .background(),

            "accountId_1", new Index().named("accountId_1")
                    .on(ACCOUNT_ID, ASC)
                    .background()
    );
}
