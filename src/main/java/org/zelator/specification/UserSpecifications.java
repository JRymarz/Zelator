package org.zelator.specification;

import org.springframework.data.jpa.domain.Specification;
import org.zelator.entity.User;

public class UserSpecifications {


    public static Specification<User> isMember() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("role"), User.Role.Member);
    }

    public static Specification<User> belongsToGroup(Long groupId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("group").get("id"), groupId);
    }

    public static Specification<User> hasGroup(Boolean hasGroup) {
        if(hasGroup) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("group"));
        } else {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("group"));
        }
    }


    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }


    public static Specification<User> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

}
