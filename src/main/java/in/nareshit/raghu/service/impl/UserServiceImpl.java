package in.nareshit.raghu.service.impl;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.nareshit.raghu.model.User;
import in.nareshit.raghu.repo.UserRepository;
import in.nareshit.raghu.service.IUserService;

@Service
public class UserServiceImpl implements IUserService,UserDetailsService {

	@Autowired
	private UserRepository repo; //HAS-A

	@Autowired
	private BCryptPasswordEncoder pwdEncoder;

	//save method
	@Override
	public Integer saveUser(User user) 
	{
		// Encode Password
		user.setPassword(
				pwdEncoder.encode(
						user.getPassword())
				);
		return repo.save(user).getId();
	}

	//get user by username
	@Override
	public Optional<User> findByUsername(String username) {
		return repo.findByUsername(username);
	}

	//-------------------------------------------//
	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException 
	{
		Optional<User> opt = findByUsername(username);
		if(opt.isEmpty())
			throw new UsernameNotFoundException("User not exist");

		//read user (from DB)
		User user = opt.get();

		return new org.springframework.security.core.userdetails.User(
				username, 
				user.getPassword(), 
				user.getRoles().stream()
				.map(role->new SimpleGrantedAuthority(role))
				.collect(Collectors.toList())
				);
	}



}
