package com.wiremock.wiremock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class VerifyReturnDataFromMockServer {

  private RestTemplate restTemplate = new RestTemplate();

  //Specifies port for mock server
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(5050);


  @Test
  public void shouldReturnValueFromMockServerGET_METHOD() {
    //starts mock server
    wireMockRule.start();

    //1. Stub the endpoint
    stubFor(get(urlEqualTo("/flyTeam"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"name\":\"Alexander\"}")));

    //2. Make a request
    String resourceUrl = "http://localhost:5050/flyTeam";
    ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);

    //3. Verify
    assertNotNull(response);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertTrue(response.getBody().contains("Alexander"));

    //stops mock server
    wireMockRule.stop();
  }

  @Test
  public void shouldReturnValuePOST_METHOD() {
    wireMockRule.start();

    //1. Stub endpoint
    stubFor(post(urlEqualTo("/bruceWayne"))
      .willReturn(aResponse()
      .withStatus(200)
      .withHeader("Content-Type", "application/json")
      .withBody("{\"model\":\"F550\"}")));

    //2. make a request
    HttpEntity<String> req = new HttpEntity<>("");
    ResponseEntity<String> res = restTemplate.postForEntity("http://localhost:5050/bruceWayne", req, String.class);

    //3. verify
    assertNotNull(res);
    assertTrue(res.getStatusCode().is2xxSuccessful());
    assertTrue(res.getBody().contains("F5"));

    wireMockRule.stop();
  }

}//End of class
