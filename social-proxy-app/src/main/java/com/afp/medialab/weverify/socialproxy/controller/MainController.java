package com.afp.medialab.weverify.socialproxy.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.afp.medialab.weverify.socialproxy.model.AppUserForm;
import com.afp.medialab.weverify.socialproxy.model.TwitterSearchForm;
import com.afp.medialab.weverify.socialproxy.utils.SecurityUtil;
import com.afp.medialab.weverify.socialproxy.utils.WebUtils;

@Controller
@Transactional
public class MainController {

	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	private UsersConnectionRepository connectionRepository;

	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("message", "This is welcome page!");
		return "welcomePage";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutSuccessfulPage(WebRequest request, Model model, Principal principal) {
		ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator,
				connectionRepository);
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		AppUserForm myForm = new AppUserForm(connection);
		ConnectionRepository conrepo = connectionRepository.createConnectionRepository(myForm.getUserName());
		conrepo.removeConnection(connection.getKey());
		SecurityUtil.logOutUser((Authentication) principal);
		return "redirect:/";
	}

	private AppUserForm connectUser(WebRequest request) {
		ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator,
				connectionRepository);
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		AppUserForm myForm = null;
		if (connection != null) {

			myForm = new AppUserForm(connection);
			SecurityUtil.logInUser(myForm.getUserName());
			ConnectionRepository conrepo = connectionRepository.createConnectionRepository(myForm.getUserName());
			try {
				conrepo.addConnection(connection);
			} catch (DuplicateConnectionException e) {
				System.out.println("already connected reused");
			}
		} else {
			myForm = new AppUserForm();
		}
		return myForm;
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(WebRequest request, Model model, Principal principal) {
		AppUserForm myForm = connectUser(request);
		model.addAttribute("userInfo", myForm);

		return "userInfoPage";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchPage(WebRequest request, Model model, Principal principal) {
		if (principal == null) {
			connectUser(request);
		}
		model.addAttribute("searchForm", new TwitterSearchForm());
		return "searchPage";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchTweets(WebRequest request, @ModelAttribute("searchForm") TwitterSearchForm searchForm,
			Model model, Principal principal) {
		ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator,
				connectionRepository);
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		Twitter twitter = (Twitter) connection.getApi();
		String searchQuery = searchForm.getQuery();
		SearchResults searchResult = twitter.searchOperations().search(searchQuery);
		model.addAttribute("tweets", searchResult.getTweets());
		return "twitterSearchResultPage";
	}

	@RequestMapping(value = "/twitterResult", method = RequestMethod.GET)
	public String twitterSearchResult(Model model, Principal principal) {

		return "twitterSearchResultPage";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();

			String userInfo = WebUtils.toString(loginedUser);

			model.addAttribute("userInfo", userInfo);

			String message = "Hi " + principal.getName() //
					+ "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);

		}

		return "403Page";
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String login(Model model) {
		return "loginPage";
	}

	@RequestMapping(value = { "/signin" }, method = RequestMethod.GET)
	public String signInPage(Model model) {
		return "redirect:/login";
	}

}
