package vn.st.schoolmanagement.web.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.st.schoolmanagement.domain.User;
import vn.st.schoolmanagement.security.jwt.JWTFilter;
import vn.st.schoolmanagement.security.jwt.TokenProvider;
import vn.st.schoolmanagement.service.MailService;
import vn.st.schoolmanagement.service.UserService;
import vn.st.schoolmanagement.web.rest.errors.*;
import vn.st.schoolmanagement.web.rest.vm.LoginVM;
import vn.st.schoolmanagement.web.rest.vm.ManagedUserVM;

@Controller
public class AccountController {

  private static class AccountResourceException extends RuntimeException {

    private AccountResourceException(String message) {
      super(message);
    }
  }

  private final UserService userService;

  private final MailService mailService;

  private final TokenProvider tokenProvider;

  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  public AccountController(
    UserService userService,
    MailService mailService,
    TokenProvider tokenProvider,
    AuthenticationManagerBuilder authenticationManagerBuilder
  ) {
    this.userService = userService;
    this.mailService = mailService;
    this.tokenProvider = tokenProvider;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
  }

  @GetMapping("/register")
  public ModelAndView getRegisterForm() {
    ModelAndView mav = new ModelAndView("account/signUp");
    ManagedUserVM managedUserVM = new ManagedUserVM();
    mav.addObject("newUser", managedUserVM);
    return mav;
  }

  @PostMapping("/register")
  public ModelAndView register(@Valid @ModelAttribute ManagedUserVM newUser) {
    if (!checkPasswordLength(newUser.getPassword())) {
      throw new InvalidPasswordException();
    }
    User user = userService.registerUser(newUser, newUser.getPassword());
    mailService.sendActivationEmail(user);
    ModelAndView mav = new ModelAndView("index");
    mav.addObject("successMessage", "Check Email to get activate key");
    return mav;
  }

  @GetMapping("/account/activate")
  public String activateAccount(@RequestParam(value = "key") String key) {
    Optional<User> user = userService.activateRegistration(key);
    if (!user.isPresent()) {
      throw new AccountResourceException("No user was found for this activation key");
    }
    return "redirect:/login";
  }

  @GetMapping("/login")
  public ModelAndView getLoginForm() {
    ModelAndView mav = new ModelAndView("account/signIn");
    LoginVM loginVM = new LoginVM();
    mav.addObject("loginVM", loginVM);
    return mav;
  }

  @PostMapping("/login")
  public ModelAndView authorize(@Valid @ModelAttribute LoginVM loginVM) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
      loginVM.getUsername(),
      loginVM.getPassword()
    );

    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
    String jwt = tokenProvider.createToken(authentication, rememberMe);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

    ModelAndView mav = new ModelAndView("index");
    mav.addObject("successMessage", "Login Successful!");
    return mav;
  }

  private static boolean checkPasswordLength(String password) {
    return (
      !StringUtils.isEmpty(password) &&
      password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
      password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH
    );
  }

  static class JWTToken {
    private String idToken;

    JWTToken(String idToken) {
      this.idToken = idToken;
    }

    @JsonProperty("id_token")
    String getIdToken() {
      return idToken;
    }

    void setIdToken(String idToken) {
      this.idToken = idToken;
    }
  }
}
