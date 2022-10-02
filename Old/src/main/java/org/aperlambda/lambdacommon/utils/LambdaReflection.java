/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of λjcommon.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.aperlambda.lambdacommon.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.Optional;

/**
 * An utility class for Java Reflection.
 *
 * @version 1.8.0
 * @since 1.1.0
 */
public class LambdaReflection
{
    private LambdaReflection()
    {
        throw new IllegalStateException("LambdaReflection is a full static class!");
    }

    /*
        Fields
     */

    private static void setupField(Field field)
    {
        field.setAccessible(true);
		/*try
		{
			var modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			int modifiers = modifiersField.getInt(field);
			modifiers &= ~Modifier.FINAL;
			modifiersField.setInt(field, modifiers);
		}
		catch (IllegalAccessException | NoSuchFieldException ignored)
		{
		}*/
    }

    /**
     * Gets the field by it's name from a specified class.
     *
     * @param clazz     The specified class.
     * @param fieldName The name of the field.
     * @param declared  Whether the field is declared or not.
     * @return The optional field.
     */
    public static @NotNull Optional<Field> getField(@NotNull Class<?> clazz, @NotNull String fieldName, boolean declared)
    {
        try {
            Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
            setupField(field);
            return Optional.of(field);
        } catch (NoSuchFieldException e) {
            return Optional.empty();
        }
    }

    /**
     * Gets the first field with the specified type in a class.
     *
     * @param clazz The class of the field.
     * @param type  The type of the field.
     * @return The optional field.
     */
    public static @NotNull Optional<Field> getFirstFieldOfType(@NotNull Class<?> clazz, @NotNull Class<?> type)
    {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(type)) {
                setupField(field);
                return Optional.of(field);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets the last field with the specified type in a class.
     *
     * @param clazz The class of the field.
     * @param type  The type of the field.
     * @return The optional field.
     */
    public static @NotNull Optional<Field> getLastFieldOfType(@NotNull Class<?> clazz, @NotNull Class<?> type)
    {
        Field field = null;
        for (Field currentField : clazz.getDeclaredFields()) {
            if (currentField.getType().equals(type))
                field = currentField;
        }
        if (field != null)
            setupField(field);
        return Optional.ofNullable(field);
    }


    /**
     * Sets the value of the specified field in the instance of the specified object.
     *
     * @param object The specified object.
     * @param field  The specified field.
     * @param value  The value to set.
     */
    public static void setValue(@Nullable Object object, @NotNull Field field, @Nullable Object value)
    {
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the value of the specified field in the instance of the specified object.
     *
     * @param object The specified object.
     * @param field  The specified field.
     * @return The optional value.
     */
    public static @Nullable Object getFieldValue(@Nullable Object object, @NotNull Field field)
    {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the value of the specified field in the instance of the specified object.
     *
     * @param object       The specified object.
     * @param field        The specified field.
     * @param defaultValue The default value if cannot access to the field's value.
     * @param <V>          The type of the value to return.
     * @return The optional value.
     */
    @SuppressWarnings("unchecked")
    public static <V> @Nullable V getFieldValue(@Nullable Object object, @NotNull Field field, V defaultValue)
    {
        field.setAccessible(true);
        try {
            return (V) field.get(object);
        } catch (IllegalAccessException e) {
            return defaultValue;
        }
    }

    /*
        Methods
     */
    public static @NotNull Optional<Method> getMethod(@NotNull Class<?> clazz, @NotNull String name, Class<?>... parameterTypes)
    {
        return getMethod(clazz, name, true, parameterTypes);
    }

    /**
     * Gets the method by name of the specified class.
     *
     * @param clazz           The specified class.
     * @param name            The specified name of the method.
     * @param declared        Whether the method is declared or not.
     * @param parameterTypes The parameters of the method.
     * @return The optional method.
     */
    public static @NotNull Optional<Method> getMethod(@NotNull Class<?> clazz, @NotNull String name, boolean declared, Class<?>... parameterTypes)
    {
        try {
            Optional<Method> method = Optional.of(declared ? clazz.getDeclaredMethod(name, parameterTypes) : clazz.getMethod(name, parameterTypes));
            method.ifPresent(m -> m.setAccessible(true));
            return method;
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    /**
     * Invokes the specified method with the specified instance of the object.
     *
     * @param object    The specified object.
     * @param method    The specified method to invoke.
     * @param arguments The arguments.
     * @return The result of the method, returns null if the method returns void.
     */
    public static @Nullable Object invokeMethod(@Nullable Object object, @NotNull Method method, Object... arguments)
    {
        method.setAccessible(true);
        try {
            return method.invoke(object, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /*
        Constructors
     */

    /**
     * Gets the declared constructor with the specified arguments of the specified class.
     *
     * @param clazz     The specified class.
     * @param arguments The arguments of the constructor.
     * @param <T>       The type of the class.
     * @return The optional declared constructor.
     * @see LambdaReflection#getConstructor(Class, boolean, Class[])
     */
    public static <T> @NotNull Optional<Constructor<T>> getConstructor(@NotNull Class<T> clazz, Class<?>... arguments)
    {
        return getConstructor(clazz, true, arguments);
    }

    /**
     * Gets the constructor with the specified arguments of the specified class.
     *
     * @param clazz     The specified class.
     * @param declared  Whether the constructor is declared or not.
     * @param arguments The arguments of the constructor.
     * @param <T>       The type of the class.
     * @return The optional constructor.
     */
    public static <T> @NotNull Optional<Constructor<T>> getConstructor(@NotNull Class<T> clazz, boolean declared, Class<?>... arguments)
    {
        try {
            return Optional.of(declared ? clazz.getDeclaredConstructor(arguments) : clazz.getConstructor(arguments));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    public static <T> @Nullable T newInstance(@NotNull Constructor<T> constructor, Object... arguments)
    {
        constructor.setAccessible(true);
        try {
            return constructor.newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /*
        Class
     */
    public static @NotNull Optional<Class<?>> getClass(String name)
    {
        try {
            return Optional.ofNullable(Class.forName(name));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public static boolean doesClassExist(String name)
    {
        return getClass(name).isPresent();
    }
}
