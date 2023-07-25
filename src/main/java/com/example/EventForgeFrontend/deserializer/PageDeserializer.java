package com.example.EventForgeFrontend.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageDeserializer<T> extends StdDeserializer<Page<T>> {

    private final Class<T> elementClass;

    public PageDeserializer(Class<T> elementClass) {
        super(Page.class);
        this.elementClass = elementClass;
    }

    @Override
    public Page<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode contentNode = node.get("content");
        List<T> content = new ArrayList<>();
        if (contentNode != null) {
            for (JsonNode elementNode : contentNode) {
                content.add(p.getCodec().treeToValue(elementNode, elementClass));
            }
        }
        JsonNode pageableNode = node.get("pageable");
        int number = pageableNode.get("pageNumber").asInt();
        int size = pageableNode.get("pageSize").asInt();
        long totalElements = node.get("totalElements").asLong();

        return new PageImpl<>(content, PageRequest.of(number, size), totalElements);
    }
}
