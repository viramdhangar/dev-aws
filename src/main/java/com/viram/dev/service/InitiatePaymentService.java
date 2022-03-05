package com.viram.dev.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viram.dev.dto.InitiatePaymentDTO;
import com.viram.dev.dto.Order;
import com.viram.dev.dto.PaymentResponse;
import com.viram.dev.dto.SecretDTO;
import com.viram.dev.dto.Token;
import com.viram.dev.dto.TokenOrder;
import com.viram.dev.repository.InitiatePaymentRepo;
import com.viram.dev.repository.OrderRepo;
import com.viram.dev.repository.PaymentResponseRepo;
import com.viram.dev.repository.SecretRepo;
import com.viram.dev.util.Constants;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class InitiatePaymentService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SecretRepo secretRepo;
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private InitiatePaymentRepo initiatePaymentRepo;
	
	@Autowired
	private PaymentResponseRepo paymentResponseRepo;
	

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	/*
	 * public String getTokenWithOk(InitiatePaymentDTO paymentDTO, SecretDTO secret,
	 * Order savedOrder) throws IOException {
	 * 
	 * final MediaType JSON = MediaType.get("application/json; charset=utf-8");
	 * 
	 * ObjectMapper obj = new ObjectMapper(); String json =
	 * obj.writeValueAsString(savedOrder);
	 * 
	 * OkHttpClient client = new OkHttpClient.Builder() .build();
	 * 
	 * RequestBody body = RequestBody.create(JSON, json);
	 * 
	 * Request request = new Request.Builder()
	 * .url("https://test.cashfree.com/api/v2/cftoken/order")
	 * .addHeader("Content-Type", "application/json") .addHeader("x-client-id",
	 * secret.getIdType()) .addHeader("x-client-secret", secret.getIdValue())
	 * .post(body) .build();
	 * 
	 * Call call = client.newCall(request); Response response = call.execute();
	 * System.out.println(response.body().string()); return
	 * response.body().string();
	 * 
	 * }
	 */
	public Token getToken(SecretDTO secret, TokenOrder order) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("x-client-id", secret.getIdType()); 
		headers.set("x-client-secret", secret.getIdValue());
		HttpEntity<TokenOrder> entity = new HttpEntity<TokenOrder>(order, headers);

		return restTemplate
				.exchange("https://test.cashfree.com/api/v2/cftoken/order", HttpMethod.POST, entity, Token.class)
				.getBody();
	}

	public InitiatePaymentDTO initiatePayment(InitiatePaymentDTO paymentDTO) throws IOException {

		// get secret
		SecretDTO secret = secretRepo.findByType(Constants.SECRET);
		
		
		Order order = new Order();
		Order savedOrder = orderRepo.save(order);
		TokenOrder to = new TokenOrder(); 
		to.setOrderId(String.valueOf(savedOrder.getId()));
		to.setOrderAmount(Integer.parseInt(paymentDTO.getOrderAmount()));
		to.setOrderCurrency("INR");
		
		orderRepo.save(savedOrder);
		
		Token tokenGenerated = getToken(secret, to);
		
		paymentDTO.setAppId(secret.getIdType());
		paymentDTO.setOrderCurrency("INR");
		paymentDTO.setOrderAmount(String.valueOf(to.getOrderAmount()));
		paymentDTO.setOrderId(to.getOrderId());
		paymentDTO.setOrderNote("Payment to dev");
		paymentDTO.setCustomerName("Payment to dev");
		paymentDTO.setCustomerEmail("viram.dhangar@gmail.com");
		paymentDTO.setCustomerPhone("8097547286");
		paymentDTO.setStage(secret.getSecretDesc());
		initiatePaymentRepo.save(paymentDTO);
		
		paymentDTO.setTokenData(tokenGenerated.getCftoken());
		
		return paymentDTO;
	}
	
	/*
	 * public SecretDTO getSecret(String type) { SecretDTO conf =
	 * configRepo.findByType(type); if(conf.isPresent()) { return conf.get(); }
	 * return new SecretDTO(); }
	 */
	
	public PaymentResponse savePaymentAudit(Map<String, String> map) {
		return paymentResponseRepo.save(convertToObject(map));
	}
	
	private PaymentResponse convertToObject(Map<String, String> map) {
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setOrderId(map.get("orderId"));
		paymentResponse.setOrderAmount(map.get("orderAmount"));
		paymentResponse.setPaymentMode(map.get("paymentMode"));
		paymentResponse.setReferenceId(map.get("referenceId"));
		paymentResponse.setSignature(map.get("signature"));
		paymentResponse.setTxMsg(map.get("txMsg"));
		paymentResponse.setTxStatus(map.get("txStatus"));
		paymentResponse.setTxTime(map.get("txTime"));
		paymentResponse.setType(map.get("type"));
		return paymentResponse;
	}
	public void verifySignature() throws NoSuchAlgorithmException, InvalidKeyException {
		
		// get secret
		SecretDTO secret = secretRepo.findByType(Constants.SECRET);
		
		LinkedHashMap<String, String> postData = new LinkedHashMap<String, String>();

		/*
		 * postData.put("orderId", ORDERID); postData.put("orderAmount", ORDERAMOUNT);
		 * postData.put("referenceId", REFERENCE_ID); postData.put("txStatus",
		 * TXN_STATUS); postData.put("paymentMode", PAYMENT_MODE); postData.put("txMsg",
		 * TX_MSG); postData.put("txTime", TX_TIME);
		 */

		String data = "";
		Set<String> keys = postData.keySet();

		for (String key : keys) {
		    data = data + postData.get(key);
		}
		String secretKey = secret.getIdValue();
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key_spec = new
		SecretKeySpec(secretKey.getBytes(),"HmacSHA256");
		sha256_HMAC.init(secret_key_spec);

		String signature = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
	}
}
