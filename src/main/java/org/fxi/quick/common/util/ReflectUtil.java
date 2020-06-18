package org.fxi.quick.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fei
 */
public class ReflectUtil {
  /**
   * 获取类的所有属性，包括父类
   *
   * @param object
   * @return
   */
  public static Field[] getAllFields(Object object) {
    Class<?> clazz = object.getClass();
    List<Field> fieldList = new ArrayList<>();
    while (clazz != null) {
      fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
      clazz = clazz.getSuperclass();
    }
    Field[] fields = new Field[fieldList.size()];
    fieldList.toArray(fields);
    return fields;
  }
}
