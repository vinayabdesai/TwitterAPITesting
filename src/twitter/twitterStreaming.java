package twitter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import twitter4j.auth.AccessToken;
import twitter4j.*;


public class twitterStreaming {

	public static void main(String[] args) throws TwitterException, IOException{
		// This function gives continuous tweets by streaming API i.e displays continuous messages from twitter
		StatusListener listener = new StatusListener(){
	        public void onStatus(Status status) {
	            System.out.println(status.getUser().getName() + " : " + status.getText());
	        }
	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
	        public void onException(Exception ex) {
	            ex.printStackTrace();
	        }
			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}
	    };
	    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	    
	    

		//Twitter twitter = new TwitterFactory().getInstance();
		
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
						
		String consumer_key = prop.getProperty("consumer_key");
		String consumer_secret = prop.getProperty("consumer_secret");
		String access_key = prop.getProperty("access_key");		
		String access_token_secret = prop.getProperty("access_token_secret");		
		AccessToken accessToken = new AccessToken(access_key, access_token_secret);

		twitterStream.setOAuthConsumer(consumer_key,consumer_secret);
		twitterStream.setOAuthAccessToken(accessToken);

		
		twitterStream.addListener(listener);
		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 20)
		    .forEach(i -> executor.submit(atomicInt::incrementAndGet));

		stop(executor);

		//System.out.println(atomicInt.get());   
	    
		
		FilterQuery locationFilter = new FilterQuery();
	    //double[][] locations = {{-180.0d,-90.0d},{180.0d,90.0d}};
	    double[][] locations = {{-122.75,36.8},{-121.75,37.8}};

	    locationFilter.locations(locations);
	    
	   // locationFilter.track("employment");
	    locationFilter.language("en");
	    
	    locationFilter.count(30);

	    twitterStream.filter(locationFilter);
	    
	    
	    // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
	    twitterStream.sample();
	}

	public static void stop(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("termination interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }

		

}
