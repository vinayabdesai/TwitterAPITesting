package twitter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TweetSearch {
	
	public static void main(String args[]) throws TwitterException
	{
	//This function is to tweet the messgae on the timeline of given credentials i.e consumer key and consumer secret key 	
		Twitter twitter = new TwitterFactory().getInstance();
	//	Twitter twitter = TwitterFactory.getSingleton();
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
		
		String latestStatus = "This is a testing message again";

		Status status = twitter.updateStatus(latestStatus);
		System.out.println("status id="+status.getId());
		long stat_id = status.getId();
	    System.out.println("Successfully updated the status to [" + status.getText() + "].");
	    //979131395095199745
		
	    long st_id = (Long.valueOf("979131395095199745"));

      //  twitter.destroyStatus(st_id);
	    
	}
	

}
