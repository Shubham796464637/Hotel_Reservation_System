package com.project;

import com.Human.Rectangle;
import com.sun.jdi.request.StepRequest;

import java.sql.*;
import java.util.Scanner;

public class Hotel_Reservations_System {

    private static final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static final String username="root";
    private static final String password="shubh@222101@";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("--------(Connection is done)--------");
            Scanner scanner = new Scanner(System.in);
            Statement statement =connection.createStatement();
            System.out.println();

            while(true)
            {
                System.out.println("----------HOTEL RESERVATION SYSTEM----------");
                System.out.println("-----MENU-----");
                System.out.println(" 1 :- Add a New Reservation Room ");
                System.out.println(" 2 :-  View   Reservation  ");
                System.out.println(" 3 :- Get Room Number ");
                System.out.println(" 4 :- Update Reservation ");
                System.out.println(" 5 :- Delete Reservation ");
                System.out.println(" 6 :-  Exit ");

                System.out.println("Enter a Option :-");
                int choice = scanner.nextInt();

                switch (choice)
                {
                    case 1:
                    {
                        reverseRoom(connection,scanner,statement);
                        break;
                    }
                    case 2:
                    {
                        viewReservation(connection,statement);
                        break;
                    }
                    case 3:
                    {
                        getRoomNumber(connection,scanner,statement);
                        break;
                    }
                    case 4:
                    {
                        updateReservation(connection,scanner,statement);
                        break;
                    }
                    case 5:
                    {
                        deleteReservation(connection,scanner,statement);
                        break;
                    }
                    case 6:
                    {
                        exit();
                        scanner.close();
                        return;
                    }
                    default:
                    {
                        System.out.println("--Invalid choice Pls try Again--");
                    }
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e)
        {
            throw  new RuntimeException(e);
        }
    }

    private static void deleteReservation(Connection connection, Scanner scanner, Statement statement)
    {
        try
        {
            System.out.println(" Enter reservation ID :-");
            int id= scanner.nextInt();

            if(!reservationExists(connection,id))
            {
                System.out.println("-----Reservation is not found for the given Id-----");
                return;
            }

            String q="delete from reservations where reservation_id = " + id;


           int rowEffected=statement.executeUpdate(q);

           if(rowEffected>0)
           {
               System.out.println("-----Data is Successfully Deleted-----");
           }
           else {
               System.out.println("-----Data is not deleted Now-----");
           }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    private static boolean reservationExists(Connection connection, int id)
    {
            try {
                String q = "select reservation_id from reservations where reservation_id = " + id;

                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(q))
                {
                    return resultSet.next();
                }
            catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
                return false;
    }

    private static void exit() throws InterruptedException
    {

        System.out.println("Existing System");
        int i=5;
        while(i!=0)
        {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("--------Thank you Using Hotel Reservation System---------");
    }

    private static void viewReservation(Connection connection, Statement statement) throws SQLException
    {

        String q="select reservation_id,guest_name,room_number,contact_number,reservation_date from reservations";

        try {
           ResultSet resultSet=statement.executeQuery(q);
               System.out.println("------------------------------------------------------------------");
           while(resultSet.next())
           {
               System.out.println("------------------------------------------------------------------");
               int id=resultSet.getInt("reservation_id");
               String name=resultSet.getString("guest_name");
               int roomNum=resultSet.getInt("room_number");
               String pNum=resultSet.getString("contact_number");
               String res_date=resultSet.getString("reservation_date").toString();


               System.out.println(" Reservation Id   :- " + id);
               System.out.println("   Guest Name     :- " + name);
               System.out.println("  Room Number     :- " + roomNum);
               System.out.println(" Contact Number   :- " + pNum);
               System.out.println(" Reservation Date :- " + res_date);
               System.out.println("------------------------------------------------------------------");
           }
               System.out.println("------------------------------------------------------------------");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection connection, Scanner scanner, Statement statement) throws SQLException
    {
        try{

            System.out.println("Enter reservation ID :- ");
            int id=scanner.nextInt();
            scanner.nextLine();

            if(!reservationExists(connection,id))
            {
                System.out.println("-----Reservation is not found on this ID-----");
                return;
            }

            System.out.println("Enter Guest Name :-");
            String g_name=scanner.nextLine();
            System.out.println("Enter Room Number :-");
            int newRoom=scanner.nextInt();
            System.out.println("Enter Contact Number :-");
            String c_num=scanner.next();

            String q="update reservations set guest_name = '" + g_name + "', " +
                    "room_number = " + newRoom + ", " +
                    "contact_number = '" + c_num + "' " +
                    "where reservation_id = " + id ;

           int rowEffected=statement.executeUpdate(q);

           if(rowEffected>0)
           {
               System.out.println("-----Record is Updated Successfully-----");
           }
           else
           {
               System.out.println("-----Record Can't be Added Because Some Error is Given-----");
           }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    private static void getRoomNumber(Connection connection, Scanner scanner, Statement statement) throws SQLException
    {
        try {
            System.out.println(" Enter Reservation Id :-");
            int id= scanner.nextInt();
            System.out.println(" Enter Guest Name :-");
            String name = scanner.next();
            scanner.nextLine();

            String q="select room_number from reservations where" +
                    " reservation_id = " + id +
                    " and guest_name = '" + name + "'";

               ResultSet resultSet=statement.executeQuery(q);
               if (resultSet.next())
               {
                   int room_num=resultSet.getInt("room_number");
                   System.out.println("Room Number for Reservation ID :- " + id + " and Guest Name is :- " + name +" and Room Number is :-" + room_num);
               }
               else
               {
                   System.out.println("-----Reservation not found for this given Id or guest_name-----");
               }
            }
        catch (SQLException e)
            {
                e.printStackTrace();
            }
    }

    private static void reverseRoom(Connection connection, Scanner scanner, Statement statement)throws SQLException {

        try {
            System.out.println("Enter a Guest name:-");
            String guest_name = scanner.next();
            System.out.println("Enter Room Number:-");
            int room_num = scanner.nextInt();
            System.out.println("Enter Contact number:-");
            String contact_name = scanner.next();

            String q = "Insert into reservations(guest_name,room_number,contact_number ) " +
                    "values ('" + guest_name + "'," + room_num + ",'" + contact_name + "')";

            int rowEffected=statement.executeUpdate(q);

            if(rowEffected>0)
            {
                System.out.println("-----Record is Add successfully-----");
            }
            else {
                System.out.println("-----Record inserted Failed-----");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
