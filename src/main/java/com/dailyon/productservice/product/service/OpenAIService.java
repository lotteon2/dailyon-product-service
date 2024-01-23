package com.dailyon.productservice.product.service;

import com.dailyon.productservice.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OpenAIService {

    private final String openAiApiKey = "your-api-key";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public String getSearchResults(String searchQuery) throws Exception {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("model", "gpt-3.5-turbo");
        requestData.put("prompt", createPrompt(searchQuery));
        requestData.put("temperature", 0.3);
        requestData.put("max_tokens", 100);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        String requestBody = objectMapper.writeValueAsString(requestData);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        String apiEndpoint = "https://api.openai.com/v1/completions";
        return restTemplate.postForObject(apiEndpoint, entity, String.class);
    }

    private String createPrompt(String searchQuery) {
        return "Price ranges: [(1, \"$0-$50\"), (2, \"$51-$100\"), (3, \"$101-$500\"), (4, \"$501-$1000\"), (5, \"Over $1001\")]"
                + "Search Query: \"" + searchQuery + "\" "
                + "Based on the search query, "
                + "let me know the relevant 3 categories, 3 brands, 1 gender, and 1 price range:"
                + "like this form below. "
                + "Categories: [(1, \"Fashion\"), (2, \"Electronics\"), (3, \"Home & Living\")] " +
                "Brands: [(1, \"Nike\"), (2, \"Apple\"), (3, \"Samsung\")] " +
                "Genders: [(1, \"Men's\")] " +
                "Price ranges: [(1, \"$0-$5=\")]";
    }
}