package com.cons.reporteya.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionAdvice{

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
    // Define the redirect URL within your application
	  redirectAttributes.addFlashAttribute("TamanioExedido",true);
    
    // Return the redirect URL as a String
    return "redirect:/home"  ;
  }
}
