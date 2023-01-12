package com.abinbev.generic.kafkaconsumer.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ErrorMessageTranslator {

  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMessageTranslator.class);
  static final String BUNDLE_PATH = "bundles/errors/errorMessages";

  public static String getErrorMessageForLocale(String exceptionMessage, Locale locale) {
    try {
      return ResourceBundle.getBundle(BUNDLE_PATH, locale).getString(exceptionMessage);
    } catch (MissingResourceException e) {
      LOGGER.warn("Resource not found: {}", exceptionMessage);
      return exceptionMessage;
    }
  }
}
