package org.fxi.quick.module.sys.util;

/**
 * id判断
 * @Author fei
 */
public class ParentIdUtils {
  public static final Long EMPTY_ID = -1L;
  public static boolean isEmpty(Long id){
    if(id == null || EMPTY_ID.equals(id)){
      return true;
    } else {
      return false;
    }
  }

  public static boolean isNotEmpty(Long id){
    return !isEmpty(id);
  }
}
