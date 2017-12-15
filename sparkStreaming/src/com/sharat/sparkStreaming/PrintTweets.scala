package com.sharat.sparkStreaming

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext._
import Utilities._

/** Simple application to listen to a stream of Tweets and print them out */
object PrintTweets{
 
  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Configure Twitter credentials using twitter.txt
    setupTwitter()
    
    // Set up a Spark streaming context named "PrintTweets" that runs locally using
    // all CPU cores and one-second batches of data
    val ssc = new StreamingContext("local[*]", "PrintTweets", Seconds(1))
    
    // Get rid of log spam (should be called after the context is set up)
    setupLogging()

    // Create a DStream from Twitter using our streaming context
    //TwitterUtils is provided in spark.streaming 
    val tweets = TwitterUtils.createStream(ssc, None)
    
    // Now extract the text of each status update into RDD's using map()
    // getText since twitter status creates status objects.
    // TwitterUtils creates Dstream of status object and we use getText() to get the text of the tweets
    // returns is DStream
    val statuses = tweets.map(status => status.getText())
    
    // Print out the first ten
    statuses.print()
    
    // Kick it all off
    ssc.start()
    ssc.awaitTermination()
  }  
}