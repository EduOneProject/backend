package io.github.eduoneproject.eduone.business.kernel.security.model;

/**
 * 身份标识ID
 *
 * @author summerain0
 */
public record PrincipalId(String id) {
    public PrincipalId {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }

    /**
     * 创建一个身份标识
     *
     * @param id 身份标识ID
     * @return 身份标识
     */
    public static PrincipalId of(String id) {
        return new PrincipalId(id);
    }
}
