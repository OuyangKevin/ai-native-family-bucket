package com.ainative.mcp.server.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 天气预报服务 - MCP Tool
 * 根据经纬度获取天气预报信息
 */
@Service
public class WeatherService {

    private static final Random random = new Random();
    private static final String[] WEATHER_CONDITIONS = {
            "晴", "多云", "阴", "小雨", "中雨", "大雨", "雷阵雨", "小雪", "中雪", "大雪", "雾"
    };
    private static final String[] WIND_DIRECTIONS = {
            "东风", "南风", "西风", "北风", "东南风", "东北风", "西南风", "西北风"
    };

    /**
     * 根据经纬度获取天气预报
     *
     * @param latitude  纬度 (-90 到 90)
     * @param longitude 经度 (-180 到 180)
     * @return 天气预报信息
     */
    @Tool(description = "根据经纬度获取天气预报信息，返回当前天气状况、温度、湿度、风向风力等信息")
    public Map<String, Object> getWeatherByLocation(
            @ToolParam(description = "纬度，范围 -90 到 90") double latitude,
            @ToolParam(description = "经度，范围 -180 到 180") double longitude) {

        // 参数校验
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("纬度必须在 -90 到 90 之间");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("经度必须在 -180 到 180 之间");
        }

        // 模拟天气数据（实际应用中应调用真实的天气API）
        Map<String, Object> weather = new HashMap<>();
        weather.put("location", Map.of(
                "latitude", latitude,
                "longitude", longitude,
                "region", getRegionByCoordinates(latitude, longitude)
        ));
        weather.put("current", Map.of(
                "condition", WEATHER_CONDITIONS[random.nextInt(WEATHER_CONDITIONS.length)],
                "temperature", generateTemperature(latitude),
                "humidity", random.nextInt(40) + 40, // 40-80%
                "wind", Map.of(
                        "direction", WIND_DIRECTIONS[random.nextInt(WIND_DIRECTIONS.length)],
                        "speed", random.nextInt(20) + 1 // 1-20 m/s
                ),
                "visibility", random.nextInt(20) + 5, // 5-25 km
                "pressure", random.nextInt(30) + 1000 // 1000-1030 hPa
        ));
        weather.put("updateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        weather.put("source", "AI Native MCP Weather Service");

        return weather;
    }

    /**
     * 根据坐标推断区域
     */
    private String getRegionByCoordinates(double latitude, double longitude) {
        // 简单的区域判断逻辑
        if (latitude >= 18 && latitude <= 54 && longitude >= 73 && longitude <= 135) {
            return "中国";
        } else if (latitude >= 24 && latitude <= 50 && longitude >= -125 && longitude <= -66) {
            return "美国";
        } else if (latitude >= 35 && latitude <= 72 && longitude >= -10 && longitude <= 40) {
            return "欧洲";
        } else if (latitude >= -10 && latitude <= 10) {
            return "赤道地区";
        } else if (latitude > 60) {
            return "北极地区";
        } else if (latitude < -60) {
            return "南极地区";
        }
        return "其他地区";
    }

    /**
     * 根据纬度生成合理的温度
     */
    private int generateTemperature(double latitude) {
        double absLatitude = Math.abs(latitude);
        // 赤道附近温度高，极地温度低
        int baseTemp = (int) (30 - absLatitude * 0.5);
        int variation = random.nextInt(10) - 5; // -5 到 5 的随机变化
        return baseTemp + variation;
    }
}
