# AI Native Family Bucket

AI Native 全家桶项目，基于 Spring Boot 和 Spring AI 的 MCP (Model Context Protocol) 实现示例。

## 技术栈

| 技术 | 版本 |
|------|------|
| Java | 21 |
| Spring Boot | 3.4.1 |
| Spring AI | 1.1.3 |
| MCP SDK | 0.17.0 |
| Maven | 3.6+ |

## 项目结构

```
ai-native-family-bucket/
├── pom.xml                              # 父项目 POM
├── ai-native-mcp-server/                # MCP Server 模块
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/ainative/mcp/server/
│       │   ├── McpServerApplication.java
│       │   ├── config/McpServerConfig.java    # MCP Tools 注册
│       │   ├── controller/HealthController.java
│       │   └── tool/WeatherService.java       # 天气查询 Tool
│       └── resources/application.yml
└── ai-native-mcp-client/                # MCP Client 模块
    ├── pom.xml
    └── src/main/
        ├── java/com/ainative/mcp/client/
        │   ├── McpClientApplication.java
        │   ├── controller/
        │   │   ├── ChatController.java        # AI 对话接口
        │   │   ├── McpClientController.java   # MCP Tools 调用接口
        │   │   └── HealthController.java
        │   └── service/
        │       ├── ChatClientService.java     # ChatClient 服务
        │       └── McpClientService.java      # MCP 客户端服务
        └── resources/application.yml
```

## 模块说明

| 模块 | 端口 | 说明 |
|------|------|------|
| ai-native-mcp-server | 8082 | MCP Server，提供天气查询 Tool |
| ai-native-mcp-client | 8083 | MCP Client，集成 ChatClient 和 MCP Tools |

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+

### 编译项目

```bash
mvn clean compile
```

### 启动服务

**1. 启动 MCP Server：**
```bash
mvn spring-boot:run -pl ai-native-mcp-server
```

**2. 启动 MCP Client（新终端）：**
```bash
mvn spring-boot:run -pl ai-native-mcp-client
```

## API 接口

### MCP Tools 接口 (MCP Client)

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/mcp/tools` | GET | 获取所有可用的 MCP Tools |
| `/api/mcp/weather` | GET | 查询天气（参数: latitude, longitude） |

**获取 Tools 列表：**
```bash
curl http://localhost:8083/api/mcp/tools
```

**查询天气：**
```bash
curl "http://localhost:8083/api/mcp/weather?latitude=39.9&longitude=116.4"
```

### AI 对话接口 (MCP Client)

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/chat` | POST | AI 对话，支持自动调用 MCP Tools |
| `/api/chat` | GET | AI 对话（简化版，参数: message） |

**示例：**
```bash
curl -X POST http://localhost:8083/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "查询北京天气"}'
```

### Health Check

| 模块 | Actuator 地址 |
|------|---------------|
| mcp-server | `http://localhost:8082/actuator/health` |
| mcp-client | `http://localhost:8083/actuator/health` |

## 配置说明

### MCP Client LLM 配置

项目默认使用 OpenAI 兼容模式连接 LLM 服务，可修改 `application.yml`：

```yaml
# OpenAI 兼容模式配置
spring.ai.openai.api-key: 你的API密钥
spring.ai.openai.base-url: https://api.openai.com
spring.ai.openai.chat.options.model: gpt-4
```

支持的服务：
- OpenAI
- DashScope (通义千问)
- 其他 OpenAI 兼容 API

## 打包部署

```bash
# 打包所有模块
mvn clean package

# 运行 jar 包
java -jar ai-native-mcp-server/target/ai-native-mcp-server-1.0.0-SNAPSHOT.jar
java -jar ai-native-mcp-client/target/ai-native-mcp-client-1.0.0-SNAPSHOT.jar
```

## License

MIT
