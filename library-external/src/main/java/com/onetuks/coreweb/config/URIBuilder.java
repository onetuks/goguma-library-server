package com.onetuks.coreweb.config;

import java.net.URI;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
public class URIBuilder {

  public URI buildUri(String baseUri, MultiValueMap<String, String> params) {
    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
    factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

    return factory.uriString(baseUri).queryParams(params).build();
  }

  public URI buildUri(String uri) {
    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
    factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

    return factory.uriString(uri).build();
  }
}
