package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.lang;

import java.util.Collection;
import java.util.Map;

/**
 * Проверочные утверждения.
 */
@SuppressWarnings("unused")
public final class Assert {

    private Assert() {
    }

    /**
     * isTrue.
     *
     * @param expression expression
     * @param message    message
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * isTrue.
     *
     * @param expression expression
     */
    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    /**
     * isNull.
     *
     * @param object  object
     * @param message message
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * isNull.
     *
     * @param object object
     */
    public static void isNull(Object object) {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    /**
     * notNull.
     *
     * @param object  object
     * @param message message
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * notNull.
     *
     * @param object object
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    /**
     * hasLength.
     *
     * @param text    text
     * @param message message
     */
    public static void hasLength(String text, String message) {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * hasLength.
     *
     * @param text text
     */
    public static void hasLength(String text) {
        hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    /**
     * hasText.
     *
     * @param text    text
     * @param message message
     */
    public static void hasText(String text, String message) {
        if (text == null || text.trim().length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * hasText.
     *
     * @param text text
     */
    public static void hasText(String text) {
        hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
    }

    /**
     * doesNotContain.
     *
     * @param textToSearch textToSearch
     * @param substring    substring
     * @param message      message
     */
    public static void doesNotContain(String textToSearch, String substring, String message) {
        if (textToSearch == null || textToSearch.length() == 0 ||
                substring == null || substring.length() == 0 ||
                !textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * doesNotContain.
     *
     * @param textToSearch textToSearch
     * @param substring    substring
     */
    public static void doesNotContain(String textToSearch, String substring) {
        doesNotContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
    }

    /**
     * notEmpty.
     *
     * @param array   array
     * @param message message
     */
    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * notEmpty.
     *
     * @param array array
     */
    public static void notEmpty(Object[] array) {
        notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
    }

    /**
     * notEmpty.
     *
     * @param array array
     * @param msg   msg
     */
    public static void notEmpty(byte[] array, String msg) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * notEmpty.
     *
     * @param collection collection
     * @param message    message
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * notEmpty.
     *
     * @param collection collection
     */
    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    /**
     * notEmpty.
     *
     * @param map     map
     * @param message message
     */
    public static void notEmpty(Map<?, ?> map, String message) {
        if (map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * notEmpty.
     *
     * @param map map
     */
    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }

    /**
     * noNullElements.
     *
     * @param array   array
     * @param message message
     */
    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (Object o : array) {
                if (o == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    /**
     * noNullElements.
     *
     * @param array array
     */
    public static void noNullElements(Object[] array) {
        noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
    }

    /**
     * isInstanceOf.
     *
     * @param clazz clazz
     * @param obj   obj
     */
    public static void isInstanceOf(Class<?> clazz, Object obj) {
        isInstanceOf(clazz, obj, "");
    }

    /**
     * isInstanceOf.
     *
     * @param type    type
     * @param obj     obj
     * @param message message
     */
    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(message + "Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
                    "] must be an instance of " + type);
        }
    }

    /**
     * isAssignable.
     *
     * @param superType superType
     * @param subType   subType
     */
    public static void isAssignable(Class<?> superType, Class<?> subType) {
        isAssignable(superType, subType, "");
    }

    /**
     * isAssignable.
     *
     * @param superType superType
     * @param subType   subType
     * @param message   message
     */
    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(message + subType + " is not assignable to " + superType);
        }
    }

    /**
     * state.
     *
     * @param expression expression
     * @param message    message
     */
    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * state.
     *
     * @param expression expression
     */
    public static void state(boolean expression) {
        state(expression, "[Assertion failed] - this state invariant must be true");
    }

    /**
     * equals.
     *
     * @param arg1    arg1
     * @param arg2    arg2
     * @param message message
     */
    public static void equals(String arg1, String arg2, String message) {
        if (arg1 == null || arg1.length() == 0 ||
                arg2 == null || arg2.length() == 0 ||
                !arg1.equals(arg2)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * equalsIgnoreCase.
     *
     * @param arg1    arg1
     * @param arg2    arg2
     * @param message message
     */
    public static void equalsIgnoreCase(String arg1, String arg2, String message) {
        if (arg1 == null || arg1.length() == 0 ||
                arg2 == null || arg2.length() == 0 ||
                !arg1.equalsIgnoreCase(arg2)) {
            throw new IllegalArgumentException(message);
        }
    }
}
