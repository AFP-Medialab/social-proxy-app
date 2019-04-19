package com.afp.medialab.weverify.socialproxy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.afp.medialab.weverify.socialproxy.model.TwitterSearchAPIModel;
/**
 * Controller that provide API endpoint to use social network services
 * @author Bertrand Goupil
 *
 */
@Controller
@Transactional
public class APIController {

	
	@Autowired
	private UsersConnectionRepository connectionRepository;

	/**
	 * Twitter search endpoint
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/api/search")
	@ResponseBody
	public List<Tweet> searchTweets(@RequestBody TwitterSearchAPIModel search) {
		
		ConnectionRepository connRepo = connectionRepository.createConnectionRepository(search.getTwitterUser());
		Connection<?> connection = connRepo.getPrimaryConnection(Twitter.class);
		Twitter twitter = (Twitter) connection.getApi();
		SearchResults results = twitter.searchOperations().search(search.getQuery());
		List<Tweet> tweets = results.getTweets();
		return tweets;

	}
}
