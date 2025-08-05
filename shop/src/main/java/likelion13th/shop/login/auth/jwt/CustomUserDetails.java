package likelion13th.shop.login.auth.jwt;

import likelion13th.shop.domain.User;
import lombok.Getter;

@Getter
public class CustomUserDetails {
    public User getUser() {
        return this.user;
    }
}
