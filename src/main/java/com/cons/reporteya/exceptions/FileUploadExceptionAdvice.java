package com.cons.reporteya.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionAdvice{

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc,
                                         RedirectAttributes redirectAttributes,
                                         HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("maxUploadSizeExceeded", true);

        String requestUrl = request.getRequestURI();

        if (requestUrl.equals("/reports/new"))
            return "redirect:/reports/new";
        else if(requestUrl.equals("/companies/new"))
            return "redirect:/companies/new";

        return "redirect:/home";
    }
}
