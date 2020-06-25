package org.fxi.quick.common.config;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

/**
 * localdate , localDateTime 序列化转换
 */
@Configuration
public class LocalDateTimeSerializerConfig {

  private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
  private static final String DATE_PATTERN = "yyyy-MM-dd";

  /**
   * string转localdate
   */
  @Bean
  public Converter<String, LocalDate> localDateConverter() {
    return new Converter<String, LocalDate>() {
      @Override
      public LocalDate convert(String source) {
        if (source.trim().length() == 0) {
          return null;
        }
        try {
          return LocalDate.parse(source);
        } catch (Exception e) {
          return LocalDate.parse(source, DateTimeFormatter.ofPattern(DATE_PATTERN));
        }
      }
    };
  }

  /**
   * string转localdatetime
   */
  @Bean
  public Converter<String, LocalDateTime> localDateTimeConverter() {
    return new Converter<String, LocalDateTime>() {
      @Override
      public LocalDateTime convert(String source) {

        DateTime dateTime;
        try {
          dateTime = DateUtil.parse(source);
        } catch (Exception e) {
          dateTime = DateUtil.parseDateTime(source.replaceAll("T", " "));
        }
        Date date = dateTime.toJdkDate();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
      }
    };
  }

  /**
   * 统一配置
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    JavaTimeModule module = new JavaTimeModule();
    LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer();
    module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
    return builder -> {
      builder.simpleDateFormat(DATE_TIME_PATTERN);
      builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)));
      builder
          .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
      builder.modules(module);
    };
  }
}
class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
  @Override
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    String dateStr = parser.getText();
    DateTime dateTime;
    try {
      dateTime = DateUtil.parse(dateStr);
    } catch (Exception e) {
      dateTime = DateUtil.parseDateTime(dateStr.replaceAll("T", " "));
    }
    Date date = dateTime.toJdkDate();
    Instant instant = date.toInstant();
    ZoneId zoneId = ZoneId.systemDefault();
    return instant.atZone(zoneId).toLocalDateTime();
  }

  @Override
  public Class<?> handledType() {
    // 关键
    return LocalDateTime.class;
  }
}
