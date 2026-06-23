package com.agritrade.agritrade.exception;

public class ResourceNotFoundException  extends RuntimeException{

   public ResourceNotFoundException(String message){
       super(message);
   }
}
