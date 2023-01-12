package com.abinbev.beesforce.kafkaconsumer.dbchangelogs;



import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;

import java.util.Arrays;
import java.util.List;


import static java.util.stream.Collectors.toList;


/**
 * Ensures that <b>ALL</b> indexes present int {@link DefaultIndexes#DEFAULT_INDEXES}
 * will be created  <b>FOR ALL Countries under</b>  {@link DefaultIndexes#AVAILABLE_COUNTRIES_PROPERTY}. If the index already exists on the collection, it is ignored. Otherwise its created.
 * <p></p>
 * <b><p>!! This script will always run on application startup !!</b>
 * <b><p>This script <b>will include all indexes for all countries</b> present in {@link DefaultIndexes#AVAILABLE_COUNTRIES_PROPERTY}, <b>even new ones !!</b>
 *
 * @since 29/03/2022
 * @author Neemias Souza
 */
@Slf4j
@ChangeLog
@SuppressWarnings("unused")
public class _0001__PersistDefaultIndexes {

    @ChangeSet(order = "1", id = "PersistingDefaultIndexesForAllCountries", author = "Neemias Souza", runAlways = true)
    public void run(final MongockTemplate mongockTemplate, final Environment environment) {
        log.info("Persisting default indexes...");

        for (final String country : getCountries(environment) ) {

            final String collectionName = setSuffixInCountryName(country);

            if( collectionContainsDefaultIndexes(mongockTemplate, collectionName) ){
                log.info("{} OK!", collectionName);
            } else {
                ensureDefaultIndexes(mongockTemplate, collectionName);
            }
        }

        log.info("Finished default indexes persistence.");
    }

    private void ensureDefaultIndexes(final MongockTemplate mongockTemplate, final String collectionName) {

        for (final var entrySet : DEFAULT_INDEXES.entrySet() ){

            final Index defaultIndexObject = entrySet.getValue();
            final String indexName = entrySet.getKey();

            if( collectionContainsIndex(mongockTemplate, collectionName, indexName)){
                log.info("Already existed: {} for {} ", indexName, collectionName);
            } else {
                mongockTemplate.indexOps(collectionName).ensureIndex(defaultIndexObject);
                log.info("Created: {} for {}", indexName, collectionName);
            }

        }

    }

    private List<String> getCountries(final Environment environment) {
        final String countries = environment.getProperty(AVAILABLE_COUNTRIES_PROPERTY);

        log.info("ACTIVE_COUNTRIES: {} ", countries);
        assert countries != null;
        return Arrays.stream(countries.split(","))
                .filter(item -> !item.isBlank())
                .map(String::trim).collect(toList());
    }

    private String setSuffixInCountryName(final String countryName){
        return countryName + SUFFIX_COLLECTION;
    }

    private boolean collectionContainsDefaultIndexes(final MongockTemplate mongockTemplate, final String collectionName ){
        final List<String> collectionIndexesNames = mongockTemplate.indexOps(collectionName).getIndexInfo().stream()
                .map(IndexInfo::getName).collect(toList());

        return collectionIndexesNames.containsAll(DEFAULT_INDEXES.keySet());
    }

    private boolean collectionContainsIndex(final MongockTemplate mongockTemplate, final String collectionName, final String indexName ){
        final List<String> collectionIndexesNames = mongockTemplate.indexOps(collectionName).getIndexInfo().stream()
                .map(IndexInfo::getName).collect(toList());

        return collectionIndexesNames.contains(indexName);
    }
}