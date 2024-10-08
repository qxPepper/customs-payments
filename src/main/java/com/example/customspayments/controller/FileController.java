package com.example.customspayments.controller;

import com.example.customspayments.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/file/")
public class FileController {

    @Autowired
    private FileService paymentService;

    @GetMapping(value = "download")
    public void fileToBase(@RequestParam(value = "filename") String fileName) {
        paymentService.fileToBase(fileName);
    }
}

