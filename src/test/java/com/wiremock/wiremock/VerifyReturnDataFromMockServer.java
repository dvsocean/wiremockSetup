package com.wiremock.wiremock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class VerifyReturnDataFromMockServer {

  //Specifies port for mock server
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(5050);


  @Test
  public void shouldReturnValueFromMockServer() {
    //starts mock server
    wireMockRule.start();

    //1. Stub the endpoint
    stubFor(get(urlEqualTo("/flyTeam"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"name\":\"Alexander\"}")));

    //2. Make a request
    RestTemplate restTemplate = new RestTemplate();
    String resourceUrl = "http://localhost:5050/flyTeam";
    ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);

    //3. Verify
    assertNotNull(response);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertTrue(response.getBody().contains("Alexander"));

    //stops mock server
    wireMockRule.stop();
  }

}
