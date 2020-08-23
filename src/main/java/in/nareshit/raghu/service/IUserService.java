package in.nareshit.raghu.service;

import java.util.Optional;

import in.nareshit.raghu.model.User;

public interface IUserService {

	Integer saveUser(User user);
	Optional<User> findByUsername(String username);
}
