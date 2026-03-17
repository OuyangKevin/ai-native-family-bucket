package com.ainative.mcp.client.service;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP Client 服务
 * 调用 MCP Server 暴露的 Tools
 */
@Service
public class McpClientService {

    private static final Logger log = LoggerFactory.getLogger(McpClientService.class);

    private final List<McpSyncClient> mcpSyncClients;

    public McpClientService(List<McpSyncClient> mcpSyncClients) {
        this.mcpSyncClients = mcpSyncClients;
    }

    /**
     * 获取所有已连接的MCP Server的可用Tools
     */
    public Map<String, Object> listTools() {
        Map<String, Object> result = new HashMap<>();
        
        for (McpSyncClient client : mcpSyncClients) {
            try {
                McpSchema.ListToolsResult toolsResult = client.listTools();
                List<Map<String, Object>> tools = toolsResult.tools().stream()
                        .map(tool -> {
                            Map<String, Object> toolInfo = new HashMap<>();
                            toolInfo.put("name", tool.name());
                            toolInfo.put("description", tool.description());
                            toolInfo.put("inputSchema", tool.inputSchema());
                            return toolInfo;
                        })
                        .toList();
                result.put("tools", tools);
                result.put("count", tools.size());
            } catch (Exception e) {
                log.error("Failed to list tools", e);
                result.put("error", e.getMessage());
            }
        }
        
        return result;
    }

    /**
     * 调用天气预报Tool
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 天气预报信息
     */
    public Map<String, Object> getWeather(double latitude, double longitude) {
        Map<String, Object> result = new HashMap<>();
        
        if (mcpSyncClients.isEmpty()) {
            result.put("error", "No MCP clients available");
            return result;
        }

        McpSyncClient client = mcpSyncClients.get(0);
        
        try {
            // 构建Tool调用参数
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("latitude", latitude);
            arguments.put("longitude", longitude);

            // 调用Tool
            McpSchema.CallToolResult callResult = client.callTool(
                    new McpSchema.CallToolRequest("getWeatherByLocation", arguments)
            );

            // 处理返回结果
            if (callResult.isError() != null && callResult.isError()) {
                result.put("error", "Tool call failed");
                result.put("content", callResult.content());
            } else {
                result.put("success", true);
                result.put("content", callResult.content());
            }
        } catch (Exception e) {
            log.error("Failed to call weather tool", e);
            result.put("error", e.getMessage());
        }

        return result;
    }
}
