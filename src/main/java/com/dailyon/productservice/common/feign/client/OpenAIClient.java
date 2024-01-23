package com.dailyon.productservice.common.feign.client;

import com.dailyon.productservice.brand.dto.response.ReadBrandListResponse;
import com.dailyon.productservice.brand.dto.response.ReadBrandResponse;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryListResponse;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryResponse;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.feign.response.OpenAIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class OpenAIClient {

    private final Environment environment;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public String getSearchResults(String searchQuery) throws Exception {
        List<ReadBrandResponse> brands = ReadBrandListResponse
                .fromEntity(brandRepository.findAll())
                .getBrandResponses();

        String allBrands = brands.stream()
                .map(ReadBrandResponse::toString)
                .collect(Collectors.joining(","));

        List<ReadChildrenCategoryResponse> categories = ReadChildrenCategoryListResponse
                .fromEntity(categoryRepository.findLeafCategories())
                .getCategoryResponses();

        String allCategories = categories.stream()
                .map(ReadChildrenCategoryResponse::toString)
                .collect(Collectors.joining(","));

        Gender[] genders = Gender.values();
        String genderStrings = Arrays.toString(genders);

        List<Map<String, Object>> messages = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", createPrompt(searchQuery, allBrands, allCategories, genderStrings));
        messages.add(message);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("model", "gpt-3.5-turbo-1106");
        requestData.put("messages", messages);
        requestData.put("temperature", 0.3);
        requestData.put("max_tokens", 300);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(environment.getProperty("open-ai.secret-key"));

        String requestBody = objectMapper.writeValueAsString(requestData);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        String apiEndpoint = "https://api.openai.com/v1/chat/completions";
        Object result = restTemplate.postForObject(apiEndpoint, entity, Object.class);
        log.info("==================================================================");
        log.info(result.toString());
        log.info("==================================================================");
        return objectMapper.writeValueAsString(result);
    }

    private String createPrompt(String searchQuery, String brands, String categories, String genders) {
        // Ensure that brands, categories, and genders are valid JSON arrays before this method is called
        return "{" +
                "\"categories\": [" + categories + "], " +
                "\"brands\": [" + brands + "], " +
                "\"genders\": " + genders + ", " +
                "\"priceRanges\": [" +
                "{\"id\": 1, \"name\": \"$0-$99\"}, " +
                "{\"id\": 2, \"name\": \"$100-$199\"}, " +
                "{\"id\": 3, \"name\": \"$200-$299\"}, " +
                "{\"id\": 4, \"name\": \"$300-$399\"}, " +
                "{\"id\": 5, \"name\": \"over $400\"}" +
                "], " +
                "Search Query: \"" + escapeJson(searchQuery) + "\" " +
                "Based on the search query, let me know the relevant 3 categories, 3 brands, 1 gender, and 1 price range. " +
                "Please provide the answer in the following JSON format. " +
                "{\"categories\": [{\"id\": 1, \"name\": \"Fashion\"}, {\"id\": 2, \"name\": \"Electronics\"}, {\"id\": 3, \"name\": \"Home & Living\"}], " +
                "\"brands\": [{\"id\": 1, \"name\": \"Nike\"}, {\"id\": 2, \"name\": \"Samsung\"}, {\"id\": 3, \"name\": \"Apple\"}], " +
                "\"genders\": [{\"value\": \"MALE\", \"name\": \"남성\"}], " +
                "\"priceRanges\": [{\"id\": 1, \"name\": \"$0-$99\"}]}" +
                "}";
    }

    private String escapeJson(String jsonContainingString) {
        return jsonContainingString.replace("\"", "\\\"");
    }
}