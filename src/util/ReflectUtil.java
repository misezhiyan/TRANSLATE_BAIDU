package util;

import java.lang.reflect.Field;

public class ReflectUtil {
	
	public static Field[] getDeclaredMethods(Class clazz){
		Field[] declaredFields = clazz.getDeclaredFields();
		return declaredFields;
	}
	
}
