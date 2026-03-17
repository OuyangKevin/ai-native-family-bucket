package com.ainative.mcp.client.controller;

import com.ainative.mcp.client.service.McpClientService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * MCP Client Controller
 * 提供调用 MCP Server Tools 的 REST 接口
 */
@RestController
@RequestMapping("/api/mcp")
public class McpClientController {

    private final McpClientService mcpClientService;

    public McpClientController(McpClientService mcpClientService) {
        this.mcpClientService = mcpClientService;
    }

    /**
     * 列出所有可用的 MCP Tools
     */
    @GetMapping("/tools")
    public Map<String, Object> listTools() {
        return mcpClientService.listTools();
    }

    /**
     * 调用天气预报工具
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 天气预报信息
     */
    @GetMapping("/weather")
    public Map<String, Object> getWeather(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        return mcpClientService.getWeather(latitude, longitude);
    }
}
