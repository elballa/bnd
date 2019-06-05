package aQute.bnd.differ;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.codehaus.plexus.util.ReflectionUtils;

/**
 * Defines some utility methods for Reflection.
 * 
 * @author Alessandro Ballarin
 */
public final class ReflectionUtil {

	/**
	 * Create a new Reflection class util.
	 */
	private ReflectionUtil() {

	}

	/**
	 * Return the class' field with a specific name, parsing class and
	 * superclasses as well.
	 * 
	 * @param fieldName the name of the field to extract
	 * @param o object containing the method
	 * @return the class' field
	 */
	public final static Object getField(String fieldName, Object o) {
		return AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				try {
					Field field = ReflectionUtils.getFieldByNameIncludingSuperclasses(fieldName, o.getClass());
					if (field != null) {
						field.setAccessible(true);
					} else {
						new Exception("Field not found");
					}
					return field.get(o);
				} catch (IllegalAccessException e) {
					throw new ReflectionException(e);
				}
			}
		});
	}

	/**
	 * Set the class' field of a specific name to the passed value.
	 * 
	 * @param o object containing the method
	 * @param fieldName the name of the field to extract
	 * @param value the value to set
	 */
	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	public final static void setField(Object o, String fieldName, Object value) {
		AccessController.doPrivileged((PrivilegedAction) () -> {
			try {
				Field field = ReflectionUtils.getFieldByNameIncludingSuperclasses(fieldName, o.getClass());

				field.setAccessible(true);

				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

				field.set(o, value);
				return null;
			} catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
				throw new ReflectionException(e);
			}
		});
	}

	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	public final static void setField(Object o, Field field, Object newValue) throws Exception {
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");

		// wrapping setAccessible
		AccessController.doPrivileged((PrivilegedAction) () -> {
			modifiersField.setAccessible(true);
			return null;
		});

		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, newValue);
	}

	/**
	 * Return the class' method with a specific name, parsing class and
	 * superclasses as well.
	 * 
	 * @param methodName the name of the method to search
	 * @param clazz the class containing the method
	 * @param parameterTypes the array of types of method parameters
	 * @return the class' method
	 */
	public final static Method getMethodByNameIncludingSuperclasses(String methodName, Class<?> clazz,
		Class<?>... parameterTypes) {
		return (Method) AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				Method retValue = null;
				try {
					retValue = clazz.getDeclaredMethod(methodName, parameterTypes);
				} catch (NoSuchMethodException e) { // NOSONAR
					Class<?> superclass = clazz.getSuperclass();
					if (superclass != null) {
						retValue = ReflectionUtil.getMethodByNameIncludingSuperclasses(methodName, superclass,
							parameterTypes);
					}
				}
				if (retValue != null) {
					retValue.setAccessible(true);
				}
				return retValue;
			}
		});
	}

	/**
	 * Invokes a method via reflection, looking for into class and superclasses.
	 * 
	 * @param methodName method name
	 * @param o Object implementing the method
	 * @param parametersClasses method parameters classes array
	 * @param parameters method parameters
	 * @return the return value of Method execution
	 */
	public final static Object callMethod(String methodName, Object o,
		@SuppressWarnings("rawtypes") Class[] parametersClasses, Object... parameters) {
		return AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				try {
					Method m = ReflectionUtil.getMethodByNameIncludingSuperclasses(methodName, o.getClass(),
						parametersClasses);
					return m.invoke(o, parameters);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new ReflectionException(e);
				}
			}
		});
	}
}
