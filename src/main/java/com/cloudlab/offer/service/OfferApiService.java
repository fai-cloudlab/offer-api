package com.cloudlab.offer.service;

import com.fujitsu.cloudlab.commons.constants.AppConstants;
import com.fujitsu.cloudlab.commons.exception.ApiException;
import com.fujitsu.cloudlab.commons.util.ResponseUtil;
import com.fujitsu.cloudlab.offer.json.model.Offer;
import com.fujitsu.cloudlab.offer.json.model.OffersList;
import com.fujitsu.cloudlab.offer.json.model.Product;
import com.fujitsu.cloudlab.offer.json.model.SearchCriteria;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class OfferApiService {

	  @Autowired RestTemplate restTemplate;

	  @Value("${offer.composer.create.url}")
	  private String offerComposerURL;

	  @Value("${offer.retriever.url}")
	  private String offerRetrieverURL;

	  @CircuitBreaker(name = "offer-composer-cb")
	  public OffersList composerOffer(SearchCriteria searchCriteria, String transactionId)
		      throws ApiException {
		    return ResponseUtil.process(
		    		OffersList.class,
		        restTemplate.exchange(
		        		offerComposerURL,
		            HttpMethod.POST,
		            new HttpEntity<>(searchCriteria, createHeaders(transactionId)),
		            String.class),
		        "OFFER",
		        "Offer Create. ");
		  }

	  @CircuitBreaker(name = "offer-retriever-cb")
	  public OffersList retrieveOffer(String offerId, String transactionId) throws ApiException {
	    return ResponseUtil.process(
	    		OffersList.class,
	        restTemplate.exchange(
	        		offerRetrieverURL + offerId,
	            HttpMethod.GET,
	            new HttpEntity<String>(createHeaders(transactionId)),
	            String.class),
	        "OFFER",
	        "Offer Search. ");
	  }
	  

	  private HttpHeaders createHeaders(String transactionId) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set(AppConstants.TRANSACTION_ID, transactionId);

	    return headers;
	  }


	@SuppressWarnings("unchecked")
	public List<Product> getProducts(String transactionId) throws RestClientException, ApiException {return ResponseUtil.process(
	    		List.class,
	        restTemplate.exchange(
	        		offerComposerURL+"/products",
	            HttpMethod.GET,
	            new HttpEntity<String>(createHeaders(transactionId)),
	            String.class),
	        "OFFER",
	        "Product Search. ");}
}
