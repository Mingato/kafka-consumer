package com.abinbev.generic.kafkaconsumer;

import com.abinbev.generic.kafkaconsumer.repository.GenericRepository;
import com.abinbev.generic.kafkaconsumer.service.AbstractMongoService;
import com.abinbev.generic.kafkaconsumer.service.MongoService;
import com.abinbev.ontaputils.core.EnableCore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@EnableCore
@Import({ EnableKafkaConsumerRepository.$.class,
        GenericRepository.class, MongoService.class, AbstractMongoService.class})
public @interface EnableKafkaConsumerRepository {

    @Slf4j
    static class $ implements BeanFactoryPostProcessor {
        @Override
        public void postProcessBeanFactory(final ConfigurableListableBeanFactory configurableListableBeanFactory)
                throws BeansException {
            log.debug("Activating kafka consumer repository...");
        }
    }

}
