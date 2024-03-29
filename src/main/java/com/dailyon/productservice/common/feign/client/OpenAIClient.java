package com.dailyon.productservice.common.feign.client;

import com.dailyon.productservice.brand.dto.response.ReadBrandListResponse;
import com.dailyon.productservice.brand.dto.response.ReadBrandResponse;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryListResponse;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryResponse;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.common.enums.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

        String brandString = brands.stream()
                .map(ReadBrandResponse::toString)
                .collect(Collectors.joining(","));

        List<Category> midCategories = categoryRepository.findByMasterCategory_IdOrderByNameAsc(null)
                .stream()
                .flatMap(rootCategory -> categoryRepository.findByMasterCategory_IdOrderByNameAsc(rootCategory.getId()).stream())
                .collect(Collectors.toList());

        List<ReadChildrenCategoryResponse> categories = ReadChildrenCategoryListResponse
                .fromEntity(midCategories)
                .getCategoryResponses();

        String categoryString = categories.stream()
                .map(ReadChildrenCategoryResponse::toString)
                .collect(Collectors.joining(","));

        Gender[] genders = Gender.values();
        String genderStrings = Arrays.toString(genders);

        List<Map<String, Object>> messages = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", createPrompt(searchQuery, brandString, categoryString, genderStrings));
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
        return objectMapper.writeValueAsString(result);
    }

    public String getTranslatedPrompt(String searchQuery) throws Exception {
        List<Map<String, Object>> messages = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", createTranslatePrompt(searchQuery));
        messages.add(message);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("model", "gpt-3.5-turbo-1106");
        requestData.put("messages", messages);
        requestData.put("temperature", 0.2);
        requestData.put("max_tokens", 300);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(environment.getProperty("open-ai.secret-key"));

        String requestBody = objectMapper.writeValueAsString(requestData);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        String apiEndpoint = "https://api.openai.com/v1/chat/completions";
        Object result = restTemplate.postForObject(apiEndpoint, entity, Object.class);
        return objectMapper.writeValueAsString(result);
    }

    private String createTranslatePrompt(String query) {
        return "Translate this sentence into english: " + "\"" + query + "\"";
    }

    private String createPrompt(String searchQuery, String brandString, String categoryString, String genders) {
        return "{" +
                "\"categories\": [" + categoryString + "], " +
                "\"brands\": [" + brandString + "], " +
                "\"genders\": [" + genders + "], " +
                "\"priceRanges\": [" +
                "{\"id\": 1, \"name\": \"$0-$499\"}, " +
                "{\"id\": 2, \"name\": \"$500-$999\"}, " +
                "{\"id\": 3, \"name\": \"$1000-$1499\"}, " +
                "{\"id\": 4, \"name\": \"$1500-$1999\"}, " +
                "{\"id\": 5, \"name\": \"over $2000\"}" +
                "], " +
                "Search Query: \"" + searchQuery + "\"." +
                "Based on the search query, let me know the relevant maximum 5 categories, maximum 5 brands, 2 gender, and 1 price range. " +
                "Please provide the answer in the json object format. And also Field priceRanges can be null." +
                "{\"categories\":[{\"id\":1, \"name\":\"Fashion\"}, {\"id\":2, \"name\":\"Electronics\"}, {\"id\":3, \"name\": \"Home & Living\"}], " +
                "\"brands\":[{\"id\":1, \"name\":\"Nike\"}, {\"id\":2, \"name\":\"Samsung\"}, {\"id\":3, \"name\":\"Apple\"}], " +
                "\"genders\":[\"MALE\"], " +
                "\"priceRanges\":[{\"id\":1, \"name\":\"$0-$199\"}]}" +
                "}";
    }
}