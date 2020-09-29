/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.describeinstances;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import static java.lang.System.exit;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringTokenizer;
import java.time.ZoneId;
import java.time.ZonedDateTime;
/**
 *
 * @author sunayanasattaru
 */
public class DescribeInstances {
        public static void main(String[] args) {
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAVCMKIJNMLP5JETUU", "kvUEEyZ+L2DzU8QJ93qa2PBsEtlOmYogV8qjCZVX");  
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                        .withRegion(Regions.US_EAST_1)
                        .build();
        boolean done = false;

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        while(!done) {
            DescribeInstancesResult response = ec2.describeInstances(request);

            for(Reservation reservation : response.getReservations()) {
                for(Instance instance : reservation.getInstances()) {
                    System.out.printf(
                        "Found instance with ID: %s, " +
                        "VPC ID: %s, " +
                        "Type: %s, " +
                        "State: %s, " +
                        "State Transition Reason: %s",
                        instance.getInstanceId(),
                        instance.getVpcId(),
                        instance.getInstanceType(),
                        instance.getState().getName(),
                        instance.getStateTransitionReason());
                        instance.getLaunchTime();
                    
                    String originalStatement = instance.getStateTransitionReason();
                    String dt = extractDtFromString(originalStatement);
                    String TransitionReasonDate = dt;
                    
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                    LocalDateTime now = LocalDateTime.now();
                    
                    try {
                        LocalDate dateBefore = LocalDate.parse(TransitionReasonDate);
                        LocalDateTime dateAfter = now;
		
                        long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
                        
                        if (noOfDaysBetween < 7) {
                            System.out.println("Recently stopped.");
                        }
                        else {
                            System.out.println("It has been more than a week.");
                        }
                    } 
                    catch (Exception e) {
                        System.out.println("Exception occured " + e);
                    }         
                 
                // Start of Launch Time Information Programmming
                Date launchTime = instance.getLaunchTime();
                System.out.println(launchTime);  // Prints out the Launch time 
                SimpleDateFormat DateFor = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String launchTimeFormatted = DateFor.format(launchTime);  // formats launch time into dd/MM/yyyy
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                String ltDateBefore = launchTimeFormatted;
                LocalDate ltDateConverted = LocalDate.parse(ltDateBefore, formatter);
                
                LocalDate dateBefore = ltDateConverted;
                LocalDateTime dateAfter = now;
                
                ZoneId newyorkZoneId = ZoneId.of("America/NewYork");
                ZonedDateTime nyZonedDateTime = dateAfter.atZone(newyorkZoneId);
                ZoneId newYorkZoneId = ZoneId.of("America/New_York");
                ZonedDateTime estDateAfter = nyZonedDateTime.withZoneSameInstant(newYorkZoneId);
            
                ZoneId newyorkZoneId2 = ZoneId.of("America/NewYork");
                ZonedDateTime nyZonedDateTime2 = dateBefore.atZone(newyorkZoneId2);
                ZoneId newYorkZoneId2 = ZoneId.of("America/New_York");
                ZonedDateTime estDateBefore = nyZonedDateTime2.withZoneSameInstant(newYorkZoneId2);
                
                long ltDaysBetween = ChronoUnit.DAYS.between(estDateBefore, estDateAfter); // subtracts the date of both the launch date and current date
                        
                    if (ltDaysBetween < 7) {
                        System.out.println("It has been less than a week since the instance has been launch.");
                    }
                    else {
                        System.out.println("It has been more than a week since the instance has been launch.");
                    }
                    
                }
            }

            request.setNextToken(response.getNextToken());

            if(response.getNextToken() == null) {
                done = true;
            } 
        }
    }
        
    private static String extractDtFromString(String originalStatement) {
        StringTokenizer strkTokenizer = new StringTokenizer(originalStatement, "(");
        strkTokenizer.nextToken();
        String token2 = strkTokenizer.nextToken();
        
        StringTokenizer anTokenizer = new StringTokenizer(token2, " G");
        String newStatement = anTokenizer.nextToken();  // new statement is in  GMT timezone
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  // formats the parsed statement
            String date = newStatement;
            LocalDate dateBeforeConverted = LocalDate.parse(date, formatter);
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  // formats the localDateTime now
            LocalDateTime now = LocalDateTime.now();  // LocalDateTime is in UTC time zone
            
            LocalDate dateBefore = dateBeforeConverted;
            LocalDateTime dateAfter = now;
            
            ZoneId newyorkZoneId = ZoneId.of("America/NewYork");
            ZonedDateTime nyZonedDateTime = dateAfter.atZone(newyorkZoneId);
            ZoneId newYorkZoneId = ZoneId.of("America/New_York");
            ZonedDateTime estDateTimeAfter = nyZonedDateTime.withZoneSameInstant(newYorkZoneId);
            
            ZoneId newyorkZoneId2 = ZoneId.of("America/NewYork");
            ZonedDateTime nyZonedDateTime2 = dateBefore.atZone(newyorkZoneId2);
            ZoneId newYorkZoneId2 = ZoneId.of("America/New_York");
            ZonedDateTime estDateTimeBefore = nyZonedDateTime2.withZoneSameInstant(newYorkZoneId2);
		
            long noOfDaysBetween = ChronoUnit.DAYS.between(estDateTimeBefore, estDateTimeAfter); // calculates the amount of days in between both days presented
                        
            if (noOfDaysBetween < 7) {
                System.out.println("Recently stopped.");
            }
            else {
                System.out.println("It has been more than a week.");
            }
        } 
                    
        catch (Exception e) {
                System.out.println("Exception occured " + e);
        }          
    
        return newStatement;
    }
    
}