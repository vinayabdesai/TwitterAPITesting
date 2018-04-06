package twitter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class twitterSearch {
	public static void main(String [] args) throws TwitterException{
		Twitter twitter = new TwitterFactory().getInstance();
		
		FileInputStream fileInput = null;
		Properties prop = new Properties();
		
		try {
			fileInput = new FileInputStream(".\\twitter.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
						}
	//Loading properties file
						
						try {
						
							prop.load(fileInput);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
		String consumer_key = prop.getProperty("consumer_key");
		String consumer_secret = prop.getProperty("consumer_secret");
		String access_key = prop.getProperty("access_key");		
		String access_token_secret = prop.getProperty("access_token_secret");		
		AccessToken accessToken = new AccessToken(access_key, access_token_secret);

		twitter.setOAuthConsumer(consumer_key,consumer_secret);
		twitter.setOAuthAccessToken(accessToken);

		try {
			Query query = new Query("#ipl");
			query.lang("en");
			query.setCount(20);
			query.setSince("2012-2-12");
			QueryResult result;
			result = twitter.search(query);
			List<Status> tweets = result.getTweets();
			for (Status tweet : tweets) {
				if(tweet.isRetweet() == false)
				{
					
					System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
					
				}
			}
		}
		catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}
       System.out.println("*****************************************");
       
       List<Status> statuses = twitter.getHomeTimeline();
       System.out.println("Showing home timeline.");
       for (Status status : statuses) {
           System.out.println(status.getUser().getName() + ":" +
                              status.getText());
       }
	}

}
