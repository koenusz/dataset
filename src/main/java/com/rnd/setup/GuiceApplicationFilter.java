package com.rnd.setup;

import com.google.inject.servlet.GuiceFilter;

import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = "/*")
public class GuiceApplicationFilter extends GuiceFilter {
}  