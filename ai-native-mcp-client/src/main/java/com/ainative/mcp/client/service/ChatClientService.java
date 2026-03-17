package com.ainative.mcp.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

/**
 * ChatClient 服务
 * 使用 ChatClient 模式调用 MCP Tools
 */
@Service
public class ChatClientService {

    private static final Logger log = LoggerFactory.getLogger(ChatClientService.class);

    private final ChatClient chatClient;

    /**
     * 构造函数
     * 通过 ToolCallbackProvider 将 MCP Tools 注入到 ChatClient
     */
    public ChatClientService(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)  // 使用 toolCallbacks 注入 MCP Tools
                .build();
        log.info("ChatClient initialized with MCP Tools");
    }

    /**
     * 与 AI 对话，AI 可以自动调用 MCP Tools
     *
     * @param userMessage 用户消息
     * @return AI 回复
     */
    public String chat(String userMessage) {
        log.info("User message: {}", userMessage);
        try {
            String response = chatClient.prompt(userMessage)
                    .call()
                    .content();
            log.info("AI response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Chat error", e);
            return "Error: " + e.getMessage();
        }
    }
}
