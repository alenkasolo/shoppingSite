package com.project.shopping_site.Utilities;

import com.project.shopping_site.Entities.User;
import com.project.shopping_site.Security.UserDetailsServices.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public interface GetUser {
    default User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {
            return ((MyUserDetails) principal).getUser();
        }
        return null;
    }
}
