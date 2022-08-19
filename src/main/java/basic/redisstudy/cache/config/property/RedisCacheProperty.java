package basic.redisstudy.cache.config.property;

public abstract class RedisCacheProperty {
    public static final String USER = "user";
    public static final Long USER_EXPIRE_SECONDS = 600l;

    public static final String DEFAULT = "default";
    public static final Long DEFAULT_EXPIRE_SECONDS = 300l;
}
