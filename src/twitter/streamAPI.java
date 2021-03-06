package twitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Stream;
import java.util.Properties;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class streamAPI {

	
			public static void main(String[] args) throws TwitterException {
				streamAPI stream = new streamAPI();
			    stream.execute();
			  }

			  private final Object lock = new Object();
			  public List<Status> execute() throws TwitterException {

			    final List<Status> statuses = new ArrayList<Status>();
				FileInputStream fileInput = null;
				Properties prop = new Properties();
				try {
					fileInput = new FileInputStream(".\\twitter.properties");
					
					  prop.load(fileInput);
					} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
								
				String consumer_Key = prop.getProperty("consumer_key");
				String consumer_Secret = prop.getProperty("consumer_secret");
				String access_Key = prop.getProperty("access_key");		
				String access_token_Secret = prop.getProperty("access_token_secret");		
			
			    ConfigurationBuilder cb = new ConfigurationBuilder();
			    cb.setDebugEnabled(true);
			    cb.setOAuthConsumerKey(consumer_Key);
			    cb.setOAuthConsumerSecret(consumer_Secret);
			    cb.setOAuthAccessToken(access_Key);
			    cb.setOAuthAccessTokenSecret(access_token_Secret);

			    TwitterStream twitterStream = new TwitterStreamFactory(cb.build())
			        .getInstance();

			    StatusListener listener = new StatusListener() {

			      public void onStatus(Status status) {
			        statuses.add(status);
			        System.out.println(statuses.size() + ":" + status.getText());
			        if (statuses.size() > 100) {
			          synchronized (lock) {
			            lock.notify();
			          }
			          System.out.println("unlocked");          
			        }
			      }

			      public void onDeletionNotice(
			          StatusDeletionNotice statusDeletionNotice) {
			        System.out.println("Got a status deletion notice id:"
			            + statusDeletionNotice.getStatusId());
			      }

			      public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			        System.out.println("Got track limitation notice:"
			            + numberOfLimitedStatuses);
			      }

			      public void onScrubGeo(long userId, long upToStatusId) {
			        System.out.println("Got scrub_geo event userId:" + userId
			            + " upToStatusId:" + upToStatusId);
			      }

			      public void onException(Exception ex) {
			        ex.printStackTrace();
			      }

			      @Override
			      public void onStallWarning(StallWarning sw) {
			        System.out.println(sw.getMessage());

			      }
			    };

			    FilterQuery fq = new FilterQuery();
			    String keywords[] = { "federer", "nadal", "#immigration", "trump","stormy" };

			    fq.track(keywords);


			    twitterStream.addListener(listener);
			    twitterStream.filter(fq);

			    try {
			      synchronized (lock) {
			        lock.wait();
			      }
			    } catch (InterruptedException e) {
			      // TODO Auto-generated catch block
			      e.printStackTrace();
			    }
			    System.out.println("returning statuses");
			    twitterStream.shutdown();
			    return statuses;
			  }
	}
