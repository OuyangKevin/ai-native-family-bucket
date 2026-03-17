package com.ainative.mcp.client.controller;

import com.ainative.mcp.client.service.ChatClientService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ChatClient Controller
 * 提供 AI 对话接口，AI 可自动调用 MCP Tools
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatClientService chatClientService;

    public ChatController(ChatClientService chatClientService) {
        this.chatClientService = chatClientService;
    }

    /**
     * AI 对话接口
     * AI 会根据用户输入自动判断是否需要调用 MCP Tools
     *
     * @param request 包含 message 字段的请求体
     * @return AI 回复
     */
    @PostMapping
    public Map<String, Object> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.isBlank()) {
            return Map.of("error", "message is required");
        }
        
        String response = chatClientService.chat(message);
        return Map.of(
                "message", message,
                "response", response
        );
    }

    /**
     * 简单的 GET 接口，便于测试
     */
    @GetMapping
    public Map<String, Object> chatGet(@RequestParam String message) {
        String response = chatClientService.chat(message);
        return Map.of(
                "message", message,
                "response", response
        );
    }
}
