/**
 * 
 */
package com.viram.dev.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Viramm
 * 
 * This controller will be used for authorization and authentication
 *
 */
@CrossOrigin(origins = {"*"}, maxAge = 3600)
@RestController
@RequestMapping({ "/identity" })
public class IdentityController {
	
	@Autowired
	private User userService;
	
	//@PostMapping(value = "/registration")
	@RequestMapping(value = "/registration", produces = { "application/JSON" }, method=RequestMethod.POST)
	public String registration(@RequestBody(required = false) UserDTO userDTO) throws Exception {	
		
		if(userDTO == null) {
			throw new ValidationException("Please provide valid information");
		}
		String validationMessage = userService.userValidation(userDTO);
		if(StringUtils.isNotEmpty(validationMessage)) {
			throw new ValidationException(validationMessage);
		}
		if (userService.createUser(userDTO) > 0) {
			return "User registered successfully.";
		} else {
			throw new ValidationException("Contact Support Team");
		}
	}
	
	/*@PostMapping(value = "/register")
	public String processRegister(@RequestBody UserDTO userDTO) {
		
		// authorities to be granted
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));		
		User user = new User(userDTO.getUserName(), bCryptPasswordEncoder.encode(userDTO.getPassword()), authorities);
		userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
		jdbcUserDetailsManager.createUser(user);
		return "Register SuccessFully.";
	}*/
	
	/*@GetMapping(value = "/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");

        return "login";
    }*/
	
	@RequestMapping(value = "/login", produces = { "application/JSON" } ,method=RequestMethod.POST)
	public UserDTO login(@RequestBody(required = false) UserDTO userDTO, HttpServletRequest request, HttpSession session) throws BadRequestException, ResourceNotFoundException {	
		
		if(userDTO == null) {
			throw new ResourceNotFoundException("Please provide valid information");
		}
		if(request.getSession(true)!=null) {
			session = request.getSession();
			//session.setMaxInactiveInterval(30);
		}	
		if (userService.validateDuplicateUser(userDTO) > 0) {
			UserDTO userDTODB = userService.login(userDTO);
			if(userDTODB != null) {
				System.out.println("Welcome");
				session.setAttribute("user", userDTODB);
				return userDTODB;
			} else {
				throw new ResourceNotFoundException("Credientials not matching");
			}
		} else {
			throw new ResourceNotFoundException("Username not exist");
		}
	}
	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpSession session) {	
		session.invalidate();
		return "Logout Successfully";
	}
	
	@GetMapping(value = "/app/version")
	public AppDetail appVersion() {
		return userService.appDetail();
	}
	
	@RequestMapping(value = "/user/identify", produces = { "application/JSON" } ,method=RequestMethod.POST)
	public UserDTO userExist(@RequestBody(required = false) UserDTO userDTO) throws Exception {
		int i = userService.validateDuplicateUser(userDTO);
		if (i > 0) {
			return userDTO;
		}else {
			throw new ResourceNotFoundException("Username not exist");
		}
	}
	
	@RequestMapping(value = "/resetPassword", produces = { "application/JSON" } ,method=RequestMethod.POST)
	public String resetPassword(@RequestBody(required = false) UserDTO userDTO) {
		int i=0;
		i = userService.resetPassword(userDTO);
		if(i > 0) {
			return "Password reseted";
		}
		return "Password not reseted";
	}
	@RequestMapping(value = "/user/{username}", produces = { "application/JSON" } ,method=RequestMethod.GET)
	public UserDTO user(@PathVariable String username) throws BadRequestException, ResourceNotFoundException {	
		UserDTO user = new UserDTO();
		user.setUserName(username);
		UserDTO userDTO = userService.getUser(user);
		if(userDTO == null) {
			throw new ResourceNotFoundException("Please provide valid information");
		}else {
			return userDTO;
		}
		
	}
	
	@RequestMapping(value = "/player/{pid}", produces = { "application/JSON" } ,method=RequestMethod.GET)
	public PlayerDTO playerInfo(@PathVariable String pid) throws BadRequestException, ResourceNotFoundException {	
		PlayerDTO playerDTO = userService.playerInfo(pid).get(0);
		if(playerDTO == null) {
			throw new ResourceNotFoundException("Please provide valid information");
		}else {
			return playerDTO;
		}
		
	}
	
	@RequestMapping(value = "/userProfile/{username}", produces = { "application/JSON" } ,method=RequestMethod.GET)
	public UserDTO userProfile(@PathVariable String username) throws BadRequestException, ResourceNotFoundException {	
		UserDTO user = new UserDTO();
		user.setUserName(username);
		UserDTO userDTO = userService.getUserProfile(user);
		if(userDTO == null) {
			throw new ResourceNotFoundException("Please provide valid information");
		}else {
			return userDTO;
		}
		
	}

}
