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
import java.time.LocalDate;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;
/**
 *
 * @author sunayanasattaru
 */
public class DescribeInstances {
        public static void main(String[] args) {
        // final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("ABC", "XYZ");  
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
                    
                    String dt = new String();
                    String ct = new String();
                    Scanner sc =  new Scanner(System.in);
                    System.out.println("\nEnter the date in yyyy-mm-dd stated in the State Transition Reason: ");
                    dt = sc.nextLine();
                    System.out.println("\nEnter the current date in yyyy-mm-dd: ");
                    ct = sc.nextLine();
                    
                    String TransitionReasonDate = dt;
                    String CurrentDate = ct;
                    
                    try {
                        LocalDate dateBefore = LocalDate.parse(TransitionReasonDate);
                        LocalDate dateAfter = LocalDate.parse(CurrentDate);
		
                        long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
                        
                        if (noOfDaysBetween < 7) {
                            System.out.println("Recently stopped.");
                        }
                        else {
                            System.out.println("It has been more than a week.");
                        }
                        
                        exit(0);
                    } 
                    
                    catch (Exception e) {
                        System.out.println("Exception occured " + e);
                    }                  
                }
            }

            request.setNextToken(response.getNextToken());

            if(response.getNextToken() == null) {
                done = true;
            }
        }
    }
    
}
