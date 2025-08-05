package likelion13th.shop.service;

import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import likelion13th.shop.repository.UserRepository;
import likelion13th.shop.DTO.response.AddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAddressService {
    private final UserRepository userRepository;

    //내 주소 조회
    public AddressResponse getAddress(User user) {
        Address address = user.getAddress();
        return AddressResponse.from(user.getAddress());
    }
}
